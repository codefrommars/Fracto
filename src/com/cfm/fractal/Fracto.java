package com.cfm.fractal;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

import javax.imageio.ImageIO;

public class Fracto {
	
	public static void main(String[] args) throws IOException {
		System.out.println("Starting Fracto...");
		final Config c = new Config();
		c.load(new File("computer.conf"));
		
		FractalAlgorithm alg = new FractalAlgorithm(42);
		alg.load(new File(c.fractalName));
		
		FractalComputer computer = new FractalComputer(new RunListener() {
			
			long nextMilestone = 10_000_000;
			
			@Override
			public long nextMilestone() {
				return nextMilestone;
			}
			
			@Override
			public void completedMilestone(int coreNumber, long value) {
				NumberFormat.getInstance().setMaximumFractionDigits(2);
				System.out.printf("Done %2.2f%% in core: %d\n", ( 100 * ((float)value / c.iterations)), coreNumber);
				nextMilestone += 10_000_000;
			}
		});
		computer.setup(alg, c);	
		System.out.println("Using config: " + c);
		
		long nanoStart = System.nanoTime();
		System.out.println("Running algorithm...");
		RenderData data = computer.run(alg, c);
		
		long nanoRun = System.nanoTime();
		float secs = (nanoRun - nanoStart) / 1000000000f;
		System.out.println("Run finished in " + secs + " secs");
		System.out.println("Obtained points: " + data.getRenderedPoints());
		
		ImageRender r = new ImageRender();
		System.out.println("Rendering data...");
		BufferedImage image = r.renderData(data);
		long nanoRender = System.nanoTime();
		secs = (nanoRender - nanoRun) / 1000000000f;
		System.out.println("Rendering finished in " + secs + " secs");
		
		
		System.out.println("Writing image ...");
		ImageIO.write(image, "PNG", new File("out/render.png"));
		long nanoWriting = System.nanoTime();
		secs = (nanoWriting - nanoRender) / 1000000000f;
		System.out.println("Data Written in " + secs + " secs");
		secs = (nanoWriting - nanoStart) / 1000000000f;
		System.out.println("Total iterations: " + (c.cores * c.iterations));
		System.out.println("Algorithm complete in " + secs + " secs");
	}	

}
