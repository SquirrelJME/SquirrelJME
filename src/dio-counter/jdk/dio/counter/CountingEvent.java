// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
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
		throw new Error("TODO");
	}
	
	public CountingEvent(PulseCounter __a, int __b, int __c, long __d, long 
		__e, int __f)
	{
		super();
		throw new Error("TODO");
	}
	
	public long getInterval()
	{
		throw new Error("TODO");
	}
	
	public int getType()
	{
		throw new Error("TODO");
	}
	
	public int getValue()
	{
		throw new Error("TODO");
	}
}


