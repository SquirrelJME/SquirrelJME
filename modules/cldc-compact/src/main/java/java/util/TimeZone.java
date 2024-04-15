// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.time.UTCTimeZone;

@Api
public abstract class TimeZone
	implements Cloneable
{
	@Api
	public static final int LONG =
		1;
	
	@Api
	public static final int SHORT =
		0;
	
	@Api
	public TimeZone()
	{
	}
	
	@Api
	public abstract int getOffset(int __a, int __b, int __c, int __d, int __e
		, int __f);
	
	@Api
	public abstract int getRawOffset();
	
	@Api
	public abstract boolean inDaylightTime(Date __a);
	
	@Api
	public abstract void setRawOffset(int __a);
	
	@Api
	public abstract boolean useDaylightTime();
	
	@Override
	public Object clone()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getDSTSavings()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final String getDisplayName()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final String getDisplayName(boolean __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getID()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getOffset(long __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean hasSameRules(TimeZone __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setID(String __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static String[] getAvailableIDs(int __a)
	{
		synchronized (TimeZone.class)
		{
			throw Debugging.todo();
		}
	}
	
	@Api
	public static String[] getAvailableIDs()
	{
		synchronized (TimeZone.class)
		{
			throw Debugging.todo();
		}
	}
	
	/**
	 * Returns the default time zone.
	 *
	 * @return The default timezone.
	 * @since 2024/01/30
	 */
	@Api
	public static TimeZone getDefault()
	{
		// TODO
		Debugging.todoNote("TimeZone.getDefault()");
		
		return new UTCTimeZone();
	}
	
	/**
	 * Returns the time zone for the given ID, if unknown this will return
	 * the UTC timezone.
	 *
	 * @param __id The ID of the timezone to get.
	 * @return The specified timezone or UTC.
	 * @since 2024/01/30
	 */
	@Api
	public static TimeZone getTimeZone(String __id)
	{
		// TODO
		Debugging.todoNote("TimeZone.getTimeZone(%s)", __id);
		
		return new UTCTimeZone();
	}
	
	@Api
	public static void setDefault(TimeZone __a)
	{
		throw Debugging.todo();
	}
}

