package disk;

/**
 * Object that represents an INode.
 * @author Jose Antonio Rodriguez Rivera
 *
 */
public class INode {
	private int type, size, firstBlock;
	private long positionInDisk;

	/**
	 * Creates an INode and saves its location on the disk.
	 * @param positionInDisk location on the disk
	 */
	public INode(long positionInDisk){
		this.positionInDisk = positionInDisk;
	}


	/**
	 * Creates an INode with some properties.
	 * @param type the type of file (0 for a data file, 1 for a directory)
	 * @param size the size of the file pointed at by the INode
	 * @param firstBlock the first block where the file/directory is located
	 * @param positionInDisk the location on the disk of this INode
	 */
	public INode(int type, int size, int firstBlock, long positionInDisk){
		this.type = type;
		this.size = size;
		this.firstBlock = firstBlock;
		this.positionInDisk = positionInDisk;
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
	 * Returns the location on the disk of this INode.
	 * @return the location on the disk
	 */
	public long getPositionInDisk() {
		return positionInDisk;
	}

}
