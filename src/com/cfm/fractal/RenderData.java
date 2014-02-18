package com.cfm.fractal;

public class RenderData {
	
	public static class RenderPoint{
		public float r, g, b; 
		public int freq;
	}
	
	private int upsampling;
	private RenderPoint[] data;
	private int renderedPoints;
	private int bWidth, bHeight;
	public static final int FREQUENCY = 0, RED = 1, GREEN = 2, BLUE = 3;
	
	public RenderData(int w, int h, int upsampling){
		bWidth = w * upsampling;
		bHeight = h * upsampling;
		this.upsampling = upsampling;
		
		System.out.println("Creating data of " + w + " by " + h + " = " + bWidth * bHeight);
		data = new RenderPoint[bWidth * bHeight];
	}
	
	public void set(int x, int y, double r, double g, double b, double aR, double aG, double aB ){
		int index = x + y * bWidth;
		
		if( index >= data.length || index < 0 )
			return;
		
		
		if( data[index] == null ){
			 RenderPoint d = new RenderPoint();
			 //Initialization
			 data[index] = d;
			 renderedPoints++;
		}
		
		data[index].freq++;
		
		data[index].r =  (float)(data[index].r * (1 - aR) + r * aR) ;
		data[index].g =  (float)(data[index].g * (1 - aG) + g * aG) ;
		data[index].b =  (float)(data[index].b * (1 - aB) + b * aB) ;
		
	}
	public void acum(int x, int y, double r, double g, double b){
		int index = x + y * bWidth;
		
		if( index >= data.length || index < 0 )
			return;
		
		
		if( data[index] == null ){
			RenderPoint d = new RenderPoint();
			//Initialization
			data[index] = d;
			renderedPoints++;
		}
		
		data[index].freq++;
		
		data[index].r +=  r;
		data[index].g +=  g;
		data[index].b +=  b;
		
	}
	
	public void mix(int index, RenderPoint p){
		float imp = 0.5f;
		if( data[index] == null ){
			data[index] = new RenderPoint();
			imp = 1;
		}
		
		data[index].freq += p.freq;
		
		data[index].r = data[index].r * (1 - imp) + p.r * imp;
		data[index].g = data[index].g * (1 - imp) + p.g * imp;
		data[index].b = data[index].b * (1 - imp) + p.b * imp;
	}
	
	public RenderPoint getData(int x, int y){
		return getData( x + y * bWidth );
	}
	
	public RenderPoint getData(int index){
		return data[index];
	}

	public int getbWidth() {
		return bWidth;
	}

	public int getbHeight() {
		return bHeight;
	}
	
	public int getRenderedPoints(){
		return renderedPoints;
	}

	public int getUpsampling() {
		return upsampling;
	}

	public int getSize() {
		return data.length;
	}
	
}
