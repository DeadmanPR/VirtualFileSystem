package systemGeneralClasses;

import operandHandlers.SimpleOperandHandler;

/**
 * Validates the FixedLengthCommand.
 * @author Jose Antonio Rodriguez Rivera
 *
 */
public class FixedLengthCommandValidator implements CommandValidator {

	public boolean validate(SystemCommand mSCommand, CommandLine c, ErrMsg errMsg) {
		if (c.getNumberOfTokens() != mSCommand.getNumberOfOperands()+1) { 
			errMsg.setMessage("Command " + mSCommand.getName() + 
					" requires " + mSCommand.getNumberOfOperands() + 
			" operand(s)."); 
			return false; 
		}
		else 
			
			for (int tn = 2; tn <= c.getNumberOfTokens(); tn++ ) { 
				String oType = mSCommand.getOperandType(tn-1); 
				String token = c.getToken(tn); 
				if (!SimpleOperandHandler.isValidToken(oType, token)) {
					errMsg.setMessage("Operand " + (tn-1) +
							" does not match \"" + oType + "\"."); 
					return false; 
				}
			}

		
		return true; 
	} 

}

