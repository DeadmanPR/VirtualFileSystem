package disk;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import diskManagerClasses.FreeBlockManager;
import diskManagerClasses.INodeManager;
import exceptions.DirectoryNotFoundException;
import exceptions.ExistingDirectoryException;
import exceptions.ExistingDiskException;
import exceptions.FullDiskException;
import exceptions.InvalidBlockNumberException;
import exceptions.NonExistingDiskException;

/**
 * Contains operations related to a disk unit. A DiskUnit is a sequence of a fixed number of bytes
 * divided in blocks.
 * @author Jose Antonio Rodriguez Rivera
 *
 */
public class DiskUnit {
	private final static int DEFAULT_CAPACITY = 1024;
	private final static int DEFAULT_BLOCK_SIZE = 256;
	public final static int DISKCAPACITY_OFFSET = 0;
	public final static int BLOCKSIZE_OFFSET = 4;
	public final static int FBROOT_OFFSET = 8;
	public final static int FBINDEX_OFFSET = 12;
	public final static int FFINODE_OFFSET = 16;
	public final static int NUMBEROFINODES_OFFSET = 20;
	private int capacity, blockSize;
	private RandomAccessFile disk;
	private LinkedList<Integer> blocksDamaged = new LinkedList<Integer>();
	private File blocksDamagedFile;
	private INodeManager iNodeManager;	
	private FreeBlockManager fbManager;
	private static File f = new File("src" + File.separator + "DiskUnits" + File.separator);
	



	/**
	 * Creates a new DiskUnit.
	 * @param name the name of the disk
	 */
	private DiskUnit(String name){
		try{
			File raf = new File(f, name);
			disk = new RandomAccessFile(raf, "rw");
			blocksDamagedFile = new File(f, name + "BlocksDamaged.txt");
			loadBlocksDamagedFromFile();	


		}
		catch (IOException e){
			System.err.println("Unable to start the disk.");
			System.exit(1);
		}
	}


	/**
	 * Turns on an existing disk unit.
	 * @param name the name of the disk unit to activate
	 * @return the corresponding DiskUnit object
	 * @throws NonExistingDiskException whenever no
	 *   	   disk with the specified name is found.
	 */
	public static DiskUnit mount(String name) throws NonExistingDiskException{
		File file=new File(f, name);
		if (!file.exists())
			throw new NonExistingDiskException("No disk has name : " + name);

		DiskUnit dUnit = new DiskUnit(name);
		// get the capacity and the block size of the disk from the file
		// representing the disk
		try {
			dUnit.disk.seek(0);
			dUnit.capacity = dUnit.disk.readInt();
			dUnit.blockSize = dUnit.disk.readInt();
			dUnit.iNodeManager = new INodeManager(dUnit);
			dUnit.fbManager = new FreeBlockManager(dUnit);

			boolean newDisk = false;

			//INODES
			int numberOfINodes = DiskUtils.getNumberOfINodes(dUnit);
			dUnit.disk.seek(dUnit.blockSize);

			for(int i = 0; i < numberOfINodes && !newDisk ; i++){
				dUnit.iNodeManager.addINode(new INode(dUnit.disk.readInt(), dUnit.disk.readInt(), dUnit.disk.readByte()));
				if(i == 0){
					if(dUnit.iNodeManager.getINode(0).getSize() == 0){
						dUnit.reserveINodeSpace();
						newDisk = true;
					}
				}
			}
			
			dUnit.iNodeManager.loadFreeINodes();



//
			//FREE BLOCKS
			if(newDisk)
				dUnit.fbManager.setInitialFBArray();


		} catch (IOException e) {
			e.printStackTrace();
		}

		return dUnit;     	
	}

	/**
	 * Turns off an existing disk unit.
	 */
	public void shutdown(){
		iNodeManager.saveINodesToDisk();

		try{
			disk.close();
			saveBlocksDamagedToFile();
		}
		catch (IOException e){
			e.printStackTrace();
		}

	}

