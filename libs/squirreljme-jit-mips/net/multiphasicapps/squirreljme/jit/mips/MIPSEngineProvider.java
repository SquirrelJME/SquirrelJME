// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.mips;

import net.multiphasicapps.squirreljme.jit.TranslationEngine;
import net.multiphasicapps.squirreljme.jit.TranslationEngineProvider;

/**
 * This provides the engine for the JIT to generate MIPS machine code.
 *
 * @since 2017/01/31
 */
public class MIPSEngineProvider
	implements TranslationEngineProvider
{
	/**
	 * {@inheritDoc}
	 * @since 2017/01/31
	 */
	@Override
	public TranslationEngine createEngine()
	{
		throw new Error("TODO");
	}
}

