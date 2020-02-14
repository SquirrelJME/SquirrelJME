// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.uart;

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
		super();
		throw new todo.TODO();
	}
	
	public UARTEvent(UART __a, int __b, long __c, int __d)
	{
		super();
		throw new todo.TODO();
	}
	
	public int getID()
	{
		throw new todo.TODO();
	}
}


