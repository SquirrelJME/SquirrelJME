// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.ServiceLoader;

/**
 * This is a factory which is used to support an architecture, an operating
 * system which uses the given architecture, and a variant of that
 * architecture.
 *
 * The factory must handle initializing outputs for the correct CPU variant
 * and endianess.
 *
 * @since 2016/07/04
 */
public abstract class JITOutputFactory
{
	/** Service loader to use. */
	private static final ServiceLoader<JITOutputFactory> _SERVICES =
		ServiceLoader.<JITOutputFactory>load(JITOutputFactory.class);
	
	/** The architecture to target. */
	protected final String architecture;
	
	/** The operating system to target. */
	protected final String operatingsystem;
	
	/** The variant of the operating system to target. */
	protected final String operatingsystemvariant;
	
	/**
	 * Initializes the base factory which creates {@link JITOutput}s.
	 *
	 * @param __arch The target architecture.
	 * @param __os The operating system to target.
	 * @param __osvar The variant of the operating system.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/04
	 */
	public JITOutputFactory(String __arch, String __os, String __osvar)
		throws NullPointerException
	{
		// Check
		if (__arch == null || __os == null || __osvar == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.architecture = __arch;
		this.operatingsystem = __os;
		this.operatingsystemvariant = __osvar;
	}
	
	/**
	 * Creates an output which uses the given configuration, the output would
	 * then be used as part of the JIT for code generation.
	 *
	 * @param __config The configuration which specifies the system to target.
	 * @return An output which targets the given system.
	 * @throws JITException If the specified configuration cannot be used,
	 * possibly because it is invalid or no {@link JITOutputFactory} supports
	 * the given system.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/05
	 */
	public static JITOutput createOutput(JITOutputConfig.Immutable __config)
		throws NullPointerException
	{
		// Check
		if (__config == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

