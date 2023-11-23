// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

import java.util.NoSuchElementException;
import java.util.ServiceLoader;

/**
 * Loads a driver.
 *
 * @since 2023/08/20
 */
public final class DriverLoader
{
	/**
	 * Not used.
	 * 
	 * @since 2023/08/20
	 */
	private DriverLoader()
	{
	}
	
	/**
	 * Loads an initializes the specified driver with the highest priority.
	 *
	 * @param <T> The type of driver to load.
	 * @param __type The type of driver to load.
	 * @return The loaded driver.
	 * @throws NoSuchElementException If no such driver exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/20
	 */
	public static <T> T loadBest(Class<T> __type)
		throws NoSuchElementException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// The current best driver
		DriverFactory best = null;
		int bestPriority = Integer.MAX_VALUE;
		
		// Go through available factories to find drivers for this
		for (DriverFactory factory : ServiceLoader.load(DriverFactory.class))
		{
			// Ignore other drivers
			if (!factory.providesClass().isAssignableFrom(__type))
				continue;
			
			// Does this driver have more priority?
			int checkPriority = factory.priority();
			if (checkPriority < bestPriority)
			{
				best = factory;
				bestPriority = checkPriority;
			}
		}
		
		// Found a driver?
		if (best != null)
			return __type.cast(best.newInstance());
		
		/* {@squirreljme.error ZZ40 No such driver. (The class)}. */
		throw new NoSuchElementException("ZZ40 " + __type);
	}
}
