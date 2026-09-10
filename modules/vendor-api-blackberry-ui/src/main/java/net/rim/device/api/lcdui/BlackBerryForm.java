package net.rim.device.api.lcdui;

import javax.microedition.lcdui.Form;
import javax.microedition.media.Control;
import javax.microedition.media.Controllable;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public class BlackBerryForm extends Form implements Controllable{
	
	@Api
	public BlackBerryForm(String __t) {
		super(__t);
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
