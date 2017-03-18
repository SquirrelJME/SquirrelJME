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

import net.multiphasicapps.squirreljme.jit.CodeFragmentOutput;
import net.multiphasicapps.squirreljme.jit.JITStateAccessor;
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
	/** The configuration for MIPS. */
	protected final MIPSConfig config;
	
	/**
	 * Initializes the MIPS engine provider.
	 *
	 * @param __c The configuration to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/02
	 */
	public MIPSEngineProvider(MIPSConfig __c)
		throws NullPointerException
	{
		// Check
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Setup config
		this.config = __c;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/31
	 */
	@Override
	public TranslationEngine createEngine(JITStateAccessor __jsa)
	{
		// Create engine
		return new MIPSEngine(this.config, __jsa);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/18
	 */
	@Override
	public CodeFragmentOutput newCodeFragmentOutput()
	{
		return new MIPSFragmentOutput(this.config);
	}
}

