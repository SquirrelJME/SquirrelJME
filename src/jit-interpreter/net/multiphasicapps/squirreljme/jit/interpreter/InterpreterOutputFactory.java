// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.interpreter;

import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITOutput;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.JITOutputFactory;

/**
 * This is an output factory which is capable of creating outputs that target
 * the interpreter.
 *
 * @since 2016/07/22
 */
public class InterpreterOutputFactory
	extends JITOutputFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2016/07/22
	 */
	@Override
	public JITOutput create(JITOutputConfig.Immutable __config)
		throws JITException
	{
		// Check
		if (__config == null)
			throw new NullPointerException("NARG");
		
		// Create
		return new InterpreterOutput(__config);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/22
	 */
	@Override
	public boolean supportsConfig(JITOutputConfig.Immutable __config)
	{
		// Check
		if (__config == null)
			throw new NullPointerException("NARG");
		
		// Just check the architecture
		return __config.triplet().architecture().equals("interpreter");
	}
}

