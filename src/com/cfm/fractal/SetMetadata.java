package com.cfm.fractal;

public class SetMetadata {
	//median and sigma
	public double uX, uY, sX, sY;
	public double yPercentiles[];
	public int testSize;
	
	@Override
	public String toString() {
		return "SetMetadata [media=(" + uX + ", " + uY + "), sigma(" + sX + ", " + sY + ")]";
	}
		
}
