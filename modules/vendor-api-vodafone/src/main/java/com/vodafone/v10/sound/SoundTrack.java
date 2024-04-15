// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.vodafone.v10.sound;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

public class SoundTrack
{
	@Api
	public static final int NO_DATA = 0;
	
	@Api
	public static final int READY = 1;
	
	@Api
	public static final int PLAYING = 2;
	
	@Api
	public static final int PAUSED = 3;
	
	@Api
	public void setSound(Sound var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public Sound getSound()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void removeSound()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void play()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void play(int var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void stop()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void pause()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void resume()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void mute(boolean var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getState()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setVolume(int var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getVolume()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setPanpot(int var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getPanpot()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setSubjectTo(SoundTrack var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public SoundTrack getSyncMaster()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getID()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setEventListener(SoundTrackListener var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isMute()
	{
		throw Debugging.todo();
	}
}
