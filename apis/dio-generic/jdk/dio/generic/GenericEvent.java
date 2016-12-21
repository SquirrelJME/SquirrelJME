// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.generic;

import jdk.dio.DeviceEvent;

public class GenericEvent
	extends DeviceEvent<GenericDevice>
{
	public static final int INPUT_BUFFER_OVERRUN =
		1;
	
	public static final int INPUT_DATA_AVAILABLE =
		0;
	
	public static final int OUTPUT_BUFFER_EMPTY =
		2;
	
	public GenericEvent(GenericDevice __a, int __b)
	{
		super();
		throw new Error("TODO");
	}
	
	public GenericEvent(GenericDevice __a, int __b, long __c, int __d)
	{
		super();
		throw new Error("TODO");
	}
	
	public int getID()
	{
		throw new Error("TODO");
	}
}


