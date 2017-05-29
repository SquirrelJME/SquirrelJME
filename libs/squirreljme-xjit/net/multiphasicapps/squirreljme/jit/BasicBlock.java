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

import java.lang.ref.Reference;

/**
 * This is a basic block which contains an input state for variables and
 * aliases and contains instructions which perform work. Note that these blocks
 * contain actual instructions for a given instruction range.
 *
 * @since 2017/05/13
 */
public class BasicBlock
{
	/**
	 * Initializes the basic block.
	 *
	 * @param __zr The reference to the basic block used.
	 * @param __es The entry state for this basic block.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/28
	 */
	BasicBlock(ImmutableVariableState __es)
		throws NullPointerException
	{
		// Check
		if (__es == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

