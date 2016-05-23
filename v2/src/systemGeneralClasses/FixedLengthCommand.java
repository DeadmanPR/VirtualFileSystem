package systemGeneralClasses;

import java.util.ArrayList;
import java.util.StringTokenizer;


/**
 * Represents a candidate command to be executed. The command
 * relates to a particular SystemCommand and to a CommandProcessor that 
 * dictates how the command is to be executed in the particular system.
 * @author Pedro I. Rivera Vega
 *
 */
public class FixedLengthCommand extends Command { 
	
    /**
     * Constructor of a Command object. 
     * @param line the attempted command with its operands.
     */
	public FixedLengthCommand(CommandLine line) { 
		super(line.toString()); 
	} 
			
	/**
	 * Returns the operand at the given index.
	 * @param index
	 * @return the operand at said index
	 */
	public String getOperand(int index) { 
		return tokensList.get(index); 
	}


}

