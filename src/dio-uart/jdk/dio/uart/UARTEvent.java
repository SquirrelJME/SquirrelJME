// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
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
		throw new Error("TODO");
	}
	
	public UARTEvent(UART __a, int __b, long __c, int __d)
	{
		super();
		throw new Error("TODO");
	}
	
	public int getID()
	{
		throw new Error("TODO");
	}
}


