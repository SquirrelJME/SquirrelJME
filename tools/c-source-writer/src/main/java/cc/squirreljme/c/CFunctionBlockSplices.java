// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;

/**
 * Contains multiple splices for a function, may be closed accordingly.
 *
 * @since 2023/07/15
 */
public final class CFunctionBlockSplices
	implements AutoCloseable
{
	/** The blocks to use. */
	private final __CFunctionSplice__[] _blocks;
	
	/**
	 * Initializes the block splices.
	 * 
	 * @param __blocks The blocks being spliced.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/15
	 */
	CFunctionBlockSplices(__CFunctionSplice__... __blocks)
		throws NullPointerException
	{
		if (__blocks == null)
			throw new NullPointerException("NARG");
		
		this._blocks = __blocks;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/15
	 */
	@Override
	public void close()
		throws IOException
	{
		// Close all blocks in the order they were defined, but use the
		// internal close logic
		for (__CFunctionSplice__ block : this._blocks)
			block.__close();
	}
	
	/**
	 * Returns the splice that belongs to the given ID.
	 * 
	 * @param __id The ID of the splice.
	 * @return The splice for the given ID.
	 * @throws IndexOutOfBoundsException If the ID is not valid.
	 * @since 2023/07/15
	 */
	public CFunctionBlock splice(int __id)
		throws IndexOutOfBoundsException
	{
		return this._blocks[__id];
	}
}
