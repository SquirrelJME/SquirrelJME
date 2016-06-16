// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.unsafe;

/**
 * This provides interfaces to time control within the virtual machine.
 *
 * @since 2016/06/16
 */
public abstract class VMTime
{
	/** Are fake milliseconds being used? */
	private volatile boolean _fakemillis;
	
	/** The base nano time. */
	private volatile long _basenano;
	
	/**
	 * Returns the approximate number of nanoseconds which have passed since
	 * an unspecified start time.
	 *
	 * @return The number of nanoseconds which have passed.
	 * @since 2016/06/16
	 */
	public abstract long nanoTime();
	
	/**
	 * Returns the number of milliseconds that have passed since the UTC Java
	 * epoch.
	 *
	 * The Java epoch is UTC 00:00 (midnight) on January 1, 1970.
	 *
	 * @return The number of passed milliseconds.
	 * @since 2016/06/16
	 */
	public long utcMillis()
	{
		// It is possible that a real time clock is not implemented, so as such
		// virtualize the passage of time using the nanosecond clock from a
		// given starting point.
		long base, now = nanoTime();
		if (!this._fakemillis)
		{
			this._fakemillis = true;
			base = now;
		}
		
		// Use the previous base time
		else
			base = _basenano;
		
		// Determine the number of nanoseconds which have passed and convert
		// that to milliseconds
		long passed = (now - base) / 1_000_000L;
		
		// Use an unspecified epoch
		return 615_729_600_305L + passed;
	}
}

