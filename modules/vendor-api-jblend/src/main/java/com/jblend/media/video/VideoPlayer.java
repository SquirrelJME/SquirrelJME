// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.media.video;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import com.jblend.media.MediaData;
import com.jblend.media.MediaImageOperator;
import com.jblend.media.MediaPlayer;
import com.jblend.media.MediaPlayerListener;

@Api
public class VideoPlayer
	extends MediaPlayer
	implements MediaImageOperator
{
	@Api
	public VideoPlayer()
	{
		throw Debugging.todo();
	}
	
	@Api
	public VideoPlayer(VideoData var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public VideoPlayer(byte[] var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static int checkData(byte[] var0)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setData(VideoData var1)
	{
		throw Debugging.todo();
	}
	
	@Override
	public void setData(MediaData var1)
	{
		throw Debugging.todo();
	}
	
	@Override
	public int getX()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int getY()
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
	
	@Override
	public void setBounds(int var1, int var2, int var3, int var4)
	{
		throw Debugging.todo();
	}
	
	@Override
	public int getOriginX()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int getOriginY()
	{
		throw Debugging.todo();
	}
	
	@Override
	public void setOrigin(int var1, int var2)
	{
		throw Debugging.todo();
	}
	
	@Override
	public int getMediaWidth()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int getMediaHeight()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void repaintCurrent()
	{
		throw Debugging.todo();
	}
	
	@Override
	public void play()
	{
		throw Debugging.todo();
	}
	
	@Override
	public void play(boolean var1)
	{
		throw Debugging.todo();
	}
	
	@Override
	public void play(int var1)
	{
		throw Debugging.todo();
	}
	
	@Override
	public void stop()
	{
		throw Debugging.todo();
	}
	
	@Override
	public void pause()
	{
		throw Debugging.todo();
	}
	
	@Override
	public void resume()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int getState()
	{
		throw Debugging.todo();
	}
	
	@Override
	public void addMediaPlayerListener(MediaPlayerListener var1)
	{
		throw Debugging.todo();
	}
	
	@Override
	public void removeMediaPlayerListener(MediaPlayerListener var1)
	{
		throw Debugging.todo();
	}
}

