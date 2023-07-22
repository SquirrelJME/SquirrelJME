// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.adc;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import jdk.dio.DeviceEvent;

@Api
public class MonitoringEvent
	extends DeviceEvent<ADCChannel>
{
	@Api
	public static final int BACK_TO_RANGE =
		1;
	
	@Api
	public static final int OUT_OF_RANGE =
		0;
	
	@Api
	public MonitoringEvent(ADCChannel __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public MonitoringEvent(ADCChannel __a, int __b, int __c, long __d, int 
		__e)
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


