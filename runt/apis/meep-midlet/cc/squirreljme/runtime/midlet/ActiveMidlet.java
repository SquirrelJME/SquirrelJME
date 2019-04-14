// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.midlet;

import javax.microedition.midlet.MIDlet;

/**
 * This is used to store the current MIDlet which is being run in the current
 * process.
 *
 * @since 2017/02/26
 */
public final class ActiveMidlet
{
	/** Lock to prevent multiple midlets from running. */
	private static final Object _ACTIVE_LOCK =
		new Object();
	
	/** Only a single midlet may run at a time. */
	private static volatile MIDlet _ACTIVE_MIDLET;
	
	/**
	 * Not used.
	 *
	 * @since 2017/02/26
	 */
	private ActiveMidlet()
	{
	}
	
	/**
	 * Returns the currently active midlet.
	 *
	 * @return The active midlet.
	 * @throws IllegalStateException If no midlet is set.
	 * @since 2017/02/26
	 */
	public static MIDlet get()
		throws IllegalStateException
	{
		MIDlet rv = ActiveMidlet.optional();
		
		// {@squirreljme.error AD01 No MIDlet is currently active.}
		if (rv == null)
			throw new IllegalStateException("AD01");
		
		return rv;
	}
	
	/**
	 * Returns the currently active midlet.
	 *
	 * @return The active midlet or {@code null} if none is active.
	 * @since 2019/04/14
	 */
	public static MIDlet optional()
		throws IllegalStateException
	{
		// Lock
		synchronized (_ACTIVE_LOCK)
		{
			return _ACTIVE_MIDLET;
		}
	}
	
	/**
	 * Sets the currently active midlet.
	 *
	 * @param __m The midlet to set.
	 * @throws IllegalStateException If a midlet is already set.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/26
	 */
	public static void set(MIDlet __m)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Prevent multiple MIDlet launches
		synchronized (_ACTIVE_LOCK)
		{
			// {@squirreljme.error AD02 Only a single MIDlet may be active at
			// a time.}
			MIDlet active = _ACTIVE_MIDLET;
			if (active != null)
				throw new IllegalStateException("AD02");
			
			// Set active midlet
			_ACTIVE_MIDLET = __m;
		}
	}
}

