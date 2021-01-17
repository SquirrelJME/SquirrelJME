// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat.handles;

import cc.squirreljme.vm.summercoat.MemHandle;

/**
 * A memory handle that is an array.
 *
 * @since 2021/01/17
 */
public abstract class MemHandleArray
	extends MemHandle
{
	/** The length of the array. */
	public final int length;
	
	/** The size of each cell in the array. */
	protected final int cellSize;
	
	/**
	 * Initializes a new handle.
	 *
	 * @param __id The identifier for this handle.
	 * @param __kind The kind of memory handle to allocate.
	 * @param __base The array base
	 * @param __cellSize The size of each cell in the array.
	 * @param __len The array length.
	 * @throws IllegalArgumentException If the kind is not valid or the
	 * requested size is negative.
	 * @since 2021/01/17
	 */
	public MemHandleArray(int __id, int __kind, int __base, int __cellSize,
		int __len)
		throws IllegalArgumentException
	{
		super(__id, __kind, __base + (__cellSize * __len), __base);
		
		if (__len < 0)
			throw new IllegalArgumentException("Negative length: " + __len);
		
		this.length = __len;
		this.cellSize = __cellSize;
	}
}
