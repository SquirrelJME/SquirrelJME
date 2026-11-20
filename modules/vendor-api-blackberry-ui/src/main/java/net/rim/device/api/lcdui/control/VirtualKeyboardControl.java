package net.rim.device.api.lcdui.control;

import javax.microedition.media.Control;

import cc.squirreljme.runtime.cldc.annotation.Api;

public interface VirtualKeyboardControl extends Control {
	
	@Api
	public static final int KEYBOARD_HIDE = 0;
	
	@Api
	public static final int KEYBOARD_HIDE_FORCE = 2;
	
	@Api
	public static final int KEYBOARD_IGNORE = 4;
	
	@Api
	public static final int KEYBOARD_RESTORE = 5;
	
	@Api
	public static final int KEYBOARD_SHOW = 1;
	
	@Api
	public static final int KEYBOARD_SHOW_FORCE = 3;
	
	@Api
	public void setKeyboardVisibility(int visibility);
	
	@Api
	public int getKeyboardVisibility();

}
