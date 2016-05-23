package listsManagementClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;

import disk.DiskUnit;
import exceptions.ExistingDiskException;
import exceptions.NonExistingDiskException;

/**
 * Represents a DiskUnit with a name.
 * @author Jose Antonio Rodriguez Rivera
 *
 */
public class NamedDiskUnit{

	private String name; 
	private DiskUnit disk;
	private boolean mounted = false;

	/**
	 * Creates a NamedDiskUnit with the given name.
	 * @param name the name of the disk
	 */
	public NamedDiskUnit(String name) {
		try{
		DiskUnit.createDiskUnit(name);
		disk = DiskUnit.mount(name);
		}catch(ExistingDiskException e){
			disk = DiskUnit.mount(name);
		}
		
		this.name = name; 
	}
	
	/**
	 * Creates a NamedDIskUnit with the given parameters.
	 * @param name the name of the disk
	 * @param blockSize the size of each block of the disk
	 * @param capacity the number of blocks the disk has
	 */
	public NamedDiskUnit(String name, int blockSize, int capacity) {
		try{
		DiskUnit.createDiskUnit(name, capacity, blockSize);
		disk = DiskUnit.mount(name);
		}catch(ExistingDiskException e){
			disk = DiskUnit.mount(name);
		}
		
		this.name = name; 
	}
	
	/**
	 * Returns the name of the disk.
	 * @return the name of the disk
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the DiskUnit associated with this NamedDiskUnit.
	 * @return the DiskUnit associated with this NamedDiskUnit
	 */
	public DiskUnit getDiskUnit(){
		return disk;
	}
	
	/**
	 * Returns the number of blocks of this disk.
	 * @return the number of blocks of this disk
	 */
	public int getNumberOfBlocks(){
		return disk.getCapacity();
	}
	
	/**
	 * Returns the size of each block in this file.
	 * @return the size of each block in this file
	 */
	public int getBlockSize(){
		return disk.getBlockSize();
	}
	
	/**
	 * Sets the disk as currently mounted
	 * @param b mounting status
	 */
	public void setMounted(boolean b){
		mounted = b;
	}
	
	/**
	 * Returns if the disk is currently mounted.
	 * @return true if the disk is mounted, false otherwise
	 */
	public boolean isMounted(){
		return mounted;
	}
	
	/**
	 * Formats the disk with the given parameters.
	 * @param numberOfBlocks the number of blocks the disk will have
	 * @param blockSize the size of each block the disk will have
	 * @throws NonExistingDiskException if the disk does not exist
	 * @throws InvalidParameterException if the parameters are not powers of 2
	 */
	public void formatDisk(int numberOfBlocks, int blockSize) throws NonExistingDiskException, InvalidParameterException{
		disk.format(name, numberOfBlocks, blockSize);
	}
	
	/**
	 * Shuts down the disk associated with this NamedDiskUnit.
	 */
	public void shutdown(){
		disk.shutdown();
	}
	
//	/**
//	 * Loads a file to the disk.
//	 * @param name the name of the file
//	 * @param size the size of the file
//	 * @param f the file to load
//	 */
//	public void loadFile(String name, int size, File f){
//		disk.createNewFile(this.name, name, size, f);
//	}
//	
	/**
	 * Deletes this disk.
	 */
	public void delete(){
		disk.delete(name);
	}
}
