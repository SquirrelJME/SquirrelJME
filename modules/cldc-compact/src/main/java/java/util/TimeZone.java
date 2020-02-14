// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

public abstract class TimeZone
	implements Cloneable
{
	public static final int LONG =
		1;
	
	public static final int SHORT =
		0;
	
	public TimeZone()
	{
		super();
		throw new todo.TODO();
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
		throw new todo.TODO();
	}
	
	public int getDSTSavings()
	{
		throw new todo.TODO();
	}
	
	public final String getDisplayName()
	{
		throw new todo.TODO();
	}
	
	public final String getDisplayName(boolean __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public String getID()
	{
		throw new todo.TODO();
	}
	
	public int getOffset(long __a)
	{
		throw new todo.TODO();
	}
	
	public boolean hasSameRules(TimeZone __a)
	{
		throw new todo.TODO();
	}
	
	public void setID(String __a)
	{
		throw new todo.TODO();
	}
	
	public static String[] getAvailableIDs(int __a)
	{
		synchronized (TimeZone.class)
		{
			throw new todo.TODO();
		}
	}
	
	public static String[] getAvailableIDs()
	{
		synchronized (TimeZone.class)
		{
			throw new todo.TODO();
		}
	}
	
	public static TimeZone getDefault()
	{
		throw new todo.TODO();
	}
	
	public static TimeZone getTimeZone(String __a)
	{
		synchronized (TimeZone.class)
		{
			throw new todo.TODO();
		}
	}
	
	public static void setDefault(TimeZone __a)
	{
		throw new todo.TODO();
	}
}

