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

/**
 * This is a fragment output which can be used to generated MIPS instructions.
 *
 * @since 2017/03/18
 */
public class MIPSFragmentOutput
	extends CodeFragmentOutput
{
	/** The MIPS configuration. */
	protected final MIPSConfig config;
	
	/**
	 * Sets the used MIPS configuration.
	 *
	 * @param __c The configuration to use.
	 * @since 2017/03/18
	 */
	public MIPSFragmentOutput(MIPSConfig __c)
	{
		super(__c);
		
		// Set
		this.config = __c;
	}
}

