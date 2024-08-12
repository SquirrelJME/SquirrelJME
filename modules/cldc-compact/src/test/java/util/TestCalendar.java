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
import net.multiphasicapps.tac.InvalidTestParameterException;
import net.multiphasicapps.tac.TestConsumer;

/**
 * Tests the calendar functionality.
 *
 * @since 2024/02/02
 */
public class TestCalendar
	extends TestConsumer<String>
{
	/**
	 * {@inheritDoc}
	 * @since 2024/02/02
	 */
	@Override
	public void test(String __zoneMillis)
		throws Throwable
	{
		// Split
		int atPos = __zoneMillis.indexOf('@');
		if (atPos < 0)
			throw new InvalidTestParameterException("Expected zone@millis");
		
		// Load in date and time
		TimeZone zone = TimeZone.getTimeZone(
			__zoneMillis.substring(0, atPos));
		Calendar base = Calendar.getInstance(zone);
		long millis = Long.parseLong(
			__zoneMillis.substring(atPos + 1));
		base.setTimeInMillis(millis);
		
		// Calendar specific fixed values
		this.secondary("firstDayOfWeek",
			base.getFirstDayOfWeek());
		this.secondary("minimalDaysInFirstWeek",
			base.getMinimalDaysInFirstWeek());
		this.secondary("lenient",
			base.isLenient());
		
		// Perform tests with 10 added/removed to each field individually
		for (int field = 0; field < Calendar.FIELD_COUNT; field++)
		{
			// The key for this specific field
			String fieldKey = TestCalendar.__fieldName(field);
			
			// Field specific fixed values
			this.secondary(fieldKey + "-actualMax",
				base.getActualMaximum(field));
			this.secondary(fieldKey + "-actualMin",
				base.getActualMinimum(field));
			this.secondary(fieldKey + "-greatestMin",
				base.getGreatestMinimum(field));
			this.secondary(fieldKey + "-leastMax",
				base.getLeastMaximum(field));
			
			// Roll or add?
			for (boolean doRoll : new boolean[]{false, true})
			{
				// Base key name
				String baseKey = fieldKey + (doRoll ? "-roll" : "-add");
				
				// Subtract/Add time from it
				for (int add = -10; add <= 10; add += 10)
				{
					// Setup new calendar instance from the base
					Calendar adjust = Calendar.getInstance(zone);
					adjust.setTimeInMillis(millis);
					
					// Determine the key used
					String key = baseKey + "-" + add;
					
					// Add to the field
					if (add != 0)
						try
						{
							if (doRoll)
								adjust.roll(field, add);
							else
								adjust.add(field, add);
						}
						
						// If it cannot be added to, we skip it
						catch (Throwable __t)
						{
							this.secondary(key, __t);
							continue;
						}
					
					// Read from the field
					this.secondary(key + "-get",
						adjust.get(field));
					this.secondary(key + "-millis",
						base.getTimeInMillis());
					
					// Before for each
					this.secondary(key + "-before-ab",
						adjust.before(base));
					this.secondary(key + "-before-ba",
						base.after(adjust));
					
					// After for each
					this.secondary(key + "-after-ab",
						adjust.after(base));
					this.secondary(key + "-after-ba",
						base.after(adjust));
					
					// Compare between the two
					this.secondary(key + "-compare-ab",
						adjust.compareTo(base));
					this.secondary(key + "-compare-ba",
						base.compareTo(adjust));
					
					// Equal between the two
					this.secondary(key + "-equal-ab",
						adjust.equals(base));
					this.secondary(key + "-equal-ba",
						base.equals(adjust));
				}
			}
		}
	}
	
	/**
	 * Determines the field name.
	 *
	 * @param __id The field ID.
	 * @return The resultant field name.
	 * @since 2024/02/02
	 */
	private static String __fieldName(int __id)
	{
		switch (__id)
		{
			case Calendar.HOUR: return "hour";
			case Calendar.HOUR_OF_DAY: return "hourOfDay";
			case Calendar.MINUTE: return "minute";
			case Calendar.SECOND: return "second";
			case Calendar.MILLISECOND: return "millisecond";
			case Calendar.AM_PM: return "AM_PM";
			
			case Calendar.YEAR: return "year";
			case Calendar.MONTH: return "month";
			
			case Calendar.WEEK_OF_MONTH: return "weekOfMonth";
			case Calendar.WEEK_OF_YEAR: return "weekOfYear";
			
			case Calendar.DAY_OF_MONTH: return "dayOfMonth";
			case Calendar.DAY_OF_WEEK: return "dayOfWeek";
			case Calendar.DAY_OF_WEEK_IN_MONTH: return "dayOfWeekInMonth";
			case Calendar.DAY_OF_YEAR: return "dayOfYear";
			
			case Calendar.ERA: return "era";
			
			case Calendar.DST_OFFSET: return "dstOffset";
			case Calendar.ZONE_OFFSET: return "zoneOffset";
		}
		
		return "unknown" + __id;
	}
}
