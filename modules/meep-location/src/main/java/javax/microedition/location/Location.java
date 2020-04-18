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

public class Location
{
	public static final int MTA_ASSISTED = 262144;
	
	public static final int MTA_UNASSISTED = 524288;
	
	public static final int MTE_ANGLEOFARRIVAL = 32;
	
	public static final int MTE_CELLID = 8;
	
	public static final int MTE_SATELLITE = 1;
	
	public static final int MTE_SHORTRANGE = 16;
	
	public static final int MTE_TIMEDIFFERENCE = 2;
	
	public static final int MTE_TIMEOFARRIVAL = 4;
	
	public static final int MTY_NETWORKBASED = 131072;
	
	public static final int MTY_TERMINALBASED = 65536;
	
	protected Location()
	{
		throw Debugging.todo();
	}
	
	public AddressInfo getAddressInfo()
	{
		throw Debugging.todo();
	}
	
	public float getCourse()
	{
		throw Debugging.todo();
	}
	
	public String getExtraInfo(String var1)
	{
		throw Debugging.todo();
	}
	
	public int getLocationMethod()
	{
		throw Debugging.todo();
	}
	
	public QualifiedCoordinates getQualifiedCoordinates()
	{
		throw Debugging.todo();
	}
	
	public float getSpeed()
	{
		throw Debugging.todo();
	}
	
	public long getTimestamp()
	{
		throw Debugging.todo();
	}
	
	public boolean isValid()
	{
		throw Debugging.todo();
	}
}
