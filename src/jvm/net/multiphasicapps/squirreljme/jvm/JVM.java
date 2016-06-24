// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jvm;

/**
 * This class provides the basic control for the Java Virtual Machine.
 *
 * @since 2016/06/23
 */
public final class JVM
{
	/** The ability factory of the JVM. */
	protected final JVMAbilityFactory abilityfactory;
	
	/**
	 * Initializes the base virtual machine.
	 *
	 * @param __af The ability factory which provides classes that provide the
	 * standard JVM interfaces.
	 * @param __args Arguments to be passed to the launcher.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/23
	 */
	public JVM(JVMAbilityFactory __af, String... __args)
		throws NullPointerException
	{
		// Check
		if (__af == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.abilityfactory = __af;
		
		throw new Error("TODO");
	}
}

