package diskManagerClasses;

import java.sql.Savepoint;

import disk.DiskUnit;
import disk.DiskUtils;
import disk.VirtualDiskBlock;
import exceptions.FullDiskException;
import exceptions.InvalidBlockNumberException;

/**
 * Manages the free blocks in a DiskUnit.
 * @author Jose Antonio Rodriguez Rivera
 *
 */
public class FreeBlockManager {
	private DiskUnit disk;
	private VirtualDiskBlock currentFBArray, block0;
	private int fbRootBlockNum, amountOfNumbersPerBlock;

	/**
	 * Constructs a free block manager for the given DiskUnit.
	 * @param dUnit the DiskUnit
	 */
	public FreeBlockManager(DiskUnit dUnit){
		disk = dUnit;
		currentFBArray = new VirtualDiskBlock(disk.getBlockSize(), 0);
		block0 = new VirtualDiskBlock(disk.getBlockSize(), 0);
		amountOfNumbersPerBlock = (disk.getBlockSize())/DiskUtils.INTSIZE;
		disk.read(0, block0);

		fbRootBlockNum = DiskUtils.getIntFromBlock(block0, DiskUnit.FBROOT_OFFSET);
		disk.read(fbRootBlockNum, currentFBArray);
	}

	/**
	 * Reads and sets the data needed for this manager.
	 */
	public void setInitialFBArray(){
		DiskUtils.copyIntToBlock(block0, DiskUnit.FBROOT_OFFSET, DiskUtils.getNumberOfINodeBlocks(disk)+1);


		disk.write(0, block0);
		fbRootBlockNum = DiskUtils.getIntFromBlock(block0, DiskUnit.FBROOT_OFFSET);



		int lastFreeBlock = disk.getCapacity()-1;
		int numberOfBlocksForFBArray = DiskUtils.getNumberOfBlocksForFBArray(disk);
		int blockNumToInsert = disk.getCapacity()-amountOfNumbersPerBlock;

		for(int i = 0; i < numberOfBlocksForFBArray-1 ; i++){

			VirtualDiskBlock vdb = new VirtualDiskBlock(disk.getBlockSize(), (blockNumToInsert + amountOfNumbersPerBlock) % disk.getCapacity());
			for(int j = amountOfNumbersPerBlock*4 - numberOfBlocksForFBArray; j >= 0; j-=4){
				DiskUtils.copyIntToBlock(vdb, j, lastFreeBlock);
				lastFreeBlock--;
			}


			disk.write(blockNumToInsert, vdb);
			if(lastFreeBlock <= numberOfBlocksForFBArray)
				blockNumToInsert = DiskUtils.getNumberOfINodeBlocks(disk)+1;
			else
				blockNumToInsert-= numberOfBlocksForFBArray;
			lastFreeBlock--;
		}

		//Last Block
		VirtualDiskBlock vdb = new VirtualDiskBlock(disk.getBlockSize(), 0);
		boolean done = false;
		int possibleIndex = 0;
		for(int j = 0 ; j < numberOfBlocksForFBArray*4 && !done; j+=4){
			DiskUtils.copyIntToBlock(vdb, j, (j/4) + DiskUtils.getNumberOfINodeBlocks(disk)+2);

			if((j/4) + DiskUtils.getNumberOfINodeBlocks(disk)+2 == lastFreeBlock){
				done = true;
				possibleIndex = j/4;
				DiskUtils.copyNextBNToBlock(vdb, lastFreeBlock+1);;
			}	

		}
		if(fbRootBlockNum == blockNumToInsert){
			DiskUtils.copyIntToBlock(block0, DiskUnit.FBINDEX_OFFSET, possibleIndex);
			disk.write(0, block0);
			disk.read(0, block0);

			fbRootBlockNum = DiskUtils.getIntFromBlock(block0, DiskUnit.FBROOT_OFFSET);
			disk.read(fbRootBlockNum, currentFBArray);
		}
		disk.write(blockNumToInsert, vdb);
		disk.read(fbRootBlockNum, currentFBArray);
	}

