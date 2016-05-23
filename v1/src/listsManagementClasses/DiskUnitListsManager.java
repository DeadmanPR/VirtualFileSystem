package listsManagementClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Scanner;

import disk.DiskUnit;
import disk.VirtualDiskBlock;
import exceptions.NonExistingDiskException;

/**
 * This class represents a list of DiskUnits used by the system.
 * @author pedroirivera-vega , Jose Antonio Rodrugez Rivera
 *
 */
public class DiskUnitListsManager {
	private ArrayList<NamedDiskUnit> disks; 
	private static File nameOfDiskUnits = new File("src" + File.separator + "DiskUnits" + File.separator + "DiskNames.txt");

	/**
	 * Creates a new manager.
	 */
	public DiskUnitListsManager() { 
		disks = new ArrayList<>(); 
	}

	/**
	 * Find the index of the position where a disk with a given name is. 
	 * If no such disk it returns -1; otherwise, it returns the index of
	 * the position where it is located in the list of disks.
	 * @param name the name to look for
	 * @return the index of the DiskUnit
	 */
	public int getListIndex(String name) { 
		for (int i=0; i<disks.size(); i++) 
			if (disks.get(i).getName().equals(name)) 
				return i; 
		return -1; 
	}


	/**
	 * Creates and adds a new DiskUnit to the list of disks.
	 * @param dName the name of the disk
	 */
	public void createNewDisk(String dName) {
		disks.add(new NamedDiskUnit(dName)); 
		saveInfoToFile();
	}

	/**
	 * Creates and adds a new DiskUnit to the list of disks with the given parameters.
	 * @param dName the name of the disk
	 * @param blockSize the size of each block in the disk
	 * @param capacity the number of blocks in the disk
	 */
	public void createNewDisk(String dName, int blockSize, int capacity) {
		disks.add(new NamedDiskUnit(dName, blockSize, capacity)); 
		saveInfoToFile();
	}

	//	public void addINode(int listIndex, VirtualDiskBlock vdb) 
	//			throws IndexOutOfBoundsException //TODO
	//	{ 
	//		disks.get(listIndex).add(value); 		
	//	}


	//	public void addFile(int listIndex, VirtualDiskBlock vdb) 
	//			throws IndexOutOfBoundsException //TODO
	//	{ 
	//		disks.get(listIndex).add(value); 		
	//	}
	
	/**
	 * Removes (deletes) a DiskUnit from the list of disks.
	 * @param index the index of the DiskUnit to remove
	 * @return the NamedDiskUnit that was removed
	 * @throws IndexOutOfBoundsException if the index is not valid
	 */
	public NamedDiskUnit removeDisk(int index) 
			throws IndexOutOfBoundsException 
	{
		if(index < 0 || index >= disks.size())
			throw new IndexOutOfBoundsException("Invalid index: " + index);

		NamedDiskUnit dtr = disks.remove(index);
		dtr.getDiskUnit().shutdown();

		try {
			dtr.delete();
		} catch (NonExistingDiskException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		saveInfoToFile();
		return dtr;
	}
	
	/**
	 * Formats a disk from the list of disks to the given parameters.
	 * @param index the index of the disk to be formatted
	 * @param numberOfBlocks the number of blocks that the disk will have
	 * @param blockSize he size of each block that the disk will have
	 * @throws NonExistingDiskException if the disk does not exist
	 * @throws InvalidParameterException if the parameters are not powers of 2
	 */
	public void formatDisk(int index, int numberOfBlocks, int blockSize) throws NonExistingDiskException, InvalidParameterException{
				String name = disks.get(index).getName();
				disks.get(index).getDiskUnit().shutdown();
				disks.get(index).formatDisk(numberOfBlocks, blockSize);
				NamedDiskUnit ndu = new NamedDiskUnit(name);
				disks.set(index, ndu);
	}


	/**
	 * Returns the block size of a disk from the list of disks.
	 * @param index the index of the disk
	 * @return the block size of the disk
	 * @throws IndexOutOfBoundsException if the index is not valid
	 */
	public int getBlockSize(int index) throws IndexOutOfBoundsException
	{
		if(index < 0 || index >= disks.size())
			throw new IndexOutOfBoundsException("Invalid index: " + index);

		return disks.get(index).getBlockSize(); 				
	}

	/**
	 * Returns the number of blocks a disk from the list has.
	 * @param index the index of the disk
	 * @return the number of blocks the disk has
	 * @throws IndexOutOfBoundsException if the index is not valid
	 */
	public int getNumberOfBlocks(int index) throws IndexOutOfBoundsException{
		if(index < 0 || index >= disks.size())
			throw new IndexOutOfBoundsException("Invalid index: " + index);

		return disks.get(index).getNumberOfBlocks();
	}

	/**
	 * Returns the name of a disk from the list.
	 * @param index the index of the disk
	 * @return the name of said disk
	 * @throws IndexOutOfBoundsException if the index is not valid
	 */
	public String getName(int index) throws IndexOutOfBoundsException{
		if(index < 0 || index >= disks.size())
			throw new IndexOutOfBoundsException("Invalid index: " + index);
		return disks.get(index).getName(); 
	}
	
	/**
	 * Returns an instance of a NamedDiskUnit representing a particular disk from the list.
	 * @param index the index of the disk
	 * @return the NamedDiskUnit representing said disk
	 * @throws IndexOutOfBoundsException if the index is not valid
	 */
	public NamedDiskUnit getNamedDiskUnit(int index) throws IndexOutOfBoundsException{
		if(index < 0 || index >= disks.size())
			throw new IndexOutOfBoundsException("Invalid index: " + index);
		return disks.get(index);
	}

	/**
	 * Returns the number of disks in the list.
	 * @return the number of disks
	 */
	public int getNumberOfDisks() { 
		return disks.size(); 
	}

	/**
	 * Checks if a given name is associated with any disk from the list.
	 * @param name the name to look for
	 * @return true if the name exists, false otherwise
	 */
	public boolean nameExists(String name) { 
		int index = getListIndex(name); 
		return index != -1; 
	}

	/**
	 * Saves the disk names to a file.
	 */
	public void saveInfoToFile(){

		try {
			PrintWriter pw = new PrintWriter(nameOfDiskUnits);
			pw.println(getNumberOfDisks());

			for(int i = 0 ; i < disks.size() ; i++)
				pw.println(disks.get(i).getName());                
			
			pw.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Loads the disk names from a file.
	 */
	public void loadInfoFromFile(){
		if(nameOfDiskUnits.exists()){
			Scanner sc;
			try {
				sc = new Scanner(nameOfDiskUnits);
				int numberOfDisks = sc.nextInt();
				sc.nextLine();
				for(int i = 0; i < numberOfDisks ; i++)
					createNewDisk(sc.nextLine());

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns if a disk from the list in mounted or not.
	 * @param index the index of the disk
	 * @return true if the disk is mounted, false otherwise
	 * @throws IndexOutOfBoundsException if the indx is not valid
	 */
	public boolean isMounted(int index) throws IndexOutOfBoundsException{
		if(index < 0 || index >= disks.size())
			throw new IndexOutOfBoundsException("Invalid index: " + index);
		
		return disks.get(index).isMounted();
	}
}
