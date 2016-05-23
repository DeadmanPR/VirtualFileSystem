package operandHandlers;

import systemGeneralClasses.OperandAnalyzer;


public class OperandValidatorUtils {
	
	/**
	 * Checks if the given operand is a valid name.
	 * @param operand the operand to check
	 * @return true if it's a valid name, false otherwise
	 */
	public static boolean isValidName(String operand) { 
		if (operand.length() == 0) 
			return false; 
		
		// operand is not empty string 
		boolean isName = (Character.isLetter(operand.charAt(0)));
		int cp=1; 
		while (cp < operand.length() && isName) { 
			char c = operand.charAt(cp); 
			if (!(Character.isDigit(c) || Character.isLetter(c)))
				isName = false; 
			cp++; 
		}		
		return isName;

	}

	/**
	 * Checks if the operand is a valid integer.
	 * @param operand the operand to check
	 * @return true if the operand is a valid integer, false otherwise
	 */
	public static boolean isValidInt(String operand) { 
		try { 
			Integer.parseInt(operand); 
			return true; 
		} 
		catch(Exception e) { 
			return false; 
		}		
	}
	
	/**
	 * Returns an analyzer depending on the type of operand.
	 * @param op the type of operand
	 * @return the analyzer for the operand, null if not valid
	 */
	public static OperandAnalyzer getAnalyzerFor(String op) {
		if (op.equals("int") || op.equals("nblocks") || op.equals("bsize"))
			return IntOperandAnalyzer.getInstance(); 
		else if (op.equals("disk_name") || op.equals("dir_name") ||op.equals("file_name" ) || op.equals("ext_file_name"))
			return NameOperandAnalyzer.getInstance(); 
		
		
		return null;   
	}


}
