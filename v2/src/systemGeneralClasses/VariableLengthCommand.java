package systemGeneralClasses;

import java.util.ArrayList;

/**
 * This class represents those commands whose length
 * varies, either in the number of operands, or because
 * some operands may consist of a list of items
 * separated by spaces or commas, etc. An object of
 * this type is created after the input given by
 * user has been analyzed and determined as satisfying
 * the grammar rule of a particular system command 
 * whose name matches the name in input. 
 * That particular system command has been added
 * to the command line, and can be accessed through
 * method "getSc"...
 * @author pirvos
 *
 */
public class VariableLengthCommand extends Command  
{
	private ArrayList<ArrayList<String>> itemsList; 
	// list of items extracted from the command line
	
	/**
	 * Creates a new VariableLengthCommand.
	 * @param itemsList the list of items that are coming 
	 * from the input. Used here to initialize this object. 
	 */
	public VariableLengthCommand(ArrayList<ArrayList<String>> itemsList) {
		this.itemsList = itemsList; 
	}
	
	/**
	 * Returns the items for the operand at the given index.
	 * @param index
	 * @return the list of items in that operand
	 */
	public ArrayList<String> getItemsForOperand(int index) {
		return itemsList.get(index - 1); 
	}
	
	/**
	 * Returns the number of operands.
	 */
	public int getNumberOfOperands() { 
		return itemsList.size(); 
	}
}
