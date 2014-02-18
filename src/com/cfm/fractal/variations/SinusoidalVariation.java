package com.cfm.fractal.variations;

import com.cfm.fractal.Point;
import com.cfm.fractal.Variation;

public class SinusoidalVariation extends Variation {

	@Override
	public void variate(Point p, Point out) {
		out.x = Math.sin(p.x);
		out.y = Math.sin(p.y);
	}

}
