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
import cc.squirreljme.runtime.midlet.ActiveMidlet;
import javax.microedition.midlet.MIDlet;

/**
 * This is used to help identify suites and such.
 *
 * @since 2019/04/14
 */
public final class SuiteIdentifier
{
	/** The identifier for the current suite. */
	private static long _CURRENT_ID;
	
	/** The current vendor. */
	private static String _CURRENT_VENDOR;
	
	/** The current name. */
	private static String _CURRENT_NAME;
	
	/**
	 * Not used.
	 *
	 * @since 2019/04/14
	 */
	private SuiteIdentifier()
	{
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
		long rv = SuiteIdentifier._CURRENT_ID;
		if (rv != 0)
			return rv;
		
		// Set, cache, and store
		SuiteIdentifier._CURRENT_ID = (rv = SuiteIdentifier.identifier(
			SuiteIdentifier.currentVendor(), SuiteIdentifier.currentName()));
		return rv;
	}
	
	/**
	 * Returns the current name.
	 *
	 * @return The current name.
	 * @since 2019/04/14
	 */
	public static String currentName()
	{
		String rv = SuiteIdentifier._CURRENT_NAME;
		if (rv != null)
			return rv;
		
		// TODO: Better means of getting the current name
		Debugging.todoNote("Better means of currentName()");
		
		// Try through the current MIDlet properties
		if (rv == null)
		{
			MIDlet mid = ActiveMidlet.optional();
			if (mid != null)
				rv = mid.getAppProperty("MIDlet-Name");
		}
		
		// Fallback
		if (rv == null)
			rv = "UndefinedName";
		
		// Cache and return
		SuiteIdentifier._CURRENT_NAME = rv;
		return rv;
	}
	
	/**
	 * Returns the current vendor.
	 *
	 * @return The current vendor.
	 * @since 2019/04/14
	 */
	public static String currentVendor()
	{
		String rv = SuiteIdentifier._CURRENT_VENDOR;
		if (rv != null)
			return rv;
		
		// TODO: Better means of getting the current name
		Debugging.todoNote("Better means of currentVendor()");
		
		// Try through the current MIDlet properties
		if (rv == null)
		{
			MIDlet mid = ActiveMidlet.optional();
			if (mid != null)
				rv = mid.getAppProperty("MIDlet-Vendor");
		}
		
		// Fallback
		if (rv == null)
			rv = "UndefinedVendor";
		
		// Cache and return
		SuiteIdentifier._CURRENT_VENDOR = rv;
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

