// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

/**
 * Represents a memory initialization operation.
 *
 * @since 2019/04/27
 */
public final class MemoryOperation
{
	/** The type of operation. */
	public final MemoryOperationType type;
	
	/** The size of write. */
	public final int size;
	
	/** The address of the write. */
	public final int address;
	
	/**
	 * Initializes the memory operation.
	 *
	 * @param __t The operation type.
	 * @param __sz The size.
	 * @param __ad The address.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	public MemoryOperation(MemoryOperationType __t, int __sz, int __ad)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		this.type = __t;
		this.size = __sz;
		this.address = __ad;
	}
}

