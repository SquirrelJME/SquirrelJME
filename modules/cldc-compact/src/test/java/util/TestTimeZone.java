// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util;

import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import net.multiphasicapps.tac.TestConsumer;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests {@link TimeZone}.
 *
 * @since 2024/02/02
 */
public class TestTimeZone
	extends TestConsumer<String>
{
	/**
	 * {@inheritDoc}
	 * @since 2024/02/02
	 */
	@Override
	public void test(String __zoneId)
	{
		TimeZone zone = TimeZone.getTimeZone(__zoneId);
		
		// ID
		this.secondary("id",
			zone.getID());
		
		// Display names
		this.secondary("displayName",
			zone.getDisplayName());
		this.secondary("displayNameNoDSTShort",
			zone.getDisplayName(false, TimeZone.SHORT));
		this.secondary("displayNameNoDSTLong",
			zone.getDisplayName(false, TimeZone.LONG));
		this.secondary("displayNameDSTShort",
			zone.getDisplayName(true, TimeZone.SHORT));
		this.secondary("displayNameDSTLong",
			zone.getDisplayName(true, TimeZone.LONG));
		
		// DST
		this.secondary("dstSavings",
			zone.getDSTSavings());
		this.secondary("useDaylightTime",
			zone.useDaylightTime());
		
		// Offset
		this.secondary("rawOffset",
			zone.getRawOffset());
		
		// Specific millisecond times
		Random rand = new Random(1234567890L);
		for (int i = 0; i < 50; i++)
		{
			// Which time will we test at?
			long millis = rand.nextLong();
			
			// Offset from given time
			this.secondary("z-offset-" + millis,
				zone.getOffset(millis));
			
			// In DST?
			Date date = new Date(millis);
			this.secondary("z-inDst-" + millis,
				zone.inDaylightTime(date));
		}
	}
}
