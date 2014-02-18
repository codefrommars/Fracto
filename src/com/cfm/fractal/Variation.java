package com.cfm.fractal;

public abstract class Variation {
	public abstract void variate(Point p, Point out);
	
	public double r(Point p){
		return Math.sqrt(p.x * p.x + p.y * p.y);
	}
	
	public double theta(Point p){
		return Math.atan2(p.x, p.y);
	}
	
	public double phi(Point p){
		return Math.atan2(p.y, p.x);
	}
	
}
