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
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.generic.GenericOutputFactory;
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
	extends GenericOutputFactory
{
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
		
		// Interpreted MIPS
		JITTriplet triplet = __config.triplet();
		return triplet.architecture().equals("mips") &&
			triplet.operatingSystem().equals("squirreljme") &&
			triplet.operatingSystemVariant().equals("interpreter");
	}
}

