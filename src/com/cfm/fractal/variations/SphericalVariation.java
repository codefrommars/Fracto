package com.cfm.fractal.variations;

import com.cfm.fractal.Point;
import com.cfm.fractal.Variation;

public class SphericalVariation extends Variation {

	@Override
	public void variate(Point p, Point out) {
		double inv_r2 = r(p);
		inv_r2 = 1 / (inv_r2 * inv_r2);
		
		out.x = p.x * inv_r2;
		out.y = p.y * inv_r2;
	}

}
