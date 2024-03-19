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
import java.util.Random;
import java.util.TimeZone;
import net.multiphasicapps.tac.TestConsumer;

/**
 * Tests recomposing calendars but to a fuller extent.
 *
 * @since 2024/02/02
 */
public class TestCalendarRecomposeFull
	extends TestConsumer<String>
{
	/** The number of rounds to execute. */
	public static final int NUM_ROUNDS =
		200;
	
	/**
	 * {@inheritDoc}
	 * @since 2024/02/02
	 */
	@Override
	public void test(String __zone)
	{
		TimeZone zone = TimeZone.getTimeZone(__zone);
		
		// Failure counts per field
		int[] failCount = new int[Calendar.FIELD_COUNT];
		
		// Perform a number of rounds
		Random rand = new Random(0x123456789L);
		for (int i = 0; i < TestCalendarRecomposeFull.NUM_ROUNDS; i++)
		{
			// Unlike the other test, the original milliseconds should not
			// get lost
			long millis = rand.nextLong();
			
			// Setup initial calendar
			Calendar init = Calendar.getInstance(zone);
			init.clear();
			init.setTimeInMillis(millis);
			
			// Copy all field values over, note that some fields cannot be
			// set at all
			Calendar redo = Calendar.getInstance(zone);
			redo.clear();
			for (int field = 0; field < Calendar.FIELD_COUNT; field++)
				try
				{
					redo.set(field, init.get(field));
				}
				catch (Throwable __t)
				{
					failCount[field]++;
				}
			
			// Unlike the other test, these values should be exactly the same
			this.secondary("" + millis,
				redo.getTimeInMillis());
		}
		
		// Track failure counts, there should not be any
		this.secondary("fails", failCount);
	}
}
