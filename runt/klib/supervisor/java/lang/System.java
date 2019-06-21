// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.SystemCallIndex;

/**
 * This contains some basic system functions.
 *
 * @since 2019/06/20
 */
public final class System
{
	/**
	 * Returns the current time on the system's clock in UTC since the epoch
	 * (January 1, 1970 UTC).
	 *
	 * Note that this clock is not monotonic in that if a system adjusts the
	 * system clock this method may return values lower than previous calls
	 * which are made.
	 *
	 * Depending on the host hardware and operating system, the granularity of
	 * this clock may or may not be accurate.
	 *
	 * @return The number of milliseconds since the epoch.
	 * @since 2017/11/10
	 */
	public static long currentTimeMillis()
	{
		// Returns the current time in UTC, not local time zone.
		return ((Assembly.sysCallV(
				SystemCallIndex.TIME_LO_MILLI_WALL) & 0xFFFFFFFFL) |
			(((long)Assembly.sysCallV(
				SystemCallIndex.TIME_HI_MILLI_WALL)) << 32L));
	}
	
	/**
	 * Returns the number of nanoseconds which have passed from a previously
	 * unspecified time. The returned value might not be accurate to the
	 * nanosecond. This clock is monotonic and does not suffer from time
	 * shifts caused by clock adjustments.
	 *
	 * The value returned here is specific to the current virtual machine and
	 * cannot be used elsewhere. Even two virtual machines running on the
	 * same system can use completely different values.
	 *
	 * After about 292 years (2 to the 63rd power nanoseconds) using signed
	 * comparison to calculate the amount of time that has passed will no
	 * longer function properly. For extremely long running processes it is
	 * recommended to treat the values as unsigned to extend past this limit
	 * or handle the overflow of the time value to represent any time
	 * quantity, this of course requires that time be checked every 292 or
	 * 584 years).
	 *
	 * @return The number of nanoseconds which have passed.
	 * @since 2016/06/16
	 */
	public static long nanoTime()
	{
		// Returns the current monotonic clock time
		return ((Assembly.sysCallV(
				SystemCallIndex.TIME_LO_NANO_MONO) & 0xFFFFFFFFL) |
			(((long)Assembly.sysCallV(
				SystemCallIndex.TIME_HI_NANO_MONO)) << 32L));
	}
}

