package net.rim.device.api.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public abstract class TouchGesture {
	
	@Api
	public TouchGesture() {}
	
	@Api
	public static int CLICK_REPEAT;

	@Api
	public static int DOUBLE_TAP;
	
	@Api
	public static int HOVER;
	
	@Api
	public static int NAVIGATION_SWIPE;
	
	@Api
	public static int PINCH_BEGIN;
	
	@Api
	public static int PINCH_END;
	
	@Api
	public static int PINCH_UPDATE;
	
	@Api
	public static int SWIPE;

	@Api
	public static int SWIPE_EAST;
	
	@Api
	public static int SWIPE_NORTH;
	
	@Api
	public static int SWIPE_SOUTH;
	
	@Api
	public static int SWIPE_WEST;
	
	@Api
	public static int TAP;
	
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
