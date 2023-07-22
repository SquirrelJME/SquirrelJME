// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.media.audio;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import com.jblend.media.MediaData;
import java.io.IOException;

@Api
public class AudioData
	extends MediaData
{
	@Api
	public static final String type = "AUDIO";
	
	@Api
	public AudioData()
	{
		throw Debugging.todo();
	}
	
	@Api
	public AudioData(String var1)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Api
	public AudioData(byte[] var1)
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
