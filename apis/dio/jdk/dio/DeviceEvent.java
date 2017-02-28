// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio;


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
		super();
		throw new todo.TODO();
	}
	
	public final int getCount()
	{
		throw new todo.TODO();
	}
	
	public final P getDevice()
	{
		throw new todo.TODO();
	}
	
	public final long getLastTimeStamp()
	{
		throw new todo.TODO();
	}
	
	public final int getLastTimeStampMicros()
	{
		throw new todo.TODO();
	}
	
	public final long getTimeStamp()
	{
		throw new todo.TODO();
	}
	
	public final int getTimeStampMicros()
	{
		throw new todo.TODO();
	}
}


