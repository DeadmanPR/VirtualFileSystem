package disk;

/**
 * Object that represents an INode.
 * @author Jose Antonio Rodriguez Rivera
 *
 */
public class INode {
	private int size, firstBlock;
	private byte type;


	/**
	 * Creates an INode.
	 */
	public INode(){
	
	}


	/**
	 * Creates an INode with some properties.
	 * @param type the type of file (0 for a data file, 1 for a directory)
	 * @param size the size of the file pointed at by the INode
	 * @param firstBlock the first block where the file/directory is located
	 * @param positionInDisk the location on the disk of this INode
	 */
	public INode(int firstBlock, int size, byte type){
		this.type = type;
		this.size = size;
		this.firstBlock = firstBlock;
	}


	/**
	 * Returns the type of the INode.
	 * @return 0 if pointing at a data file, 1 if pointing at a directory
	 */
	public int getType() {
		return type;
	}

	/**
	 * Returns the size of the file pointed at by the INode.
	 * @return the size of the file/directory
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Returns the number of the first block of the file/directory
	 * @return the number of the first block
	 */
	public int getFirstBlock() {
		return firstBlock;
	}
	
	/**
	 * Sets the first block of this INode.
	 * @param fb the block number to point to
	 */
	public void setFirstBlock(int fb){
		firstBlock = fb;
	}
	
	/**
	 * Sets the size of the file(or directory) represented by this INode.
	 * @param size the size of the target block
	 */
	public void setSize(int size){
		this.size = size;
	}
	
	/**
	 * Sets the type of the target of the INode.
	 * @param b the type of the target of the INode
	 */
	public void setType(byte b){
		type = b;
	}
	
	public String toString(){
		return "INode: FirstBlock: " + firstBlock + " | Size: " + size + " | Type: " + type;
	}
}
