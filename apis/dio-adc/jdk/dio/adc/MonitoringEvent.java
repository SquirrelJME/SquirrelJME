// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
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
		throw new Error("TODO");
	}
	
	public MonitoringEvent(ADCChannel __a, int __b, int __c, long __d, int 
		__e)
	{
		super();
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


