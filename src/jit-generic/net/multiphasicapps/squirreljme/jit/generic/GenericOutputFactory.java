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
 * This is the factory which is used to create instances of the generic JIT
 * which targets a wide range of systems via the native code generation
 * interfaces. The generic JIT relies on a standard output format and defers
 * any native generation to the native code generators.
 *
 * @since 2016/07/26
 */
public class GenericOutputFactory
	extends JITOutputFactory
{
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
		return new GenericOutput(__config);
	}
}

