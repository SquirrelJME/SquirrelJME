// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.hil;

import net.multiphasicapps.squirreljme.jit.java.BasicBlockKey;

/**
 * This class represents a high level basic block which contains instructions
 * and a sole entry point from other basic blocks.
 *
 * @since 2017/08/25
 */
public class HighLevelBlock
{
	/** The key which this block is associated with. */
	protected final BasicBlockKey key;
	
	/**
	 * Initializes the high level basic block.
	 *
	 * @param __key The key this block is associated with.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/25
	 */
	public HighLevelBlock(BasicBlockKey __key)
		throws NullPointerException
	{
		// Check
		if (__key == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.key = __key;
	}
}

