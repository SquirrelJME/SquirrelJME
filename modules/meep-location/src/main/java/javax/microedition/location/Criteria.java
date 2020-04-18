// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.location;

import cc.squirreljme.runtime.cldc.debug.Debugging;

public class Criteria
{
	public static final int NO_REQUIREMENT = 0;
	
	public static final int POWER_USAGE_HIGH = 3;
	
	public static final int POWER_USAGE_LOW = 1;
	
	public static final int POWER_USAGE_MEDIUM = 2;
	
	public Criteria()
	{
		throw Debugging.todo();
	}
	
	public int getHorizontalAccuracy()
	{
		throw Debugging.todo();
	}
	
	public int getPreferredPowerConsumption()
	{
		throw Debugging.todo();
	}
	
	public int getPreferredResponseTime()
	{
		throw Debugging.todo();
	}
	
	public int getVerticalAccuracy()
	{
		throw Debugging.todo();
	}
	
	public boolean isAddressInfoRequired()
	{
		throw Debugging.todo();
	}
	
	public boolean isAllowedToCost()
	{
		throw Debugging.todo();
	}
	
	public boolean isAltitudeRequired()
	{
		throw Debugging.todo();
	}
	
	public boolean isSpeedAndCourseRequired()
	{
		throw Debugging.todo();
	}
	
	public void setAddressInfoRequired(boolean var1)
	{
		throw Debugging.todo();
	}
	
	public void setAltitudeRequired(boolean var1)
	{
		throw Debugging.todo();
	}
	
	public void setCostAllowed(boolean var1)
	{
		throw Debugging.todo();
	}
	
	public void setHorizontalAccuracy(int var1)
	{
		throw Debugging.todo();
	}
	
	public void setPreferredPowerConsumption(int var1)
	{
		throw Debugging.todo();
	}
	
	public void setPreferredResponseTime(int var1)
	{
		throw Debugging.todo();
	}
	
	public void setSpeedAndCourseRequired(boolean var1)
	{
		throw Debugging.todo();
	}
	
	public void setVerticalAccuracy(int var1)
	{
		throw Debugging.todo();
	}
}
