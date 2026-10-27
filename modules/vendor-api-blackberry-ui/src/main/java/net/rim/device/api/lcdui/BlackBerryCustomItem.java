package net.rim.device.api.lcdui;

import javax.microedition.lcdui.CustomItem;
import javax.microedition.media.Controllable;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.rim.device.api.ui.TouchEvent;

@Api
public abstract class BlackBerryCustomItem extends CustomItem implements Controllable {
	
	@Api
	public BlackBerryCustomItem(String __a) {
		super(__a);
	}
	
	@Api
	public void touchEvent(TouchEvent message) {
	throw Debugging.todo();
	}
}
