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

/**
 * This represents a simplified time and date.
 *
 * The epoch is the same as {@link System#currentTimeMillis()} and is from
 * January 1, 1970 at 00:00 GMT.
 *
 * @since 2019/12/25
 */
public class Date
	implements Cloneable, Comparable<Date>
{
	/** The current Java time. */
	private transient long _javatime;
	
	/**
	 * Initializes this date at the current system time.
	 *
	 * @since 2019/12/25
	 */
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
	public Date(long __time)
	{
		this._javatime = __time;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/25
	 */
	@Override
	public Object clone()
	{
		return new Date(((Date)this)._javatime);
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
		
		long a = this._javatime,
			b = __b._javatime;
		
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
		
		if (!(this instanceof __o))
			return false;
		
		return this._javatime == ((Date)__o)._javatime;
	}
	
	/**
	 * Returns the number of milliseconds since the epoch.
	 *
	 * @return The number of milliseconds since the epoch.
	 * @since 2019/12/25
	 */
	public long getTime()
	{
		return this._javatime;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/25
	 */
	@Override
	public int hashCode()
	{
		long javatime = this._javatime;
		return (int)(javatime ^ (javatime >>> 32));
	}
	
	/**
	 * Sets the current time.
	 *
	 * @param __v The time to set.
	 * @since 2019/12/25
	 */
	public void setTime(long __v)
	{
		this._javatime = __v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/25
	 */
	@Override
	public String toString()
	{
		// "dow mon dd hh:mm:ss zzz yyyy"
		
		throw new todo.TODO();
	}
}

