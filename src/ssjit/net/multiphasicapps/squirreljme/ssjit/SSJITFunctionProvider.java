// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ssjit;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * This is a provider for functions that may be associated with a code
 * producer so that support for architectures and operating systems are
 * plugable and can easily be patched.
 *
 * @since 2016/06/25
 */
public abstract class SSJITFunctionProvider
{
	/** This is a shared service lookup for function providers. */
	private static final ServiceLoader<SSJITFunctionProvider> _SERVICES =
		ServiceLoader.<SSJITFunctionProvider>load(SSJITFunctionProvider.class);
	
	/** The target architecture. */
	protected final String architecture;
	
	/** The operationg system the provider targets. */
	protected final String operatingsystem;
	
	/**
	 * This initializes the base provider.
	 *
	 * @param __arch The architecture that this provider supports.
	 * @param __os The operating system this provider is defined for, if
	 * {@code null} then it is a base provider not intended for any operating
	 * system.
	 * @throws NullPointerException If no architecture was specified.
	 * @since 2016/06/25
	 */
	public SSJITFunctionProvider(String __arch, String __os)
		throws NullPointerException
	{
		// Check
		if (__arch == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.architecture = __arch;
		this.operatingsystem = __os;
	}
	
	/**
	 * Returns the generic variant.
	 *
	 * @return The generic variant.
	 * @since 2016/06/25
	 */
	public abstract SSJITVariant genericVariant();
	
	/**
	 * This returns the array of possible variants that are available for usage
	 * for a given architecture. A variant is used in the case where a specific
	 * CPU needs to be targetted. It is recommended although not required that
	 * there be a "generic" variant with the highest level of support.
	 *
	 * @return An array of target variants.
	 * @since 2016/06/25
	 */
	public abstract SSJITVariant[] variants();
	
	/**
	 * Returns the architecture that this factory exists for.
	 *
	 * @return The architecture the factory targets.
	 * @since 2016/06/25
	 */
	public final String architecture()
	{
		return this.architecture;
	}
	
	/**
	 * Returns the variant which is associated with the given variant name.
	 *
	 * @param __var The variant to check.
	 * @return The variant matching the given name or {@code null} if there
	 * is no such variant.
	 * @since 2016/06/25
	 */
	public final SSJITVariant getVariant(String __var)
		throws NullPointerException
	{
		// Check
		if (__var == null)
			throw new NullPointerException("NARG");
		
		// If generic, use the generic one
		if (__var.equals("generic"))
			return genericVariant();
		
		// Go through variants
		for (SSJITVariant v : variants())
			if (__var.equals(v.variant()))
				return v;
		
		// Does not
		return null;
	}
	
	/**
	 * Returns the operating system that this factory exists for.
	 *
	 * @return The operating system this factory targets or {@code null} if
	 * there is no specified operating system.
	 * @since 2016/06/25
	 */
	public final String operatingSystem()
	{
		return this.operatingsystem;
	}
	
	/**
	 * This looks up and returns function providers which support the given
	 * architecture along with the optionally specified operating system.
	 *
	 * @param __arch The architecture to return a function provider for.
	 * @param __archvar The variant of a given architecture, this is optional.
	 * @param __os The operating system to provide functions for.
	 * @throws NullPointerException If no architecture was specified.
	 * @since 2016/06/27
	 */
	public static SSJITFunctionProvider[] lookup(String __arch,
		String __archvar, String __os)
		throws NullPointerException
	{
		// Check
		if (__arch == null)
			throw new NullPointerException("NARG");
		
		// Lock
		ServiceLoader<SSJITFunctionProvider> services = _SERVICES;
		synchronized (services)
		{
			throw new Error("TODO");
		}
	}
}

