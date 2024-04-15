// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.generic;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import jdk.dio.DeviceEvent;

@Api
public class GenericEvent
	extends DeviceEvent<GenericDevice>
{
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
	public GenericEvent(GenericDevice __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public GenericEvent(GenericDevice __a, int __b, long __c, int __d)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getID()
	{
		throw Debugging.todo();
	}
}


