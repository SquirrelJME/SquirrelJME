// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio;


import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public abstract class DeviceEvent<P extends Device<? super P>>
{
	@Api
	protected int count;
	
	@Api
	protected P device;
	
	@Api
	protected long lastTimeStamp;
	
	@Api
	protected int lastTimeStampMicros;
	
	@Api
	protected long timeStamp;
	
	@Api
	protected int timeStampMicros;
	
	@Api
	public DeviceEvent()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final int getCount()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final P getDevice()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final long getLastTimeStamp()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final int getLastTimeStampMicros()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final long getTimeStamp()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final int getTimeStampMicros()
	{
		throw Debugging.todo();
	}
}


