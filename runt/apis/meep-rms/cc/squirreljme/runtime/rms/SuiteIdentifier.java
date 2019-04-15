// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.rms;

import javax.microedition.swm.ManagerFactory;
import javax.microedition.swm.Suite;
import javax.microedition.swm.Task;
import javax.microedition.swm.TaskManager;

/**
 * This is used to help identify suites and such.
 *
 * @since 2019/04/14
 */
public final class SuiteIdentifier
{
	/** The identifier for the current suite. */
	private static long _CURRENTID;
	
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
		long rv = _CURRENTID;
		if (rv != 0)
			return rv;
		
		// Need to obtain the current suite
		TaskManager tm = ManagerFactory.getTaskManager();
		Task ct = tm.getCurrentTask();
		Suite su = ct.getSuite();
		
		// Get the name and vendor
		String vend = su.getVendor(),
			name = su.getName();
		
		// If these are not set, just make them set
		if (vend == null)
			vend = "UndefinedVendor";
		if (name == null)
			name = "UndefinedName";
		
		// Return the identifier for this suite
		return (_CURRENTID = SuiteIdentifier.identifier(vend, name));
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

