package net.rim.device.api.lcdui.game;

import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.media.Controllable;

import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public abstract class BlackBerryGameCanvas extends GameCanvas implements Controllable {
	@Api
	public BlackBerryGameCanvas(boolean suppressKeyEvents) {
		super(suppressKeyEvents);
	}
}
