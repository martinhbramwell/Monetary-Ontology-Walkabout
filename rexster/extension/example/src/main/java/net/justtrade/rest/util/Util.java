package net.justtrade.rest.util;

/**
* Some generic global utilities and a main method for testing them
* 
* @author Martin "Hasan" Bramwell (http://hasanbramwell.blogspot.com/2011/03/hello-world.html)
*/
public class Util {

	
	/**
	 * Main for testing.
	 * @param args unused
	 * @throws  none
	 * @throws Exception if thrown.
	 */
	public static void main(String[] args) {
		
		long tempVal = -1;
		
		tempVal = replaceByteX(65535, 9, 1);
		System.out.println("Result : " + tempVal + " or " + Long.toBinaryString(tempVal));
		
		tempVal = replaceByteX(65535, 6, 2);
		System.out.println("Result : " + tempVal + " or " + Long.toBinaryString(tempVal));
		
		tempVal = getByteX(7, 0);
		System.out.println("Result : " + tempVal + " or " + Long.toBinaryString(tempVal));
		
		tempVal = getByteX(65439, 1);
		System.out.println("Result : " + tempVal + " or " + Long.toBinaryString(tempVal));
		
		tempVal = getByteX(63231, 2);
		System.out.println("Result : " + tempVal + " or " + Long.toBinaryString(tempVal));
		
		

	}
	
	/**
	 * Utility for using a long as a bit map. Alter one of its bytes.
	 * @param oldVal the value to be altered
	 * @param newVal the value to include
	 * @param X where to include
	 * @return a new long value with on byte replaced in the indicated position
	 * @throws  none
	 * @throws Exception if thrown.
	 */
	public static long replaceByteX(long oldVal, int newVal, int X) {
		return ((~(255  << (8*X))) & oldVal) | ((255 & newVal) << (8*X));
	}

	/**
	 * Utility for using a long as a bit map. Get one of its bytes.
	 * @param val
	 * @param X
	 * @return an all zeroes long except for the lowest eight bits.
	 * @throws   none
	 * @throws Exception if thrown.
	 */
	public static long getByteX(long val, int X) {
		return (val >> (8*X)) & 255;
	}

}
