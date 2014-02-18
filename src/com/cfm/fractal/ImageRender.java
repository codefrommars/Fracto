package com.cfm.fractal;

import java.awt.image.BufferedImage;

import com.cfm.fractal.RenderData.RenderPoint;

public class ImageRender {
	
	public BufferedImage renderData(RenderData data){
		
		int upsample = data.getUpsampling();
		
		int w = data.getbWidth() / upsample;
		int h = data.getbHeight() / upsample;
		
		BufferedImage im = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		
		RenderPoint point = new RenderPoint();
		
		int maxFreq = getMaxFrequency(data);
		
		for(int i = 0; i < w; i++)
			for (int j = 0; j < h; j++){
				
				sample(i, j, data, point, maxFreq);
				
				im.setRGB(i, h - j - 1, packColor(point.r, point.g, point.b));
			}
		
		return im;
	}
	
	public void sample(int i, int j, RenderData data, RenderPoint sample, int maxFreq){
		int upsample = data.getUpsampling();
		int up_i = i * upsample;
		int up_j = j * upsample;
		
		double r = 0, g = 0, b = 0, f = 0;
		
		int counted = 0;
		
		for(int iu = up_i ; iu < up_i + upsample; iu++)
			for(int ju = up_j ; ju < up_j + upsample; ju++){
				RenderPoint d = data.getData(iu, ju);
				if( d == null )
					continue;
				
				r += d.r;
				g += d.g;
				b += d.b;
				
				f += d.freq;
				
				counted++;
			}
		
		upsample *= upsample;
		
		f /= upsample;
		
		double intensity = Math.log(f + 1) / Math.log(maxFreq + 1); 
		double gamma = Math.pow(intensity, 0.25) / upsample;
		sample.r = (float) (r * gamma);
		sample.g = (float) (g * gamma);
		sample.b = (float) (b * gamma);
	}
	
	private int getMaxFrequency (RenderData data){
		int max = 0;
		int totalNums = data.getbWidth() * data.getbHeight();
		for(int i = 0; i < totalNums; i++){
			RenderPoint p = data.getData(i);
			if( p == null )
				continue;
			max = (max < p.freq) ? p.freq : max; 
		}
		
		return max;
	}
	
	public static int packColor(int r, int g, int b, int a){
		return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF) ;
	}
	
	public static int packColor(int r, int g, int b){
		return (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF) ;
	}
	
	public static int packColor(int rgb, int a){
		return (a & 0xFF) << 24 | rgb;
	}
	
	public static int packColor(double r, double g, double b, double a){
		return packColor( (int) (r * 255), (int) (g * 255), (int) (b * 255), (int) (a * 255) );
	}
	
	public static int packColor(double r, double g, double b){
		return packColor( (int) (r * 255), (int) (g * 255), (int) (b * 255));
	}

}
