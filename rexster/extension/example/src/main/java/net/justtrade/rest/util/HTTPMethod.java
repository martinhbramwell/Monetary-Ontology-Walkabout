package net.justtrade.rest.util;

import java.util.HashMap;

public class HTTPMethod {
	
	public static final int GET = 1;
	public static final int POST = 2;
	public static final int PUT = 3;
	public static final int DELETE = 4;
	
	static final HashMap<String, Integer> intMap = new HashMap<String, Integer>();

	static {
		intMap.put("GET", GET);
		intMap.put("POST", POST);
		intMap.put("PUT", PUT);
		intMap.put("DELETE", DELETE);
	}
	
	public static int convert(String name) {
		return intMap.get(name.toUpperCase()).intValue();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.print("------------------" + HTTPMethod.convert("put") + "-------------------");
		System.out.print("------------------" + HTTPMethod.convert("POST") + "-------------------");

	}

}
