// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.JITOutput;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.JITOutputFactory;

/**
 * This is the base class for all factories which JIT compile byte code into
 * standard blobs shared by many architectures.
 *
 * @since 2016/07/26
 */
public abstract class GenericOutputFactory
	extends JITOutputFactory
{
	/**
	 * Internally creates a new generic output using the given configuration.
	 *
	 * @param __config The configuration to use.
	 * @return The generic JIT output.
	 * @throws JITException If the output could not created.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/26
	 */
	protected abstract GenericOutput internalCreate(
		JITOutputConfig.Immutable __config)
		throws JITException, NullPointerException;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/26
	 */
	@Override
	public final JITOutput create(JITOutputConfig.Immutable __config)
		throws JITException, NullPointerException
	{
		// Check
		if (__config == null)
			throw new NullPointerException("NARG");
		
		// Create
		return internalCreate(__config);
	}
}

