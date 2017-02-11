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

/**
 * This is the engine which is able to generate MIPS machine code.
 *
 * @since 2017/02/11
 */
public class MIPSEngine
	extends TranslationEngine
{
	/** The configuration used. */
	protected final MIPSConfig config;
	
	/**
	 * Initializes the MIPS engine.
	 *
	 * @param __conf The MIPS configuration to use.
	 * @since 2017/02/11
	 */
	public MIPSEngine(MIPSConfig __conf)
	{
		super(__conf);
		
		// Set
		this.config = __conf;
	}
}

