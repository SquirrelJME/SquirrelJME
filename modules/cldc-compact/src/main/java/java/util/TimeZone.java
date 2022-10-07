// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.debug.Debugging;

public abstract class TimeZone
	implements Cloneable
{
	public static final int LONG =
		1;
	
	public static final int SHORT =
		0;
	
	public TimeZone()
	{
		throw Debugging.todo();
	}
	
	public abstract int getOffset(int __a, int __b, int __c, int __d, int __e
		, int __f);
	
	public abstract int getRawOffset();
	
	public abstract boolean inDaylightTime(Date __a);
	
	public abstract void setRawOffset(int __a);
	
	public abstract boolean useDaylightTime();
	
	@Override
	public Object clone()
	{
		throw Debugging.todo();
	}
	
	public int getDSTSavings()
	{
		throw Debugging.todo();
	}
	
	public final String getDisplayName()
	{
		throw Debugging.todo();
	}
	
	public final String getDisplayName(boolean __a, int __b)
	{
		throw Debugging.todo();
	}
	
	public String getID()
	{
		throw Debugging.todo();
	}
	
	public int getOffset(long __a)
	{
		throw Debugging.todo();
	}
	
	public boolean hasSameRules(TimeZone __a)
	{
		throw Debugging.todo();
	}
	
	public void setID(String __a)
	{
		throw Debugging.todo();
	}
	
	public static String[] getAvailableIDs(int __a)
	{
		synchronized (TimeZone.class)
		{
			throw Debugging.todo();
		}
	}
	
	public static String[] getAvailableIDs()
	{
		synchronized (TimeZone.class)
		{
			throw Debugging.todo();
		}
	}
	
	public static TimeZone getDefault()
	{
		throw Debugging.todo();
	}
	
	public static TimeZone getTimeZone(String __a)
	{
		synchronized (TimeZone.class)
		{
			throw Debugging.todo();
		}
	}
	
	public static void setDefault(TimeZone __a)
	{
		throw Debugging.todo();
	}
}

