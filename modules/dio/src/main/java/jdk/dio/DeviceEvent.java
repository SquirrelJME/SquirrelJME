// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio;


import cc.squirreljme.runtime.cldc.debug.Debugging;

public abstract class DeviceEvent<P extends Device<? super P>>
{
	protected int count;
	
	protected P device;
	
	protected long lastTimeStamp;
	
	protected int lastTimeStampMicros;
	
	protected long timeStamp;
	
	protected int timeStampMicros;
	
	public DeviceEvent()
	{
		throw Debugging.todo();
	}
	
	public final int getCount()
	{
		throw Debugging.todo();
	}
	
	public final P getDevice()
	{
		throw Debugging.todo();
	}
	
	public final long getLastTimeStamp()
	{
		throw Debugging.todo();
	}
	
	public final int getLastTimeStampMicros()
	{
		throw Debugging.todo();
	}
	
	public final long getTimeStamp()
	{
		throw Debugging.todo();
	}
	
	public final int getTimeStampMicros()
	{
		throw Debugging.todo();
	}
}


