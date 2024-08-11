// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.j_phone.system;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public class DeviceControl
{
	/** The device control instance. */
	private static volatile DeviceControl _INSTANCE;
	
	@Api
	public int getDeviceState(int __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the default device control.
	 *
	 * @return The default device control.
	 * @since 2024/08/04
	 */
	@Api
	public static DeviceControl getDefaultDeviceControl()
	{
		synchronized (DeviceControl.class)
		{
			// Use pre-existing one?
			DeviceControl result = DeviceControl._INSTANCE;
			if (result != null)
				return result;
			
			// Set up a new one
			result = new __DefaultDeviceControl__();
			DeviceControl._INSTANCE = result;
			return result;
		}
	}
}
