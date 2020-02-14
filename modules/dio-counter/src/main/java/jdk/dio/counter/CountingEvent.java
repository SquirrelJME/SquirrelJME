// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.counter;

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
		super();
		throw new todo.TODO();
	}
	
	public CountingEvent(PulseCounter __a, int __b, int __c, long __d, long 
		__e, int __f)
	{
		super();
		throw new todo.TODO();
	}
	
	public long getInterval()
	{
		throw new todo.TODO();
	}
	
	public int getType()
	{
		throw new todo.TODO();
	}
	
	public int getValue()
	{
		throw new todo.TODO();
	}
}