	/***
	 * Creates a new disk unit with the given name. The disk is formatted
	 * as having default capacity (number of blocks), each of default
	 * size (number of bytes). Those values are: DEFAULT_CAPACITY and
	 * DEFAULT_BLOCK_SIZE. The created disk is left as in off mode.
	 * @param name the name of the file that is to represent the disk.
	 * @throws ExistingDiskException whenever the name attempted is
	 * already in use.
	 */
	public static void createDiskUnit(String name) throws ExistingDiskException{
		createDiskUnit(name, DEFAULT_CAPACITY, DEFAULT_BLOCK_SIZE);
	}

	/**
	 * Creates a new disk unit with the given name. The disk is formatted
	 * as with the specified capacity (number of blocks), each of specified
	 * size (number of bytes).  The created disk is left as in off mode.
	 * @param name the name of the file that is to represent the disk.
	 * @param capacity number of blocks in the new disk
	 * @param blockSize size per block in the new disk
	 * @throws ExistingDiskException whenever the name attempted is
	 * already in use.
	 * @throws InvalidParameterException whenever the values for capacity
	 *  or blockSize are not valid according to the specifications
	 */
	public static void createDiskUnit(String name, int capacity, int blockSize) throws ExistingDiskException, InvalidParameterException{
		if(!f.exists())
			f.mkdirs();

		File file=new File(f,name);
		if (file.exists())
			throw new ExistingDiskException("Disk name is already used: " + name);

		RandomAccessFile disk = null;
		if (capacity < 0 || blockSize < 0 ||!DiskUtils.powerOf2(capacity) || !DiskUtils.powerOf2(blockSize))
			throw new InvalidParameterException("Invalid values: " + " capacity = " + capacity + " block size = " +  blockSize);
		// disk parameters are valid... hence create the file to represent the
		// disk unit.
		try {
			disk = new RandomAccessFile(file, "rw");
		}
		catch (IOException e) {
			System.err.println ("Unable to start the disk");
			System.exit(1);
		}

		reserveDiskSpace(disk, capacity, blockSize);


		// after creation, just leave it in shutdown mode - just
		// close the corresponding file
		try {
			disk.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	/**
	 * Writes the content of a VirtualDiskBlock into the corresponding block in the DiskUnit
	 * given by blockNum.
	 * @param blockNum the number of the block that's going to be written in
	 * @param b the VirtualDiskBlock containing the data to be written
	 * @throws InvalidBlockNumberException whenever blockNum is an invalid number (less than 0 or more than capacity)
	 */
	public void write(int blockNum, VirtualDiskBlock b) throws InvalidBlockNumberException{

		//Checks if the block number is valid.
		if(blockNum < 0 || blockNum > capacity)
			throw new InvalidBlockNumberException("The block number is not valid. | number = " + blockNum);

		//Checks if the block is not damaged, so that it can be written to.
		if(!isDamaged(blockNum)){
			//Goes to the given block in the file and writes the contents of the VirtualDiskBlock
			try {
				for(int i = 0; i < capacity ; i++)
					disk.seek(blockNum * blockSize);
				disk.write(b.getAll());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


	}

	/**
	 * Reads a block from the DiskUnit, given by blockNum. The data that is read will
	 * be copied to the VirtualDiskBlock b.
	 * @param blockNum the number of the block from where the data will be read
	 * @param b the VirtualDiskBlock where the data will be copied to
	 * @throws InvalidBlockNumberException whenever blockNum is an invalid number (less than 0 or more than capacity)
	 */
	public void read(int blockNum, VirtualDiskBlock b) throws InvalidBlockNumberException{

		//Checks if the block number is valid
		if(blockNum < 0 || blockNum > capacity)
			throw new InvalidBlockNumberException("The block number is not valid. | number = " + blockNum);

		//Checks if the block is damaged and notifies the user.
		if(isDamaged(blockNum)){
			System.out.print("Error: Can't read from this block. This block is damaged!");
		}
		//Goes to the given block number in the file, reads the block and copies the contents on the given VirtualDiskBlock
		try {
			disk.seek(blockNum * blockSize);
			byte[] bytesToRead = new byte[blockSize];
			disk.read(bytesToRead);
			for(int i = 0 ; i < bytesToRead.length ; i++)
				b.setElement(i, bytesToRead[i]);

		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	/**
	 * Returns the capacity of the disk. That is, the number of blocks the disk has.
	 * @return the maximum number of blocks the disk has
	 */
	public int getCapacity(){
		return capacity;
	}

	/**
	 * Returns the number of bytes each disk block has.
	 * @return the number of bytes a block has
	 */
	public int getBlockSize(){
		return blockSize;
	}

	/**
	 * Fills the disk unit with 0's (empties its contents). Also marks some blocks as damaged
	 * with a probability of 1 x 10^-5
	 */
	public void lowLevelFormat(){
		Random rng = new Random();

		//Goes to the start of the file and writes 0 on all blocks except block 0 (to retain
		//capacity and blockSize in order to be usable again)
		try {
			disk.seek(blockSize);
			for(int i = 0; i < capacity ; i++){
				for(int j =0; j < blockSize ; j++)
					disk.writeByte(0);

				//Marks blocks as damaged with a probability of 1 x 10^-5
				if(rng.nextInt(99999) == 1)
					blocksDamaged.add(i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	/**
	 * Sets the length of the disk, and writes the capacity and blockSize to the first
	 * block of the disk (in that order).
	 * @param disk the disk to be written to
	 * @param capacity the maximum number of blocks
	 * @param blockSize the number of bytes per block
	 */
	private static void reserveDiskSpace(RandomAccessFile disk, int capacity, int blockSize){
		try {
			disk.setLength(blockSize * capacity);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// write disk parameters (number of blocks, bytes per block) in
		// block 0 of disk space
		try {
			disk.seek(0);
			disk.writeInt(capacity);  
			disk.writeInt(blockSize);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Loads the blocks damaged from this disk to the internal list.
	 */
	private void loadBlocksDamagedFromFile(){
		if(blocksDamagedFile.exists()){
			try {
				Scanner input = new Scanner(blocksDamagedFile);
				while(input.hasNextInt()){
					//Add the block numbers to the list
					blocksDamaged.add(input.nextInt());
				}

				input.close();
			} catch (FileNotFoundException e) {
				System.out.println("Error: The file was not found.");
			}

		}

	}

	/**
	 * Saves the blocks damaged to the internal list.
	 */
	private void saveBlocksDamagedToFile(){
		try{
			PrintWriter output = new PrintWriter(blocksDamagedFile);
			for(int i=0; i<blocksDamaged.size(); i++)
				//Print the block numbers to a file
				output.println(blocksDamaged.get(i));

			output.close();
		}catch (FileNotFoundException e){
			System.out.println("Error: The file was not found.");

		}
	}

	/**
	 * Checks if the block is damaged.
	 * @param blockNum the block to search for
	 * @return true if the block is damaged
	 */
	private boolean isDamaged(int blockNum){
		boolean isDamaged = false;
		for(int i=0; i<blocksDamaged.size(); i++)
			if(blocksDamaged.get(i) == blockNum)
				isDamaged = true;

		return isDamaged;
	}

	/**
	 * Reserves the space of the INodes and updates the INodeManager.
	 */
	private void reserveINodeSpace(){
		iNodeManager.clearINodes();

		try {
			disk.seek(blockSize);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int numberOfINodeBlocks = DiskUtils.getNumberOfINodeBlocks(this);
		int numberOfINodes = DiskUtils.getNumberOfINodes(this);
		INode node = new INode(numberOfINodeBlocks+1, blockSize-4, (byte)1); //Root directory
		
		if(numberOfINodes == 1)
			node.setFirstBlock(0);
		iNodeManager.addINode(node);

		for(int i = 2 ; i <= numberOfINodes ; i++){
			INode n = new INode(i % numberOfINodes , 0, (byte)0);
			iNodeManager.addINode(n);
		}
	}

	/**
	 * Deletes this DiskUnit.
	 * @param name the name of this DiskUnit
	 * @throws NonExistingDiskException if the disk does not exist
	 */
	public void delete(String name) throws NonExistingDiskException{
		File file = new File(f, name);
		File file2 = new File(f, name + "BlocksDamaged.txt");
		if(file.exists()){
			file.delete();
			if(file2.exists())
				file2.delete();
		}
		else
			throw new NonExistingDiskException("DiskUnit " + name + " does not exist!");


	}

	/**
	 * Formats this disk with the given capacity and block size.
	 * @param name the name of this DiskUnit
	 * @param capacity the number of blocks
	 * @param blockSize the size of the blocks
	 * @throws NonExistingDiskException if the disk does not exist
	 * @throws InvalidParameterException if capacity and/or block size are not powers of 2
	 */
	public void format(String name, int capacity, int blockSize) throws NonExistingDiskException, InvalidParameterException{
		if(!DiskUtils.powerOf2(capacity) || !DiskUtils.powerOf2(blockSize))
			throw new InvalidParameterException();

		delete(name);
		DiskUnit.createDiskUnit(name, capacity, blockSize);
	}
	
	/**
	 * Returns the free block structure.
	 */
	public String showFreeBlocks(){
		return fbManager.showFreeBlocks();
	}
	
	//***********************Testing purposes************************
	/**
	 * Returns the INodeManager for this DiskUnit.
	 * @return the INodeManager for this DiskUnit
	 */
	public INodeManager getINodeManager(){
		return iNodeManager;
	}
	
	/**
	 * Returns the FreeBlockManager for this DiskUnit.
	 * @return the FreeBlockManager for this DiskUnit
	 */
	public FreeBlockManager getFBManager(){
		return fbManager;
	}

//	/**
//	 * Creates a new directory in this DiskUnit.
//	 * @param name the name of the directory
//	 * @param size the size of the directory
//	 * @throws ExistingDirectoryException if the name used for the directory is already taken
//	 */
//	public void createNewDirectory(String name, int size) throws ExistingDirectoryException{
//		Directory dtc = new Directory(name, size);
//
//
//	}
//	/**
//	 * Returns the current directory.
//	 * @return the current directory
//	 */
//	public Directory getCurrentDirectory(){
//		return current;
//	}
//
//	/**
//	 * Goes to a subdirectory.
//	 * @param name the name of the subdirectory to go to
//	 */
//	public void goToSubdirectory(String name){
//		Directory dir = current.getSubdirectory(name);
//
//
//	}
//
//	/**
//	 * Goes (returns) to a parent directory.
//	 */
//	public void goToParentDirectory(){
//
//	}
//
//	/**
//	 * Creates a new DiskFile for this DiskUnit.
//	 * @param diskName the name of this DiskUnit
//	 * @param name the name of the file
//	 * @param size the size of the file
//	 * @param file the external file
//	 */
//	public void createNewFile(String diskName, String name, int size, File file){
//		DiskFile df = new DiskFile(size);
//		try {
//			df.loadFile(diskName, name, file);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		current.createNewFile(name, df);
//
//	}
//
//	/**
//	 * Returns a DiskFile.
//	 * @param name the name of the file
//	 * @return the DiskFile corresponding to the name given
//	 * @throws FileNotFoundException if the file does not exist
//	 */
//	public DiskFile accessFile(String name) throws FileNotFoundException{
//		return current.getDiskFile(name);
//
//	}
//
//	/**
//	 * Deletes the file from this DiskUnit.
//	 * @param name the name of the file to remove
//	 * @throws FileNotFoundException if the file does not exist
//	 */
//	public void removeFile(String name) throws FileNotFoundException{
//		current.removeFile(name);
//	}
//
//	/**
//	 * Removes a directory from this DiskUnit.
//	 * @param name the name of the directory
//	 * @throws DirectoryNotFoundException if the directory does not exist
//	 */
//	public void removeDirectory(String name) throws DirectoryNotFoundException{
//		current.removeDirectory(name);
//
//	}
//

	
	
}
