package com.cfm.fractal;

import java.util.Arrays;
import java.util.Random;

import com.cfm.fractal.RenderData.RenderPoint;

public class FractalComputer {
	private Random rand;
	private RunListener listener;
	private SetMetadata metadata;
	
	public FractalComputer(RunListener listener){
		this.listener = listener;
	}
	
	public void analizeSet(FractalAlgorithm alg, Config c){
		
		long testSize = 1_000_000;
		
		int[] freqLine = new int[c.height * c.upSample];
		int n = 0;
		
		double uX = 0, uY = 0, sX = 0, sY = 0;
		
		Point p = randomPoint(c.xMin, c.xMax, c.yMin, c.yMax);
		
		//Mean
		for(long i = 0; i < testSize; i++){
			alg.iterate(p);
			uX += p.x / testSize;
			uY += p.y / testSize;
			
			int y = (int) (c.upSample * c.height * (p.y - c.yMin)  / (c.yMax - c.yMin) );
			
			if( y >= freqLine.length || y < 0)
				continue;
			
			freqLine[y]++;
			n++;
		}
		
		//Standard deviation
		p = randomPoint(c.xMin, c.xMax, c.yMin, c.yMax);
		for(int i = 0; i < testSize; i++){
			alg.iterate(p);
			sX += (p.x - uX) * (p.x - uX) / testSize; 
			sY += (p.y - uY) * (p.y - uY) / testSize; 
		}
		
		sX = Math.sqrt(sX);
		sY = Math.sqrt(sY);
		
		
		
		metadata = new SetMetadata();
		
		metadata.uX = uX;
		metadata.uY = uY;
		metadata.sX = sX;
		metadata.sY = sY;
		
		metadata.yPercentiles = new double[c.cores];
		
		double percentileSize = (double)n / c.cores;
		
		System.out.println("Counted points: " + n);
		System.out.println("Percentile Size: " + percentileSize);
		
		int currPercentile = 0;
		int accum = 0;
		
		for(int i = 0; i < freqLine.length; i++){
			accum += freqLine[i];
			
			if( accum > percentileSize * (currPercentile + 1) ){
				System.out.println("At line " + i + " there are " + accum);
				metadata.yPercentiles[currPercentile] = i / (double)freqLine.length;
				currPercentile ++;
			}
		}
		
		//make sure the last is reached ... don't know if needed though
		metadata.yPercentiles[c.cores - 1] = 1.0; 
		metadata.testSize = n;
		
		System.out.println("Percentiles: " + Arrays.toString(metadata.yPercentiles));
	}	
	
	public void setup(FractalAlgorithm algorithm, Config config){
		rand = new Random(config.seed);
		analizeSet(algorithm, config);
		
		if( config.autofocus ){
			config.xMin = metadata.uX - config.sigmas * metadata.sX;
			config.xMax = metadata.uX + config.sigmas * metadata.sX;
			config.yMin = metadata.uY - config.sigmas * metadata.sY;
			config.yMax = metadata.uY + config.sigmas * metadata.sY;
		}
	}
	
	public RenderData run(FractalAlgorithm algorithm, Config config){
		
		//Setup the cores
		Thread threads[] = new Thread[config.cores]; 
		FractalCore[] cores = new FractalCore[config.cores];
		
		//Divide the cores in y 
		double dy = (config.yMax - config.yMin) / config.cores;
		double y = config.yMin;
		int coreSize = config.height / config.cores;
		
		double prevPercentile = 0;
		int totalRealLines = config.height; 
		for(int i = 0; i < threads.length; i++){
			
			//Change the y part of the cores
			Config c = config.clone();
			
//			Naive approach			
//			c.yMin = y + dy * i;
//			c.yMax = y + dy * (i + 1);
//			//Change the core size
//			c.height = coreSize;
//			
//			Percentile approach
			
			int yLineStart = (int)(prevPercentile * totalRealLines);
			int yLineEnd = (int)(metadata.yPercentiles[i] * totalRealLines) - 1;
			c.height = yLineEnd - yLineStart + 1;
			c.yMin = config.yMin +  prevPercentile 				* (config.yMax - config.yMin);
			c.yMax = config.yMin +  metadata.yPercentiles[i]	* (config.yMax - config.yMin);
			
			//Setup the core
			cores[i] = new FractalCore(i, rand.nextLong(), listener, algorithm, c, c.iterations);
			threads[i] = new Thread(cores[i]);
			prevPercentile = metadata.yPercentiles[i];
		}
		
		//Start the cores
		for(int i = 0; i < threads.length; i++)
			threads[i].start();
		
		//Wait for the cores to finish
		for(int i = 0; i < threads.length; i++)
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		//Allocate an empty set of data
		RenderData data = new RenderData(config.width , config.height , config.upSample);
		
		//Mix the data
		System.out.println("Mixing the data");
		int dataLength = data.getSize();
		System.out.println("Total Data Length: " + dataLength);
		
		int accum = 0;
		for(int k = 0; k < cores.length; k++){
			System.out.println("For core " + k + ": " + cores[k].getRenderData().getSize());
			accum += cores[k].getRenderData().getSize();
		}
		
		System.out.println("Accumulated: " + accum);
		
		int chunkStart = 0;
		long totalRealPoints = 0;
		for(int k = 0; k < cores.length; k++){
			long cc = 0;	
			for(int i = 0; i < cores[k].getRenderData().getSize(); i++){
				RenderPoint p = cores[k].getRenderData().getData(i);
				if( p == null )
					continue;
				
				cc += p.freq;
				data.mix( chunkStart + i, p);
			}
			System.out.println("Core " + k + " had " + cc + "real points");
			
			totalRealPoints += cc;
			chunkStart += cores[k].getRenderData().getSize();
		}
		
		System.out.println("Total Real Points: " + totalRealPoints);
		
		return data;
	}

	public Point randomPoint(double xMin, double xMax, double yMin, double yMax){
		Point p = new Point();
		
		p.r = 0;
		p.g = 0;
		p.b = 0;
		
		p.x = xMin + rand.nextDouble() * (xMax - xMin);
		p.y = yMin + rand.nextDouble() * (yMax - yMin);
			
		return p;
	}
	
}
