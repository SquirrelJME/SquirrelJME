// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.gpio;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import jdk.dio.DeviceEvent;

@Api
public class PinEvent
	extends DeviceEvent<GPIOPin>
{
	@Api
	public PinEvent(GPIOPin __a, boolean __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public PinEvent(GPIOPin __a, boolean __b, long __c, int __d)
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean getValue()
	{
		throw Debugging.todo();
	}
}


