package com.cfm.fractal.variations;

import com.cfm.fractal.Point;
import com.cfm.fractal.Variation;

public class LinearVariation extends Variation {

	@Override
	public void variate(Point p, Point out) {
		out.x = p.x;
		out.y = p.y;
	}

}
