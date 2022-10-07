// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.vodafone.v10.system.device;

import cc.squirreljme.runtime.cldc.debug.Debugging;

public class DeviceControl {
    public static final int BATTERY = 1;
    public static final int FIELD_INTENSITY = 2;
    public static final int KEY_STATE = 3;
    public static final int VIBRATION = 4;
    public static final int BACK_LIGHT = 5;
    public static final int EIGHT_DIRECTIONS = 6;

    public static final  DeviceControl getDefaultDeviceControl()
	{
		throw Debugging.todo();
	}

    public int getDeviceState(int var1)
	{
		throw Debugging.todo();
	}

    public boolean isDeviceActive(int var1)
	{
		throw Debugging.todo();
	}

    public boolean setDeviceActive(int var1, boolean var2)
	{
		throw Debugging.todo();
	}

    public void blink(int var1, int var2, int var3)
	{
		throw Debugging.todo();
	}

    public boolean setKeyRepeatState(int var1, boolean var2)
	{
		throw Debugging.todo();
	}

    public boolean getKeyRepeatState(int var1)
	{
		throw Debugging.todo();
	}

    public static  void setMailListener(MailListener var0)
	{
		throw Debugging.todo();
	}

    public static  void setScheduledAlarmListener(ScheduledAlarmListener var0)
	{
		throw Debugging.todo();
	}

    public static  void setTelephonyListener(TelephonyListener var0)
	{
		throw Debugging.todo();
	}

    public static  void setRingStateListener(RingStateListener var0)
	{
		throw Debugging.todo();
	}
}
