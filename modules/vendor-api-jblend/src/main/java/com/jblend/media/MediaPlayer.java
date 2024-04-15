// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.media;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import com.jblend.io.j2me.events.NativeMediaEventDispatcher;

@Api
public abstract class MediaPlayer
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
	public static final int ERROR = 65536;
	
	@Api
	protected static final int REAL_WIDTH = Debugging.<Integer>todoObject();
	
	@Api
	protected static final int REAL_HEIGHT = Debugging.<Integer>todoObject();
	
	@Api
	public MediaPlayer()
	{
		throw Debugging.todo();
	}
	
	@Api
	public abstract void setData(MediaData var1);
	
	@Api
	public abstract void play();
	
	@Api
	public abstract void play(boolean var1);
	
	@Api
	public abstract void play(int var1);
	
	@Api
	public abstract void stop();
	
	@Api
	public abstract void pause();
	
	@Api
	public abstract void resume();
	
	@Api
	public abstract int getState();
	
	@Api
	public abstract void addMediaPlayerListener(MediaPlayerListener var1);
	
	@Api
	public abstract void removeMediaPlayerListener(MediaPlayerListener var1);
	
	@Api
	protected static void addNativeMediaEventDispatcher(
		NativeMediaEventDispatcher var0)
	{
		throw Debugging.todo();
	}
}
