// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.power;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.event.Event;

public class PowerStateEvent
	extends Event
{
	public static final String BATTERY_LEVEL = "BATTERY_LEVEL";
	
	public static final String POWER_ALERT = "POWER_ALERT";
	
	public static final int POWER_ALERT_BATTERY_CRITICAL = 1;
	
	public static final int POWER_ALERT_BATTERY_LOW = 2;
	
	public static final int POWER_ALERT_BATTERY_NORMAL = 3;
	
	public static final int POWER_ALERT_CALL_TERMINATION = 4;
	
	public static final int POWER_ALERT_EXTERNAL_POWER_SOURCE_CHANGE = 5;
	
	public static final int POWER_ALERT_IR_TERMINATION = 6;
	
	public static final int POWER_ALERT_NETWORK_TERMINATION = 7;
	
	public static final String POWER_STATE = "POWER_STATE_CHANGE";
	
	public static final int POWER_STATE_FULL_POWER = 1;
	
	public static final int POWER_STATE_OFF = 0;
	
	public static final int POWER_STATE_REGULATED_HIGH_POWER = 3;
	
	public static final int POWER_STATE_REGULATED_LOW_POWER = 2;
	
	public static final int POWER_STATE_SLEEP = 4;
	
	public static final int POWER_STATE_SUSPEND = 5;
	
	protected PowerStateEvent(int var1, int var2, boolean var3)
	{
		super(null, 0, null, null);
		
		throw Debugging.todo();
	}
	
	public int getNewState()
	{
		throw Debugging.todo();
	}
	
	public int getPrevState()
	{
		throw Debugging.todo();
	}
	
	public boolean isUrgent()
	{
		throw Debugging.todo();
	}
}
