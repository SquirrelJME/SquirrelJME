package net.rim.device.api.lcdui.control;

import javax.microedition.media.Control;

import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public interface DirectionControl extends Control{
	
	@Api
	public static final int DIRECTION_EAST = 2;
	
	@Api
	public static final int DIRECTION_LANDSCAPE = 16;
	
	@Api
	public static final int DIRECTION_NORTH = 1;
	
	@Api
	public static final int DIRECTION_PORTRAIT = 32;
	
	@Api
	public static final int DIRECTION_WEST = 8;
	
	@Api
	public static final int ORIENTATION_LANDSCAPE = 16;
	
	@Api
	public static final int ORIENTATION_PORTRAIT = 32;
	
	@Api
	public static final int ORIENTATION_SQUARE = 0;
	
    @Api
	public int getOrientation();
    
    @Api
	public void setAcceptableScreenDirections(int directions);
}
