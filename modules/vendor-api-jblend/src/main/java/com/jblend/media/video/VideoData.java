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
import java.io.IOException;

@Api
public class VideoData
	extends MediaData
{
	@Api
	public static final String type = "VIDEO";
	
	@Api
	public VideoData()
	{
	}
	
	@Api
	public VideoData(String var1)
		throws IOException
	{
	}
	
	@Api
	public VideoData(byte[] var1)
	{
	}
	
	@Api
	public int getWidth()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getHeight()
	{
		throw Debugging.todo();
	}
	
	@Override
	public String getMediaType()
	{
		throw Debugging.todo();
	}
	
	@Override
	public void setData(byte[] var1)
	{
		throw Debugging.todo();
	}
}
