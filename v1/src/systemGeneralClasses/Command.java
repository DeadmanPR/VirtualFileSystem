package systemGeneralClasses;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Represents a command.
 * @author Jose Antonio Rodriguez Rivera
 *
 */
public abstract class Command {
	protected ArrayList<String> tokensList; 	

	/**
	 * 
	 * @return
	 */
	
	public Command() {}
	
	/**
	 * Creates a command from the input.
	 * @param input
	 */
	public Command(String input) { 
		tokensList = new ArrayList<String>(); 
		StringTokenizer st = new StringTokenizer(input); 
		while (st.hasMoreTokens())
			tokensList.add(st.nextToken()); 

	}
	
	/**
	 * Returns the number of operands in this command.
	 * @return the number of operands
	 */
	public int getNumberOfOperands() { 
		return tokensList.size() - 1; 
	}
	
	/**
	 * Returns the name of this command.
	 * @return the name of this command
	 */
	public String getName() { 
		return tokensList.get(0); 
	}
	
	public String toString() { 
		String rs = ""; 
		for (int i=0; i < tokensList.size(); i++)
			rs = rs + tokensList.get(i)+ " "; 
		return rs; 
	}

}
