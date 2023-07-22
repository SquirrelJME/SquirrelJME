// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.vodafone.v10.system.device;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

public class DeviceControl
{
	@Api
	public static final int BATTERY = 1;
	
	@Api
	public static final int FIELD_INTENSITY = 2;
	
	@Api
	public static final int KEY_STATE = 3;
	
	@Api
	public static final int VIBRATION = 4;
	
	@Api
	public static final int BACK_LIGHT = 5;
	
	@Api
	public static final int EIGHT_DIRECTIONS = 6;
	
	@Api
	public static final DeviceControl getDefaultDeviceControl()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getDeviceState(int var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isDeviceActive(int var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean setDeviceActive(int var1, boolean var2)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void blink(int var1, int var2, int var3)
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean setKeyRepeatState(int var1, boolean var2)
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean getKeyRepeatState(int var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void setMailListener(MailListener var0)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void setScheduledAlarmListener(ScheduledAlarmListener var0)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void setTelephonyListener(TelephonyListener var0)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void setRingStateListener(RingStateListener var0)
	{
		throw Debugging.todo();
	}
}
