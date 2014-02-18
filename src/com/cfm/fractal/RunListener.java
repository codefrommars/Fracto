package com.cfm.fractal;

public interface RunListener {
	public void completedMilestone(int coreNumber, long value);
	public long nextMilestone();
}
