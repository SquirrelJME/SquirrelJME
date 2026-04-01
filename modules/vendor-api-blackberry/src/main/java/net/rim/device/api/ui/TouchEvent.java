package net.rim.device.api.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public abstract class TouchEvent {

	@Api
	public TouchEvent() {}
	
	@Api
	public static int CANCEL = 0;

	@Api
	public static int CLICK = 1;
	
	@Api
	public static int DOWN = 2;
	
	@Api
	public static int GESTURE = 3;
	
	@Api
	public static int MOVE = 4;
	
	@Api
	public static int UNCLICK = 5;
	
	@Api
	public static int UP = 6;
	
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
