// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.adc;

import cc.squirreljme.runtime.cldc.debug.Debugging;
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
		throw Debugging.todo();
	}
	
	public MonitoringEvent(ADCChannel __a, int __b, int __c, long __d, int 
		__e)
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


