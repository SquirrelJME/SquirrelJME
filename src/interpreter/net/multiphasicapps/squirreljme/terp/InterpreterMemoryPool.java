// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp;

import net.multiphasicapps.squirreljme.memory.AbstractMemoryPool;
import net.multiphasicapps.squirreljme.memory.MemoryPool;

/**
 * This is a memory pool which is available for usage by the interpreter.
 *
 * @since 2016/06/05
 */
public class InterpreterMemoryPool
	implements MemoryPool
{
	/** The bytes which make up the active memory. */
	protected final byte[] memory;
	
	/**
	 * Initializes the memory pool to use the given number of bytes.
	 *
	 * @param __bytes The bytes to use.
	 * @throws IllegalArgumentException On null arguments.
	 * @since 2016/06/05
	 */
	public InterpreterMemoryPool(int __bytes)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AN03 The number of bytes to use in the memory
		// pool cannot be negative or a non-power of two.}
		if (__bytes <= 0 || Integer.bitCount(__bytes) != 1)
			throw new IllegalArgumentException("AN03");
		
		// Allocate backing buffer
		this.memory = new byte[__bytes];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public final long size()
	{
		return this.memory.length;
	}
}

