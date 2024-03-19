// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.time;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Date;
import java.util.TimeZone;

/**
 * A time zone that can only represent UTC.
 *
 * @since 2024/01/30
 */
public class UTCTimeZone
	extends TimeZone
{
	/**
	 * Initializes the base UTC time zone.
	 *
	 * @since 2024/02/02
	 */
	public UTCTimeZone()
	{
		this.setID("UTC");
	}
	
	@Override
	public int getOffset(int __era, int __year, int __month, int __day,
		int __dayOfWeek, int __dayMillis)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/02/02
	 */
	@Override
	public int getRawOffset()
	{
		// Always zero for UTC
		return 0;
	}
	
	@Override
	public boolean inDaylightTime(Date __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public void setRawOffset(int __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/02/02
	 */
	@Override
	public boolean useDaylightTime()
	{
		// UTC has no time zone
		return false;
	}
}
