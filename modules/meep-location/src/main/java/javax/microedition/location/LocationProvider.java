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

public abstract class LocationProvider
{
	public static final int AVAILABLE = 1;
	
	public static final int OUT_OF_SERVICE = 3;
	
	public static final int TEMPORARILY_UNAVAILABLE = 2;
	
	protected LocationProvider()
	{
		throw Debugging.todo();
	}
	
	public abstract Location getLocation(int var1)
		throws LocationException, InterruptedException;
	
	public abstract int getState();
	
	public abstract void reset();
	
	public abstract void setLocationListener(LocationListener var1, int var2,
		int var3, int var4);
	
	public static void addProximityListener(ProximityListener var0,
		Coordinates var1, float var2)
		throws LocationException
	{
		throw Debugging.todo();
	}
	
	public static LocationProvider getInstance(Criteria var0)
		throws LocationException
	{
		throw Debugging.todo();
	}
	
	public static Location getLastKnownLocation()
	{
		throw Debugging.todo();
	}
	
	public static void removeProximityListener(ProximityListener var0)
	{
		throw Debugging.todo();
	}
}
