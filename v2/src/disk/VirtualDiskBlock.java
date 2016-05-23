package disk;

import java.nio.ByteBuffer;

/**
 * Represents a disk block. Has the ability to store a fixed number of bytes.
 * @author Jose Antonio Rodriguez Rivera
 *
 */
public class VirtualDiskBlock {
	private int capacity;
	private final static int DEFAULT_BLOCK_SIZE = 256;
	private byte[] byteArray;
	
	/**
	 * Creates a new VirtualDiskBlock with the default block size and the given next block.
	 * @param nextBlock the number of the next block
	 */
	public VirtualDiskBlock(int nextBlock){
		//Sets the capacity and creates the array of bytes with the default length.
		capacity = DEFAULT_BLOCK_SIZE;
		byteArray = new byte[DEFAULT_BLOCK_SIZE];
		
		//Fills the array with 0s
		for(int i = 0; i < DEFAULT_BLOCK_SIZE-4; i++)
			byteArray[i] = (byte)0;
		
		DiskUtils.copyNextBNToBlock(this, nextBlock);
	}
	
	/**
	 * Creates a new VirtualDiskBlock with the capacity given, and the next block given.
	 * @param blockCapacity the capacity of the block
	 * @param nextBlock the number of the next block
	 */
	public VirtualDiskBlock(int blockCapacity , int nextBlock){
		//Sets the capacity as given and creates the byte array.
		capacity = blockCapacity;
		byteArray = new byte[blockCapacity];
		
		//Fills the array with 0s
		for(int i = 0; i < blockCapacity; i++)
			byteArray[i] = (byte)0;
		
		DiskUtils.copyNextBNToBlock(this, nextBlock);
	}
	
	/**
	 * Returns the number of bytes that can be stored in this VirtualDiskBlock.
	 * @return the number of bytes (capacity)
	 */
	public int getCapacity(){
		return capacity;
	}
	
	/**
	 * Sets an element of this VirtualDiskBlock.
	 * @param index the position to set the element at
	 * @param nuevo the element to store
	 */
	public void setElement(int index, byte nuevo){
		byteArray[index] = nuevo;
		
	}
	
	/**
	 * Returns the element (byte) at the given position.
	 * @param index the position of the element
	 * @return the element at position index
	 */
	public byte getElement(int index){
		return byteArray[index];
	}
	
	/**
	 * Returns the contents of this VirtualDiskBlock as an array of bytes.
	 * @return the contents of the disk block
	 */
	public byte[] getAll(){
		return byteArray;
	}
	
	/**
	 * Returns the block number of the next block.
	 * @return the block number of the next block
	 */
	public int getNextBlockNumber(){
		byte[] nbn = {byteArray[capacity-4], byteArray[capacity-3], byteArray[capacity-2], byteArray[capacity-1]};
		return ByteBuffer.wrap(nbn).getInt();
	}
	
}
