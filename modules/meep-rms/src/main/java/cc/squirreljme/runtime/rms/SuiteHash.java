// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.rms;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.midlet.ApplicationHandler;

/**
 * Hash for suites.
 *
 * @since 2019/04/14
 */
public final class SuiteHash
	implements Comparable<SuiteHash>
{
	/** The identifier for the current suite. */
	private static long _CURRENT_ID;
	
	/**
	 * Not used.
	 *
	 * @since 2019/04/14
	 */
	private SuiteHash()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/16
	 */
	@Override
	public int compareTo(SuiteHash __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the current identifier.
	 *
	 * @return The current identifier.
	 * @since 2019/04/14
	 */
	public static long currentIdentifier()
	{
		// Already been cached?
		long rv = SuiteHash._CURRENT_ID;
		if (rv != 0)
			return rv;
		
		// Set, cache, and store
		SuiteHash._CURRENT_ID = (rv = SuiteHash.identifier(
			ApplicationHandler.currentVendor(),
			ApplicationHandler.currentName()));
		return rv;
	}
	
	/**
	 * Returns the suite identifier.
	 *
	 * @param __vend The vendor.
	 * @param __suite The suite.
	 * @return The identifier.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/14
	 */
	public static long identifier(String __vend, String __suite)
		throws NullPointerException
	{
		if (__vend == null || __suite == null)
			throw new NullPointerException("NARG");
		
		return ((((long)__vend.hashCode()) & 0xFFFFFFFFL) << 32) |
			(((long)__suite.hashCode()) & 0xFFFFFFFFL);
	}
}

