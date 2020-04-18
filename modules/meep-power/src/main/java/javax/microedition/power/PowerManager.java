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
import javax.microedition.event.EventManager;

public class PowerManager
	extends EventManager
{
	protected PowerManager()
	{
		throw Debugging.todo();
	}
	
	public boolean enableRadio(boolean var1, boolean var2)
		throws PowerManagerException, SecurityException
	{
		throw Debugging.todo();
	}
	
	public int getBatteryLevel()
	{
		throw Debugging.todo();
	}
	
	public int getEstimatedTimeRemaining()
	{
		throw Debugging.todo();
	}
	
	public int getPowerState()
	{
		throw Debugging.todo();
	}
	
	public boolean isUsingExternalPower()
	{
		throw Debugging.todo();
	}
	
	public void rebootDevice(boolean var1)
		throws PowerManagerException, SecurityException
	{
		throw Debugging.todo();
	}
	
	public void setPowerState(int var1, boolean var2)
		throws PowerManagerException, SecurityException
	{
		throw Debugging.todo();
	}
	
	public static PowerManager getInstance()
	{
		throw Debugging.todo();
	}
}
