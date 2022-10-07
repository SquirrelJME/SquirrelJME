// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.counter;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import jdk.dio.DeviceEvent;

public class CountingEvent
	extends DeviceEvent<PulseCounter>
{
	public static final int INTERVAL_EXPIRED =
		1;
	
	public static final int TERMINAL_VALUE_REACHED =
		0;
	
	public CountingEvent(PulseCounter __a, int __b, int __c, long __d)
	{
		throw Debugging.todo();
	}
	
	public CountingEvent(PulseCounter __a, int __b, int __c, long __d, long 
		__e, int __f)
	{
		throw Debugging.todo();
	}
	
	public long getInterval()
	{
		throw Debugging.todo();
	}
	
	public int getType()
	{
		throw Debugging.todo();
	}
	
	public int getValue()
	{
		throw Debugging.todo();
	}
}


