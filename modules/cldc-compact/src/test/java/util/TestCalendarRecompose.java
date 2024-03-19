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
 * Tests recomposing calendars.
 *
 * @since 2024/02/02
 */
public class TestCalendarRecompose
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
		
		// Perform a number of rounds
		Random rand = new Random(0x123456789L);
		for (int i = 0; i < TestCalendarRecompose.NUM_ROUNDS; i++)
		{
			// Note that the input milliseconds will be lost, however we do
			// want to keep it around as we could better rebuild from it...
			// we are losing information due to the lack of era as well
			long millis = rand.nextLong();
			
			// Setup initial calendar
			Calendar init = Calendar.getInstance(zone);
			init.clear();
			init.setTimeInMillis(millis);
			
			// Set the calendar to the fields of the start one
			// Note clearing has to be done because other fields will be
			// retained such as the milliseconds which means the test will
			// not result in the same values depending on when it is run
			Calendar redo = Calendar.getInstance(zone);
			redo.clear();
			redo.set(init.get(Calendar.YEAR),
				init.get(Calendar.MONTH),
				init.get(Calendar.DAY_OF_MONTH),
				init.get(Calendar.HOUR_OF_DAY),
				init.get(Calendar.MINUTE),
				init.get(Calendar.SECOND));
			
			// Note that the resultant value is not the same as the input
			// because negative values with different eras cannot be rebuilt
			// so in essence all resultant values here will be positive. This
			// can just be used to test those situations.
			this.secondary("" + millis,
				redo.getTimeInMillis());
		}
	}
}
