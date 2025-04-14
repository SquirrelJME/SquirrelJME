// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.vodafone.v10.system.media;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

@Api
@SuppressWarnings("RedundantThrows")
public class MediaPlayer
	extends Canvas
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
	public MediaPlayer(byte[] var1)
	{
	}
	
	@Api
	public MediaPlayer(String var1)
		throws IOException
	{
	}
	
	@Api
	public void setMediaData(byte[] var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setMediaData(String var1)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getState()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getMediaWidth()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getMediaHeight()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int getWidth()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int getHeight()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setContentPos(int var1, int var2)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void play()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void play(boolean var1)
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
	public void setMediaPlayerListener(MediaPlayerListener var1)
	{
		throw Debugging.todo();
	}
	
	@Override
	protected void paint(Graphics var1)
	{
		throw Debugging.todo();
	}
	
	@Override
	protected void showNotify()
	{
		throw Debugging.todo();
	}
	
	@Override
	protected void hideNotify()
	{
		throw Debugging.todo();
	}
}

