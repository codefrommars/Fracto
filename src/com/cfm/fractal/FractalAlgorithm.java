package com.cfm.fractal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.cfm.fractal.variations.LinearVariation;


public class FractalAlgorithm {
	
	
	protected double[] funcProb;
	protected FuncParams[] funcParams;
	protected double[][] palette;
	protected Variation[] vars;
	
	//Temporal value
	private transient Random rand;
	private transient Point out;
	
	public FractalAlgorithm(long seed) {
		rand = new Random(seed);
		out = new Point();
	}
	
	public int iterate(Point p){
		int i = selectFunction();
		
		apply(i, p);
		vars[i].variate(p, out);
		
		p.x = out.x;
		p.y = out.y;
		
		return i;
	}
	
	public void load(File file) throws FileNotFoundException{
		//load functions number
		//NumberFormat format = NumberFormat.getInstance(Locale.US);
		Scanner scanner = new Scanner(file);
		scanner.useDelimiter(Pattern.compile("(#.*|\\s)+"));
		scanner.useLocale(Locale.UK);
		
		int n = scanner.nextInt();
		
		funcParams = new FuncParams[n];
		funcProb = new double[n];
		palette = new double[n][3];
		vars = new Variation[n];
		
		for(int i = 0; i < n; i++){
			funcParams[i] = new FuncParams();
			
			funcParams[i].a = scanner.nextDouble();
			funcParams[i].b = scanner.nextDouble();
			funcParams[i].c = scanner.nextDouble();
			
			funcParams[i].d = scanner.nextDouble();
			funcParams[i].e = scanner.nextDouble();
			funcParams[i].f = scanner.nextDouble();
			
			funcProb[i] = scanner.nextDouble();
			
			palette[i][0] = scanner.nextDouble();
			palette[i][1] = scanner.nextDouble();
			palette[i][2] = scanner.nextDouble();
			
			vars[i] = new LinearVariation();
		}
		
		scanner.close();
	}
	
	private int selectFunction(){
		double 	p = rand.nextDouble(),
				accum = 0;
				
		for(int index = 0; index < funcProb.length - 1; index++){
			accum += funcProb[index];
			
			if( p < accum )
				return index;
		}
		//System.out.println("Last function ?");
		return funcProb.length - 1;
	}
	
	private void apply(int funcIndex, Point p){
		double x = p.x;
		double y = p.y;
		
		p.x = funcParams[funcIndex].a * x + funcParams[funcIndex].b * y + funcParams[funcIndex].c;
		p.y = funcParams[funcIndex].d * x + funcParams[funcIndex].e * y + funcParams[funcIndex].f;
		
		//Apply color stuff
		p.r = palette[funcIndex][0];
		p.g = palette[funcIndex][1];
		p.b = palette[funcIndex][2];
		//System.out.println(funcIndex + ": " + p.r + ", " + p.g + ", " + p.b);
	}

	@Override
	public FractalAlgorithm clone() {
		
		FractalAlgorithm o = new FractalAlgorithm((long) (Math.random() * 57895) );
		
		int n = funcParams.length;
		
		o.funcParams = new FuncParams[n];
		o.funcProb = new double[n];
		o.palette = new double[n][3];
		o.vars = new Variation[n];
		
		System.arraycopy(funcParams, 0, o.funcParams, 0, n);
		System.arraycopy(funcProb, 0, o.funcProb, 0, n);
		System.arraycopy(vars, 0, o.vars, 0, n);
		
		for(int i = 0; i < n; i++)
			System.arraycopy(palette[i], 0, o.palette[i], 0, 3);
		
		return o;
	}
	
}
