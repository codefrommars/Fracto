package com.cfm.fractal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Config {
	public long iterations;
	public int width, height, upSample, plotThreshold;
	public double xMin, xMax, yMin, yMax;
	
	public boolean autofocus;
	public double sigmas;
	public int cores;
	public long seed;
	
	public String fractalName;
	
	public Config(){
//		width = 600;
//		height = 800;
//		upSample = 2;
//		xMin = -2;
//		xMax = 2;
//		yMin = -2;
//		yMax = 2;
//		plotThreshold = 200;
//		iterations = 10000000;
	}

	
	@Override
	protected Config clone() {
		Config o = new Config();
		
		o.iterations = iterations;
		o.width = width;
		o.height = height;
		o.upSample = upSample;
		o.plotThreshold = plotThreshold;
		o.xMin = xMin;
		o.xMax = xMax;
		o.yMin = yMin;
		o.yMax = yMax;
		o.autofocus = autofocus;
		o.sigmas = sigmas;
		o.cores = cores;
		o.seed = seed;
		
		return o;
	}

	public void load(File file) throws FileNotFoundException{
		Scanner scanner = new Scanner(file);
		scanner.useDelimiter(Pattern.compile("(#.*|\\s)+"));
		scanner.useLocale(Locale.UK);
		
		do{
			fractalName = scanner.nextLine();
		}while( fractalName != null && !fractalName.trim().isEmpty() && fractalName.charAt(0) == '#' );
		
		cores = scanner.nextInt();
		seed = scanner.nextLong();
		
		width = scanner.nextInt();
		height = scanner.nextInt();
		upSample = scanner.nextInt();
		
		iterations = scanner.nextInt();
		plotThreshold = scanner.nextInt();
		
		autofocus = scanner.nextBoolean();
		
		if( autofocus ){
			sigmas = scanner.nextDouble();
		}
		
		xMin = scanner.nextDouble();
		xMax = scanner.nextDouble();
		yMin = scanner.nextDouble();
		yMax = scanner.nextDouble();
		
		scanner.close();
	}

	@Override
	public String toString() {
		return "Config [iterations=" + iterations + ", width=" + width + ", height=" + height + ", upSample=" + upSample + ", plotThreshold=" + plotThreshold
				+ ", xMin=" + xMin + ", xMax=" + xMax + ", yMin=" + yMin + ", yMax=" + yMax + ", autofocus=" + autofocus + ", sigmas=" + sigmas + ", cores="
				+ cores + ", seed=" + seed + "]";
	}
	
}
