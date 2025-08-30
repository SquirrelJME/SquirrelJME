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
	
	/** The ID of the time zone. */
	private volatile String _id;
	
	@Api
	public TimeZone()
	{
	}
	
	@Api
	public abstract int getOffset(int __era, int __year, int __month,
		int __day, int __dayOfWeek, int __dayMillis);
	
	/**
	 * Returns the current raw UTC offset in milliseconds. Note that an
	 * implementation may allow for historical setting of time zone offsets,
	 * if such an implementation exists then {@link #setRawOffset(int)} may
	 * change that offset accordingly.
	 *
	 * @return The current raw UTC offset in milliseconds.
	 * @since 2024/02/02
	 */
	@Api
	public abstract int getRawOffset();
	
	@Api
	public abstract boolean inDaylightTime(Date __a);
	
	@Api
	public abstract void setRawOffset(int __a);
	
	/**
	 * Does this time zone use daylight savings time?
	 *
	 * @return If daylight savings time is used.
	 * @since 2024/02/02
	 */
	@Api
	public abstract boolean useDaylightTime();
	
	@Override
	public Object clone()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the number of milliseconds to offset to get the local time
	 * when daylights savings time is in use. This is replaced accordingly
	 * as needed by implementations of this class.
	 * 
	 * By default, if {@link #useDaylightTime()} is {@code true} then this
	 * will return one hour in milliseconds ({@code 3600000}), otherwise
	 * the value returned will be zero.
	 *
	 * @return The number of milliseconds to offset when daylight savings time
	 * is in effect.
	 * @since 2024/02/02
	 */
	@Api
	public int getDSTSavings()
	{
		if (this.useDaylightTime())
			return 3600000;
		return 0;
	}
	
	/**
	 * Returns the display name of the time zone.
	 *
	 * @return The display name of the time zone.
	 * @since 2024/02/02
	 */
	@Api
	public final String getDisplayName()
	{
		return this.getDisplayName(false, TimeZone.LONG);
	}
	
	/**
	 * Returns the display name of this time zone.
	 *
	 * @param __dst If {@code true} then the name for the time zone in daylight
	 * savings time should be used, if the time zone does not have daylight
	 * savings time then this value is ignored.
	 * @param __shortOrLong Either {@link TimeZone#LONG} or
	 * {@link TimeZone#SHORT}.
	 * @return The display name of the given time zone.
	 * @since 2024/02/02
	 */
	@Api
	public final String getDisplayName(boolean __dst, int __shortOrLong)
	{
		// If we are requesting a DST time zone because the ID itself can be
		// mixed and matched between countries, CST being both Central
		// Standard Time in the US and CST being China Standard Time in
		// China, we need to use the offset to determine which ID maps to what
		String id;
		int rawOffset = this.getRawOffset();
		if (__dst)
			id = TimeZone.__dstId(this.getID(), rawOffset);
		else
			id = this.getID();
		
		// Return either the ID directly or its long name
		if (__shortOrLong == TimeZone.SHORT)
			return id;
		return TimeZone.__longName(id, rawOffset);
	}
	
	/**
	 * Returns the ID of this time zone.
	 *
	 * @return The time zone ID.
	 * @since 2024/02/02
	 */
	@Api
	public String getID()
	{
		return this._id;
	}
	
	/**
	 * Returns the offset from UTC for the given UTC milliseconds since the
	 * Unix epoch. This is used to get the local time if daylights savings
	 * time is in effect. If the implementation supports historical daylights
	 * savings time changes, then this will use as such accordingly.
	 *
	 * @param __millis The UTC milliseconds since the Unix epoch.
	 * @return The offset for the given date.
	 * @since 2024/02/02
	 */
	@Api
	public int getOffset(long __millis)
	{
		// Initialize calendar to get the date
		Calendar cal = Calendar.getInstance(this);
		cal.clear();
		cal.setTimeInMillis(__millis);
		
		// Determine the amount of milliseconds in the day
		int dayMillis = (cal.get(Calendar.HOUR_OF_DAY) * 1000 * 60 * 60) +
			(cal.get(Calendar.MINUTE) * 1000 * 60) +
			(cal.get(Calendar.SECOND) * 1000) +
			cal.get(Calendar.MILLISECOND);
		
		// Forward call
		return this.getOffset(
			cal.get(Calendar.ERA),
			cal.get(Calendar.YEAR),
			cal.get(Calendar.MONTH),
			cal.get(Calendar.DAY_OF_MONTH),
			cal.get(Calendar.DAY_OF_WEEK),
			dayMillis);
	}
	
	@Api
	public boolean hasSameRules(TimeZone __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets the ID of the given time zone. Note that this does not have
	 * effect on the actual time zone information itself as this is mostly
	 * used to determine what {@link #getDisplayName()} returns based on the
	 * ID.
	 *
	 * @param __id The new ID to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/02/02
	 */
	@Api
	public void setID(String __id)
		throws NullPointerException
	{
		if (__id == null)
			throw new NullPointerException("NARG");
		
		this._id = __id;
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
	
	/**
	 * Maps the given ID and offset to a daylight savings time ID.
	 *
	 * @param __id The ID to map.
	 * @param __rawOffset The offset of the time zone.
	 * @return The resultant ID.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/02/02
	 */
	private static String __dstId(String __id, int __rawOffset)
		throws NullPointerException
	{
		if (__id == null)
			throw new NullPointerException("NARG");
		
		// Depending on the passed ID and the offset...
		switch (__id)
		{
				// Assume there is no DST
			default:
				return __id;
		}
	}
	
	/**
	 * Returns the long name of the given time zone based on its ID and
	 * raw offset.
	 *
	 * @param __id The time zone ID.
	 * @param __rawOffset The raw offset of the time zone.
	 * @return The resultant long display name of the time zone.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/02/02
	 */
	private static String __longName(String __id, int __rawOffset)
		throws NullPointerException
	{
		if (__id == null)
			throw new NullPointerException("NARG");
		
		switch (__id)
		{
			case "UTC":		return "Universal Coordinated Time";
			
				// Unknown?
			default:
				return String.format("Time Zone %s%c%dms",
					__id, (__rawOffset < 0 ? '-' : '+'), __rawOffset);
		}
	}
}

