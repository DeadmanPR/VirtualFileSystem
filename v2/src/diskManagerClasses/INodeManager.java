package diskManagerClasses;

import java.util.ArrayList;
import java.util.Stack;

import disk.DiskUnit;
import disk.DiskUtils;
import disk.INode;
import disk.VirtualDiskBlock;
import exceptions.FullDiskException;
import exceptions.INodeIndexOutOfBoundsException;

/**
 * Manages the INodes of a DiskUnit.
 * @author Jose Antonio Rodriguez Rivera
 *
 */
public class INodeManager {
	private VirtualDiskBlock block0;
	private DiskUnit disk;
	public static final int INODESIZE = 9;

	private ArrayList<INode> iNodes;
	private Stack<INode> freeINodes;
	private int ffINodeIndex;

	/**
	 * Constructs an INode manager for the given DiskUnit.
	 * @param dUnit the DiskUnit
	 */
	public INodeManager(DiskUnit dUnit){
		disk = dUnit;
		block0 = new VirtualDiskBlock(disk.getBlockSize(), 0);
		disk.read(0, block0);
		iNodes = new ArrayList<>();
	}

	/**
	 * Adds an INode to this manager.
	 * @param e the INode to add
	 */
	public void addINode(INode e){
		iNodes.add(e);
	}

	/**
	 * Returns an INode whose index is the given integer.
	 * @param index the index of the INode
	 * @return
	 */
	public INode getINode(int index) throws INodeIndexOutOfBoundsException{
		if(index < 0 || index >= iNodes.size())
			throw new INodeIndexOutOfBoundsException("INode with index " + index + " does not exist!");
		return iNodes.get(index);
	}

	/**
	 * Deletes an INode from this manager.
	 * @param e the INode to delete
	 */
	public void deleteINode(INode e){
		for(int i = 0 ; i < iNodes.size() ; i ++)
			if(e.equals(iNodes.get(i)))
				iNodes.remove(i); 
	}

	/**
	 * Returns the index of an given INode.
	 * @param e the INode to search for
	 * @return the index of the give INode
	 */
	public int getINodeIndex(INode e){
		for(int i = 0 ; i < iNodes.size() ; i ++)
			if(e.equals(iNodes.get(i)))
				return i;

		return -1;
	}

	/**
	 * Clears all the INodes from this manager.
	 */
	public void clearINodes(){
		iNodes.clear();
	}

	/**
	 * Loads the free INodes to this manager.
	 */
	public void loadFreeINodes(){
		freeINodes = new Stack<>();
		int ffINode = 0 ;
		for(int i = iNodes.size() - 1; i >= 0 ; i--)
			if(iNodes.get(i).getSize() == 0){
				freeINodes.push(iNodes.get(i));
				ffINode = i;
			}
		DiskUtils.copyIntToBlock(block0, DiskUnit.FFINODE_OFFSET, ffINode);
		DiskUtils.copyIntToBlock(block0, DiskUnit.NUMBEROFINODES_OFFSET, iNodes.size());

		ffINodeIndex = ffINode;

		//Writes FirstFreeINodeIndex and NumberOfINodes to block 0
		disk.write(0, block0);
	}

	/**
	 * Marks an INode as a free INode.
	 * @param index the index of the INode to mark as free
	 */
	public void setFreeINode(int index) throws INodeIndexOutOfBoundsException{
		if(index < 0 || index >= iNodes.size())
			throw new INodeIndexOutOfBoundsException("INode with index " + index + " does not exist!");

		if(iNodes.get(index).getSize() != 0){
			iNodes.get(index).setFirstBlock(ffINodeIndex);
			iNodes.get(index).setSize(0);
			iNodes.get(index).setType((byte)0);

			ffINodeIndex = index;

			freeINodes.push(iNodes.get(index));
		}
	}

	/**
	 * Returns a free INode.
	 * @return a free INode
	 * @throws FullDiskException if the DiskUnit does not have a free INode
	 */
	public INode getFreeINode() throws FullDiskException{
		if(freeINodes.isEmpty())
			throw new FullDiskException("The disk is full!");
		INode etr =  freeINodes.pop();
		ffINodeIndex = etr.getFirstBlock();
		etr.setSize(disk.getBlockSize()-4);
		return etr;
	}

	/**
	 * Saves the corresponding data of the INodes to the DiskUnit.
	 */
	public void saveINodesToDisk(){
		int[] iNodeInfo = new int[iNodes.size() * 2];
		byte[] iNodeType = new byte[iNodes.size()];

		for(int i = 0; i < iNodeInfo.length; i+=2){
			iNodeInfo[i] = iNodes.get(i/2).getFirstBlock();
			iNodeInfo[i+1] = iNodes.get(i/2).getSize();
			iNodeType[i/2] = (byte)iNodes.get(i/2).getType();
		}

		int temp = 0;
		int typeTemp = 0;
		VirtualDiskBlock vdb = new VirtualDiskBlock(iNodeInfo.length*4 + iNodeType.length, 0);
		for(int i = 1; i <=DiskUtils.getNumberOfINodeBlocks(disk) ; i++){


			for(int j = 0; j < disk.getBlockSize() && temp < iNodeInfo.length ; j+=4){
				DiskUtils.copyIntToBlock(vdb, j, iNodeInfo[temp]);

				if(temp % 2 == 1){
					vdb.setElement(j+4, iNodeType[typeTemp]);
					j++;
					typeTemp++;
				}
				temp++;
			}


		}
		disk.write(1, vdb);
	}

}
