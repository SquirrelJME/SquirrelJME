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

import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.java.BasicBlockKey;

/**
 * This class represents a high level program which has been generated from
 * the Java byte code.
 *
 * @since 2017/08/24
 */
public class HighLevelProgram
{
	/** Blocks which make up the program. */
	private final Map<BasicBlockKey, HighLevelBlock> _blocks =
		new LinkedHashMap<>();
	
	/**
	 * Creates a basic block which uses the given key as the entry point.
	 *
	 * @param __k The entry point key for the block.
	 * @return A newly created high level block.
	 * @throws IllegalStateException If a block is already associated with
	 * the specified key.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/25
	 */
	public final HighLevelBlock createBlock(BasicBlockKey __k)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JI28 The specifiec key is already associated
		// with a high level basic block. (The basic block key)}
		Map<BasicBlockKey, HighLevelBlock> blocks = this._blocks;
		if (blocks.containsKey(__k))
			throw new IllegalStateException(String.format("JI28 %s", __k));
		
		// Create
		HighLevelBlock rv = new HighLevelBlock(__k);
		blocks.put(__k, rv);
		return rv;
	}
}

