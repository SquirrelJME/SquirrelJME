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

/**
 * This is the factory for a code producer which is capable of generating the
 * code producer that is required for native code generation for a given
 * target.
 *
 * @since 2016/06/25
 */
public abstract class SSJITProducerFactory
{
	/** The target architecture. */
	protected final String architecture;
	
	/** The operationg system the factory targets. */
	protected final String operatingsystem;
	
	/**
	 * This initializes the base factory.
	 *
	 * @param __arch The architecture this acts as a factory form.
	 * @param __os The operating system this factory is defined for, if
	 * {@code null} then it is a base factory not intended for any operating
	 * system.
	 * @throws NullPointerException If no architecture was specified.
	 * @since 2016/06/25
	 */
	public SSJITProducerFactory(String __arch, String __os)
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
	 * This creates a new producer which targets the given operating system
	 * with an optional variant.
	 *
	 * @param __os The stream to be passed to the code producer.
	 * @param __v The variant to target, if {@code null} then a default and
	 * the most compatible generic producer should be used.
	 * @return A code producer which outputs to the given stream and uses
	 * the given optional variant.
	 * @since 2016/06/25
	 */
	public abstract SSJITProducer createProducer(OutputStream __os,
		SSJITVariant __v);
	
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
}

