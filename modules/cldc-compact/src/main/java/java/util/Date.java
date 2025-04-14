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

/**
 * This represents a simplified time and date.
 *
 * The epoch is the same as {@link System#currentTimeMillis()} and is from
 * January 1, 1970 at 00:00 GMT.
 *
 * @since 2019/12/25
 */
@Api
public class Date
	implements Cloneable, Comparable<Date>
{
	/** The current Java time. */
	private transient long _javaTime;
	
	/**
	 * Initializes this date at the current system time.
	 *
	 * @since 2019/12/25
	 */
	@Api
	public Date()
	{
		this(System.currentTimeMillis());
	}
	
	/**
	 * Initializes the date to the given time.
	 *
	 * @param __time The time to set.
	 * @since 2019/12/25
	 */
	@Api
	public Date(long __time)
	{
		this._javaTime = __time;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/25
	 */
	@Override
	public Object clone()
	{
		return new Date(((Date)this)._javaTime);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/25
	 */
	@Override
	public int compareTo(Date __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		long a = this._javaTime;
		long b = ((Date)__b)._javaTime;
		
		return (a < b ? -1 : (a > b ? 1 : 0));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/25
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof Date))
			return false;
		
		return this._javaTime == ((Date)__o)._javaTime;
	}
	
	/**
	 * Returns the number of milliseconds since the epoch.
	 *
	 * @return The number of milliseconds since the epoch.
	 * @since 2019/12/25
	 */
	@Api
	public long getTime()
	{
		return this._javaTime;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/25
	 */
	@Override
	public int hashCode()
	{
		long javatime = this._javaTime;
		return (int)(javatime ^ (javatime >>> 32));
	}
	
	/**
	 * Sets the current time.
	 *
	 * @param __v The time to set.
	 * @since 2019/12/25
	 */
	@Api
	public void setTime(long __v)
	{
		this._javaTime = __v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/25
	 */
	@Override
	public String toString()
	{
		// "dow mon dd hh:mm:ss zzz yyyy"
		
		throw Debugging.todo();
	}
}

