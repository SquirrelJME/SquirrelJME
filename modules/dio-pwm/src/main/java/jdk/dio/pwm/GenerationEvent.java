// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.pwm;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import jdk.dio.DeviceEvent;

public class GenerationEvent
	extends DeviceEvent<PWMChannel>
{
	@Api
	public GenerationEvent(PWMChannel __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public GenerationEvent(PWMChannel __a, int __b, long __c, int __d)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getPulseCount()
	{
		throw Debugging.todo();
	}
}


