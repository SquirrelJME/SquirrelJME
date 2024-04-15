// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.counter;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import jdk.dio.DeviceEvent;

@Api
public class CountingEvent
	extends DeviceEvent<PulseCounter>
{
	@Api
	public static final int INTERVAL_EXPIRED =
		1;
	
	@Api
	public static final int TERMINAL_VALUE_REACHED =
		0;
	
	@Api
	public CountingEvent(PulseCounter __a, int __b, int __c, long __d)
	{
		throw Debugging.todo();
	}
	
	@Api
	public CountingEvent(PulseCounter __a, int __b, int __c, long __d, long 
		__e, int __f)
	{
		throw Debugging.todo();
	}
	
	@Api
	public long getInterval()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getType()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getValue()
	{
		throw Debugging.todo();
	}
}


