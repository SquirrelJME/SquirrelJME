package net.rim.device.api.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public abstract class TouchGesture {
	
	@Api
	public TouchGesture() {}
	
	@Api
	public static final int CLICK_REPEAT = 13575;

	@Api
	public static final int DOUBLE_TAP = 3;
	
	@Api
	public static final int HOVER = 0;
	
	@Api
	public static final int NAVIGATION_SWIPE = 7;
	
	@Api
	public static final int PINCH_BEGIN = 4;
	
	@Api
	public static final int PINCH_END = 6;
	
	@Api
	public static final int PINCH_UPDATE = 5;
	
	@Api
	public static final int SWIPE = 13572;

	@Api
	public static final int SWIPE_EAST = 4;
	
	@Api
	public static final int SWIPE_NORTH = 1;
	
	@Api
	public static final int SWIPE_SOUTH = 2;
	
	@Api
	public static final int SWIPE_WEST = 8;
	
	@Api
	public static final int TAP = 2;
	
	@Api
	public int getClickRepeatCount() {throw Debugging.todo();};
	
	@Api
	public abstract int getEvent();
	
	@Api
	public int getHoverCount() {throw Debugging.todo();};
	
	@Api
	public float getPinchMagnitude() {throw Debugging.todo();};
	
	@Api
	public int getSwipeAngle() {throw Debugging.todo();};
	
	@Api
	public int getSwipeContentAngle() {throw Debugging.todo();};
	
	@Api
	public int  getSwipeDirection() {throw Debugging.todo();};
	
	@Api
	public int  getSwipeMagnitude() {throw Debugging.todo();};
	
	@Api
	public int  getTapCount() {throw Debugging.todo();};
	

}
