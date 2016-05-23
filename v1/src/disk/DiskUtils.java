package disk;

import java.nio.ByteBuffer;

public class DiskUtils {
	
	/**
	 * Determines if a given number is a power of 2.
	 * @param number the number to be analyzed
	 * @return true if number is a power of 2
	 * @return false if number is NOT a power of 2
	 */
	public static boolean powerOf2(int number){
		//Calculates log base 2 of number and checks if it is an integer
		double power = Math.log10(number) / Math.log10(2);
		return (power - (int)power == 0);
	}
	
	/**
	 * Converts an integer to an array of bytes.
	 * @param value the integer to convert
	 * @return the byte equivalent
	 */
	public static byte[] convertToByteArray(int value){
		byte[] btr = new byte[4];
		int mask = 0xFF;
		int shiftNumber = 0;
		
		for(int i = 3; i >=0 ; i--){
			int byteValue = value & mask;
			
			btr[i] = (byte)(byteValue >> shiftNumber);
			shiftNumber+=8;
			mask = (int)((byte)mask << shiftNumber);
		}
		
		return btr;
	}
	
	/**
	 * Converts a byte array to an integer.
	 * @param arr the array to convert
	 * @return the corresponding integer
	 */
	public static int convertToInt(byte[] arr){
		return ByteBuffer.wrap(arr).getInt();
	}
	
	

}
