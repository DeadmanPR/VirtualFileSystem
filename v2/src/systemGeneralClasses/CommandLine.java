package systemGeneralClasses;

/**
 * author: Pedro I. Rivera Vega
 */
import java.util.ArrayList;
import java.util.StringTokenizer; 

/**
 * Whenever a command is expected, the whole input line is read. From the 
 * input read, a preliminary processing of that input is done to create
 * an object of this type. Such object is later used to continue the
 * with the validation and further execution of the command. Part of this
 * preliminary analysis is to match the input with a valid command in the
 * current state of the system. 
 *  
 * @author pedroirivera-vega
 *
 */
public class CommandLine { 
	private ArrayList<String> tokensList; 	
	private SystemCommand sc; 

	/**
	 * Creates a command line from the given line.
	 * @param line the line to create a command from
	 */
	public CommandLine(String line) { 
		tokensList = new ArrayList<String>(); 
		StringTokenizer st = new StringTokenizer(line); 
		while 	(st.hasMoreTokens()) 
			tokensList.add(st.nextToken()); 
		
		sc = null; 
	} 
		

	/**
	 * Returns the token at the give index.
	 * @param index the index to look for
	 * @return the token at the given index
	 */
	public String getToken(int index) { 
		//  index is assumed to be >=1 and <= tokenList.size()	
		return tokensList.get(index - 1); 
	} 
	
	/**
	 * Returns the number of tokens.
	 * @return the number of tokens
	 */
	public int getNumberOfTokens() { 
		return tokensList.size(); 
	} 
	

	public String toString() { 
		String rs = ""; 
		for (String token : tokensList)
			rs = rs + token + " "; 
		return rs; 
	}
	
	/**
	 * Returns a string of the operands.
	 * @return a string containing all of the operands
	 */
	public String stringOfOperands() { 
		String rs = ""; 
		for (int i=1; i<tokensList.size(); i++)
			rs = rs + tokensList.get(i) + " "; 
		return rs; 
	}

	/**
	 * Returns this command line's SystemCommand.
	 * @return this CommandLine's SystemCommand
	 */
	public SystemCommand getSc() {
		return sc;
	}

	/**
	 * Sets the SystemCommand for this CommandLine.
	 * @param sc the SystemCommand to set
	 */
	public void setSc(SystemCommand sc) {
		this.sc = sc;
	}
}
