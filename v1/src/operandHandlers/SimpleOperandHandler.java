package operandHandlers;

import java.security.InvalidParameterException;

/**
import dataTypesHandlers.DateComparator;
import dataTypesHandlers.DateValidator;
import dataTypesHandlers.IntComparator;
import dataTypesHandlers.IntValidator;
import dataTypesHandlers.MoneyComparator;
import dataTypesHandlers.MoneyValidator;
**/


public class SimpleOperandHandler {
	
	/**
	 * Validates of a token complies with the grammar
	 * @param tType The type of token to be matched with (the grammar)
	 * @param token The token content to be analyzed. 
	 * @return true if valid; false, otherwise.
	 */
	public static boolean isValidToken(String tType, String token) { 
		// current simple token types are: name, int, 
		// more need to be added...
		if (tType.equals("disk_name") || tType.equals("dir_name") || tType.equals("file_name") || tType.equals("ext_file_name"))
			return isValidName(token); 
		else if (tType.equals("int") || tType.equals("nblocks") || tType.equals("bsize"))
			return isValidInt(token); 
		else 
			return false; 
		
		
	}

	/**
	 * Checks if the given operand is a valid name.
	 * @param operand the operand to check
	 * @return true if the operand is a valid name, false otherwise
	 */
	private static boolean isValidName(String operand) { 
		if (operand.length() == 0) 
			return false; 
		else 
			return true;
	}

	/**
	 * Checks if the given operand is a valid integer.
	 * @param operand the operand to check
	 * @return true if the operand is a valid integer, false otherwise
	 */
	private static boolean isValidInt(String operand) { 
		try { 
			Integer.parseInt(operand); 
			return true; 
		} 
		catch(Exception e) { 
			return false; 
		}		
	}

}
