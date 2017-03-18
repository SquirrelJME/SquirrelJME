// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.util.datadeque.ByteDeque;

/**
 * This is a code fragment output which is where generated machine code
 * instructions are placed during method generation.
 *
 * @since 2017/03/18
 */
public class CodeFragmentOutput
{
	/** The byte deque containing machine code. */
	protected final ByteDeque deque =
		new ByteDeque();
	
	/** The configuration for the output. */
	protected final JITConfig config;
	
	/**
	 * Initializes the code fragment output.
	 *
	 * @param __conf The configuration used for the JIT.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/18
	 */
	public CodeFragmentOutput(JITConfig __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
	}
}

