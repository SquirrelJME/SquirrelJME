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
import java.io.IOException;

@Api
public abstract class MediaData
{
	@Api
	public MediaData()
	{
		throw Debugging.todo();
	}
	
	@Api
	public MediaData(String var1)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Api
	public MediaData(byte[] var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public abstract String getMediaType();
	
	@Api
	public abstract void setData(byte[] var1);
}
