// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util;

import java.util.Calendar;
import java.util.TimeZone;
import net.multiphasicapps.tac.TestConsumer;

/**
 * Tests that the calendar is initialized to some value.
 *
 * @since 2024/02/02
 */
public class TestCalendarInitTime
	extends TestConsumer<String>
{
	/**
	 * {@inheritDoc}
	 * @since 2024/02/02
	 */
	@Override
	public void test(String __zone)
	{
		// The created calendar instance should be at or after this point
		long now = System.currentTimeMillis();
		
		// Get calendar instance
		TimeZone zone = TimeZone.getTimeZone(__zone);
		Calendar cal = Calendar.getInstance(zone);
		
		// It should have a time that is not zero
		this.secondary("nonzero",
			cal.getTimeInMillis() != 0);
		
		// And should be at or after the millisecond time
		this.secondary("after",
			cal.getTimeInMillis() >= now);
	}
}
