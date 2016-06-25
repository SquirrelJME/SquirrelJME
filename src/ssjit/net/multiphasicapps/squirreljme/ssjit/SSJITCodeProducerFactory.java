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

/**
 * This is the factory for a code producer which is capable of generating the
 * code producer that is required for native code generation for a given
 * target.
 *
 * @since 2016/06/25
 */
public abstract class SSJITCodeProducerFactory
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
	public SSJITCodeProducerFactory(String __arch, String __os)
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
	 * This returns the array of possible variants that are available for usage
	 * for a given architecture. A variant is used in the case where a specific
	 * CPU needs to be targetted.
	 *
	 * It is required that there always be at least one variant which is
	 * entitled "generic" which should act as a most common set support for a
	 * given CPU.
	 *
	 * @return An array of target variants.
	 * @since 2016/06/25
	 */
	public abstract Variant[] variants();
	
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
	 * This interface is used in the specification of variants.
	 *
	 * @since 2016/06/25
	 */
	public static final interface Variant
	{
		/**
		 * Returns the name of the variant.
		 *
		 * @return The variant name.
		 * @since 2016/06/25
		 */
		public abstract String variant();
	}
}

