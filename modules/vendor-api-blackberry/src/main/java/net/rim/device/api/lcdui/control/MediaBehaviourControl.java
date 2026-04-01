package net.rim.device.api.lcdui.control;

import javax.microedition.media.Control;

import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public interface MediaBehaviourControl extends Control {
	
	@Api
	public static final String JAD_ATTRIBUTE_ENABLED = "RIM-MIDlet-MediaPlayerModeEnabled";
	
	@Api
	public void setMediaPlayerModeEnabled(boolean enabled);
	
	@Api
	public boolean isMediaPlayerModeEnabled();
}
