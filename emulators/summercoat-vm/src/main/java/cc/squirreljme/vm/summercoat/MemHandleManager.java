// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.emulator.vm.VMException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Manages memory handles.
 *
 * @since 2020/11/28
 */
public final class MemHandleManager
{
	/** Memory handles that exist. */
	private final Map<Integer, MemHandle> _handles =
		new ConcurrentSkipListMap<>();
	
	/**
	 * Allocates the given handle.
	 * 
	 * @param __kind The kind of memory handle to allocate.
	 * @param __byteSize The number of bytes to allocate.
	 * @return The allocated handle.
	 * @throws IllegalArgumentException If the kind is not valid or the
	 * requested size is negative.
	 * @since 2021/01/17
	 */
	protected MemHandle alloc(int __kind, int __byteSize)
		throws IllegalArgumentException
	{
		throw new Error("TODO " + __kind + " " + __byteSize);
	}
	
	/**
	 * Returns the given memory handle.
	 * 
	 * @param __id The identifier of the handle.
	 * @return The memory handle for the given item.
	 * @throws VMException If the memory handle does not exist.
	 * @since 2020/11/28
	 */
	public final MemHandle get(int __id)
		throws VMException
	{
		MemHandle rv = this._handles.get(__id);
		if (rv == null)
			throw new VMException("No handle for 0x" +
				Integer.toUnsignedString(__id, 16));
		
		return rv;
	}
}
