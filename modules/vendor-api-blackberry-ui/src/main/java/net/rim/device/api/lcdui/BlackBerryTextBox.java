package net.rim.device.api.lcdui;

import javax.microedition.lcdui.TextBox;
import javax.microedition.media.Control;
import javax.microedition.media.Controllable;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public class BlackBerryTextBox extends TextBox implements Controllable {
	
	@Api
	public BlackBerryTextBox(String __title, String __text, int __max, int __con) throws IllegalArgumentException {
		super(__title, __text, __max, __con);
	}

	@Api
	@Override
	public Control getControl(String __control) throws IllegalArgumentException, IllegalStateException {
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public Control[] getControls() {
		throw Debugging.todo();
	}

}
