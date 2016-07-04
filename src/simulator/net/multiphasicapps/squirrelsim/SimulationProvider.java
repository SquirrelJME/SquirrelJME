// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelsim;

import java.io.InputStream;
import java.util.ServiceLoader;

/**
 * This provider allows the creation of simulations that simulate a given
 * operating system (and any of its variants) on a given CPU. The purpose of
 * the simulator is to test SquirrelJME although it may be possible to run a
 * more limited set of programs on it.
 *
 * This is used with the service loader.
 *
 * @since 2016/07/04
 */
public abstract class SimulationProvider
{
	/** Service lookup. */
	private static final ServiceLoader<SimulationProvider> _SERVICES =
		ServiceLoader.<SimulationProvider>load(SimulationProvider.class);

	/** The architecture which is simulated. */
	protected final String architecture;
	
	/** The operating system to simulate. */
	protected final String operatingsystem;
	
	/** The variant of the operating system. */
	protected final String operatingsystemvar;
	
	/**
	 * Initializes the base simulation provider.
	 *
	 * @param __arch The architecture being simulated.
	 * @param __os The operating system to simulate.
	 * @param __osvar The operating system variant.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/04
	 */
	public SimulationProvider(String __arch, String __os, String __osvar)
		throws NullPointerException
	{
		// Check
		if (__arch == null || __os == null || __osvar == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.architecture = __arch;
		this.operatingsystem = __os;
		this.operatingsystemvar = __osvar;
	}
	
	/**
	 * Returns the simulated architecture.
	 *
	 * @return The simulated architecture.
	 * @since 2016/07/04
	 */
	public final String architectureName()
	{
		return this.architecture;
	}
	
	/**
	 * Returns the name of the operating system to simulate.
	 *
	 * @return The name of the simulated operating system.
	 * @since 2016/07/04
	 */
	public final String operatingSystemName()
	{
		return this.operatingsystem;
	}
	
	/**
	 * Returns the variant of the operating system to simulate.
	 *
	 * @return The operating system variant to simulate.
	 * @since 2016/07/04
	 */
	public final String operatingSystemVariant()
	{
		return this.operatingsystemvar;
	}
	
	/**
	 * Goes through all simulation provider services and locates resources that
	 * are avaialble to the classes that the provider is a part of. This is
	 * used so that configurations may be read from providers which may reside
	 * in other JAR files.
	 *
	 * @param __res The resource to locate.
	 * @return The stream of the given resource or {@code null} if not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/04
	 */
	public static InputStream getResourceAsStream(String __res)
		throws NullPointerException
	{
		// Lock
		ServiceLoader<SimulationProvider> services = _SERVICES;
		synchronized (services)
		{
			for (SimulationProvider sp : services)
			{
				// Get resource off the class
				InputStream rv = sp.getClass().getResourceAsStream(__res);
				
				// Return if found
				if (rv != null)
					return rv;
			}
		}
		
		// Not found
		return null;
	}
}

