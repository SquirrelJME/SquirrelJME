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

import net.multiphasicapps.squirreljme.jit.base.JITException;

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
	implements JITObjectProperties
{
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
	 * {@inheritDoc}
	 * @since 2016/09/02
	 */
	@Override
	public final String[] properties()
	{
		throw new Error("TODO");
	}
}

