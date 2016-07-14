// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
	
	/**
	 * Initializes the base factory which creates {@link JITOutput}s.
	 *
	 * @since 2016/07/04
	 */
	public JITOutputFactory()
	{
	}
	
	/**
	 * This creates a new output which is used by JITs and uses the given
	 * configuration.
	 *
	 * @param __config The configuration to create an output for.
	 * @throws JITException If the output could not be created likely due to
	 * an incompatible configuration.
	 * @since 2016/07/05
	 */
	public abstract JITOutput create(JITOutputConfig.Immutable __config)
		throws JITException;
	
	/**
	 * Checks whether the factory supports the given configuration for output.
	 *
	 * @param __config The configuration to check support for.
	 * @return {@code true} if the configuration is supported.
	 * @since 2016/07/05
	 */
	public abstract boolean supportsConfig(JITOutputConfig.Immutable __config);
	
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
		throws JITException, NullPointerException
	{
		// Check
		if (__config == null)
			throw new NullPointerException("NARG");
		
		// Go through all services and find one that supports the given
		// configuration
		ServiceLoader<JITOutputFactory> services = _SERVICES;
		synchronized (services)
		{
			for (JITOutputFactory jof : services)
				if (jof.supportsConfig(__config))
					return jof.create(__config);
		}
		
		// {@squirreljme.error ED0a No output factory supports the given
		// configuration. (The used configuration)}
		throw new JITException(String.format("ED0a %s", __config));
	}
}

