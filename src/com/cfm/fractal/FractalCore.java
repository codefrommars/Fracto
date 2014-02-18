package com.cfm.fractal;

import java.util.Random;

public class FractalCore implements Runnable{
	private Random rand;
	private RunListener listener;
	private RenderData data;
	private FractalAlgorithm algorithm;
	private Config config;
	private int coreNumber;
	
	public FractalCore(int coreNumber, long seed, RunListener listener, FractalAlgorithm algorithm, Config config, long iterations){
		this.coreNumber = coreNumber;
		this.listener = listener;
		rand = new Random(seed);
		this.algorithm = algorithm.clone();
		this.config = config;
		this.data = new RenderData(config.width , config.height , config.upSample);
	}
	
	public void plot(RenderData data, Point p, Config c) {
		int x = (int) (c.upSample * c.width *  (p.x - c.xMin)  / (c.xMax - c.xMin) );
		int y = (int) (c.upSample * c.height * (p.y - c.yMin)  / (c.yMax - c.yMin) );
		data.set( x, y, p.r, p.g, p.b, 0.5, 0.5, 0.5);		
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

	@Override
	public void run() {
		//Generate a random point
		Point p = randomPoint(config.xMin, config.xMax, config.yMin, config.yMax);
		
		//Normalize it
		//for(int i = 0; i < config.plotThreshold; i++){
		//	algorithm.iterate(p);
		//}
		
		long nextMilestone = 100_000_000;
		System.out.println("Iterations");
		//Run the iterations
		for(long i = 0; i < config.iterations; i++){
			algorithm.iterate(p);
			//System.out.println(p.r + ", " + p.g + ", " + p.b);
			plot(data, p, config);
			
			if( nextMilestone <= i ){
				System.out.printf("The core %d reached milestone %2.2f \n", coreNumber, nextMilestone * 100 / (float)config.iterations);
				nextMilestone += 100_000_000;
			}
		}
		System.out.printf("The core %d finished!\n", coreNumber);
	}
	
	public RenderData getRenderData(){
		return data;
	}
}
