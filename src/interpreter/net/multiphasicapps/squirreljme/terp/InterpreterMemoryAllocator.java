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

import net.multiphasicapps.squirreljme.mmu.MemoryAllocator

/**
 * This is allocator which is associated with the interpreter and is used to
 * partition the memory spaces within it.
 *
 * @since 2016/06/13
 */
public class InterpreterMemoryAllocator
	implements MemoryAllocator
{
	/** The accessor the interpreter uses for memory. */
	protected final InterpreterMemoryAccessor coremem;
	
	/**
	 * This initializes the memory accessor used in the interpreter.
	 *
	 * @param __ima The memory accessor used by the interpreter to partition
	 * memory with.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/13
	 */
	public InterpreterMemoryAllocator(InterpreterMemoryAccessor __ima)
		throws NullPointerException
	{
		// Check
		if (__ima == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.coremem = __ima;
	}
}

