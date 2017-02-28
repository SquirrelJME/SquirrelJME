// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.adc;

import jdk.dio.DeviceEvent;

public class MonitoringEvent
	extends DeviceEvent<ADCChannel>
{
	public static final int BACK_TO_RANGE =
		1;
	
	public static final int OUT_OF_RANGE =
		0;
	
	public MonitoringEvent(ADCChannel __a, int __b, int __c)
	{
		super();
		throw new todo.TODO();
	}
	
	public MonitoringEvent(ADCChannel __a, int __b, int __c, long __d, int 
		__e)
	{
		super();
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


