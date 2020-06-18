package be.promsoc.arlon.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class GetTerminalInput {
	
//	static public String readKey(String name, String msg) throws IOException {
//		System.out.println("\n" + name + " : " + msg + " : Hit enter");
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		String s = br.readLine();
//		return s;
//	}
	
	static public String readKey(String msg) throws IOException {
		System.out.println("\n>"  + msg + " (+ Hit enter)");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s = br.readLine();
		return s;
	}
	
	public static String readKey() throws IOException {
		System.out.println("\n>votre choix -> ... (+ Hit enter)");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s = "";
		s = br.readLine();
		return s;
	}
	
//	public static int getInput() {
//		Scanner scnr = new Scanner(System.in);
//		int returnValue = 0;
//		System.out.println("> votre choix ... (+ Hit enter)");
//        try {
//			returnValue = scnr.nextInt();
//		} catch (Exception e) {
//			e.printStackTrace();
//			returnValue = 9;
//		}
//		scnr.close();
//        return returnValue;
//	}
}
