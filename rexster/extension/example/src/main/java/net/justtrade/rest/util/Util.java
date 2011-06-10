/**
 * 
 */
package net.justtrade.rest.util;

/**
 * @author Hasan
 *
 */
public class Util {

	/**
	 * @param args
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
	
	public static long replaceByteX(long oldVal, int newVal, int X) {
		return ((~(255  << (8*X))) & oldVal) | ((255 & newVal) << (8*X));
	}

	public static long getByteX(long val, int X) {
		return (val >> (8*X)) & 255;
	}

}
