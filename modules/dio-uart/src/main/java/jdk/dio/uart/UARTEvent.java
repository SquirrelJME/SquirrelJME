// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.uart;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import jdk.dio.DeviceEvent;

@Api
public class UARTEvent
	extends DeviceEvent<UART>
{
	@Api
	public static final int BREAK_INTERRUPT =
		4;
	
	@Api
	public static final int FRAMING_ERROR =
		16;
	
	@Api
	public static final int INPUT_BUFFER_OVERRUN =
		1;
	
	@Api
	public static final int INPUT_DATA_AVAILABLE =
		0;
	
	@Api
	public static final int OUTPUT_BUFFER_EMPTY =
		2;
	
	@Api
	public static final int PARITY_ERROR =
		8;
	
	@Api
	public UARTEvent(UART __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public UARTEvent(UART __a, int __b, long __c, int __d)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getID()
	{
		throw Debugging.todo();
	}
}


