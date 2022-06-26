// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.uart;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import jdk.dio.DeviceEvent;

public class UARTEvent
	extends DeviceEvent<UART>
{
	public static final int BREAK_INTERRUPT =
		4;
	
	public static final int FRAMING_ERROR =
		16;
	
	public static final int INPUT_BUFFER_OVERRUN =
		1;
	
	public static final int INPUT_DATA_AVAILABLE =
		0;
	
	public static final int OUTPUT_BUFFER_EMPTY =
		2;
	
	public static final int PARITY_ERROR =
		8;
	
	public UARTEvent(UART __a, int __b)
	{
		throw Debugging.todo();
	}
	
	public UARTEvent(UART __a, int __b, long __c, int __d)
	{
		throw Debugging.todo();
	}
	
	public int getID()
	{
		throw Debugging.todo();
	}
}


