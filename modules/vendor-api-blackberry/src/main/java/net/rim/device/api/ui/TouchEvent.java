package net.rim.device.api.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public abstract class TouchEvent {

	@Api
	public TouchEvent() {}
	
	@Api
	public static final int CANCEL = 0;

	@Api
	public static final int CLICK = 13573;
	
	@Api
	public static final int DOWN = 13569;
	
	@Api
	public static final int GESTURE = 1;
	
	@Api
	public static final int MOVE = 13571;
	
	@Api
	public static final int UNCLICK = 13574;
	
	@Api
	public static final int UP = 13570;
	
	@Api
	public abstract int getEvent();
	
	@Api
	public abstract TouchGesture getGesture();
	
	@Api
	public abstract int getGlobalX(int touch);
	
	@Api
	public abstract int getGlobalY(int touch);
	
	@Api
	public abstract void getMovePoints(int touch, int[] x, int[] y, int[] time);
	
	@Api
	public abstract int getMovePointsSize();
	
	@Api
	public abstract int getTime();
	
	@Api
	public abstract int getX(int touch);
	
	@Api
	public abstract int getY(int touch);
	
	@Api
	public abstract boolean isValid();

}
