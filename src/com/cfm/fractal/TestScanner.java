package com.cfm.fractal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TestScanner {
	public static void main(String[] ar) throws FileNotFoundException{
		
		//Read all 
		Scanner scanner = new Scanner(new File("a.txt"));
		scanner.useDelimiter(Pattern.compile("(#.*|\\s)+"));
		scanner.useLocale(Locale.UK);
		
		System.out.println(scanner.nextInt());
		
		
		while( scanner.hasNextDouble() )
			System.out.println(scanner.nextDouble());
		
	}
}
