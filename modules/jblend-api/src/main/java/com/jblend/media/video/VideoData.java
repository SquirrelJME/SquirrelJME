// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.media.video;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import com.jblend.media.MediaData;
import java.io.IOException;

public class VideoData
	extends MediaData
{
	@Api
	public static final String type = "VIDEO";
	
	public VideoData()
	{
	}
	
	public VideoData(String var1)
		throws IOException
	{
	}
	
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