	/**
	 * Returns the number of a free block (if any).
	 * @return the number of the free block
	 * @throws FullDiskException if the DiskUnit does not have any free blocks
	 */
	public int getFreeBlock() throws FullDiskException{
		disk.read(fbRootBlockNum, currentFBArray);
		int currentIndex = DiskUtils.getIntFromBlock(block0, DiskUnit.FBINDEX_OFFSET);
		int vtr;

		if(currentIndex == -1){
			vtr = DiskUtils.getIntFromBlock(block0, DiskUnit.FBROOT_OFFSET);
			int nextBlock = currentFBArray.getNextBlockNumber();

			if(nextBlock == 0)
				throw new FullDiskException("The disk is full!");

			DiskUtils.copyIntToBlock(block0, DiskUnit.FBROOT_OFFSET, nextBlock);
			DiskUtils.copyIntToBlock(block0, DiskUnit.FBINDEX_OFFSET, amountOfNumbersPerBlock-2);

			disk.read(nextBlock, currentFBArray);
			disk.write(0,  block0);
			disk.read(0, block0);

			fbRootBlockNum = DiskUtils.getIntFromBlock(block0, DiskUnit.FBROOT_OFFSET);

		}
		else{
			vtr = DiskUtils.getIntFromBlock(currentFBArray, currentIndex*4);
			currentIndex--;

			DiskUtils.copyIntToBlock(block0, DiskUnit.FBINDEX_OFFSET, currentIndex);

			disk.write(0, block0);
		}

		return vtr;

	}

	/**
	 * Adds a free block to this manager. (Used when freeing a block).
	 * @param blockNum  the number of the block to mark as free
	 */
	public void registerFreeBlock(int blockNum) throws InvalidBlockNumberException{
		disk.read(fbRootBlockNum, currentFBArray);
		if(blockNum < 0 || blockNum >= disk.getCapacity())
			throw new InvalidBlockNumberException();

		int currentIndex = DiskUtils.getIntFromBlock(block0, DiskUnit.FBINDEX_OFFSET);

		if(currentIndex < amountOfNumbersPerBlock-2){
			currentIndex++;
			DiskUtils.copyIntToBlock(currentFBArray, currentIndex*4, blockNum); 
			DiskUtils.copyIntToBlock(block0, DiskUnit.FBINDEX_OFFSET, currentIndex);

			disk.write(0, block0);
			disk.write(fbRootBlockNum, currentFBArray);
			disk.read(fbRootBlockNum, currentFBArray);

		}
		else{
			VirtualDiskBlock newRoot = new VirtualDiskBlock(disk.getBlockSize(), fbRootBlockNum);
			disk.write(blockNum, newRoot);
			fbRootBlockNum = blockNum;

			DiskUtils.copyIntToBlock(block0, DiskUnit.FBROOT_OFFSET, fbRootBlockNum);
			DiskUtils.copyIntToBlock(block0, DiskUnit.FBINDEX_OFFSET, -1);

			disk.write(0, block0);
			disk.read(0, block0);
			disk.read(blockNum, currentFBArray);

		}
	}

	/**
	 * Shows the free blocks structure.
	 */
	public String showFreeBlocks(){
		String s = "";
		VirtualDiskBlock vdb = currentFBArray;
		int blockNum = fbRootBlockNum;
		int maxIndex = DiskUtils.getIntFromBlock(block0, DiskUnit.FBINDEX_OFFSET);
		s = s + "\tFree Blocks Structure: \n";

		boolean done = false;
		while(blockNum != 0){
			s = s + "\t\tBlock " + blockNum + ": ";
			for(int i = 0 ; i < (amountOfNumbersPerBlock*4)-4 && !done ; i+=4){
				if(blockNum == fbRootBlockNum && (i/4) > maxIndex)
					s = s + "-" + " ";
				else
					s = s + DiskUtils.getIntFromBlock(vdb, i) + " ";

				if(DiskUtils.getIntFromBlock(vdb, i) == disk.getCapacity()-1)
					done = true;
			}

			blockNum = DiskUtils.getNextBNFromBlock(vdb);
			s = s + blockNum + "\n";

			disk.read(blockNum, vdb);
		}

		return s;
	}
}
