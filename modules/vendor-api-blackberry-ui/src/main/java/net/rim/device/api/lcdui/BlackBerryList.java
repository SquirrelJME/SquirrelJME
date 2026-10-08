package net.rim.device.api.lcdui;

import javax.microedition.lcdui.List;
import javax.microedition.media.Control;
import javax.microedition.media.Controllable;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public class BlackBerryList extends List implements Controllable {
	
	@Api
	public BlackBerryList(String __title, int __type) throws IllegalArgumentException {
		super(__title, __type);
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
