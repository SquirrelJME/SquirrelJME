// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.util.Map;

/**
 * This class contains the state of memory actions, that is everything that
 * has modified or set memory specifically.
 * 
 * There are actions which act upon memory handles and set a specific value
 * or otherwise within them. This is to the bootloader is able to reconstruct
 * the pre-boot state and determine the best way to represent whatever is
 * written.
 *
 * @since 2020/12/22
 */
public final class MemActions
{
	/** Actions that are being performed. */
	private final Map<__MemHandleRef__, MemHandleActions> _actions =
		new SortedTreeMap<>();
	
	/**
	 * Returns the actions for the given handle.
	 * 
	 * @param __memHandle The handle to get.
	 * @return The actions for this single memory handle.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/10
	 */
	protected MemHandleActions get(MemHandle __memHandle)
		throws NullPointerException
	{
		if (__memHandle == null)
			throw new NullPointerException("NARG");
		
		Map<__MemHandleRef__, MemHandleActions> actions = this._actions;
		__MemHandleRef__ ref = new __MemHandleRef__(__memHandle);
		
		MemHandleActions rv = actions.get(ref);
		if (rv != null)
			return rv;
		
		actions.put(ref, (rv = new MemHandleActions()));
		return rv;
	}
	
	/**
	 * Writes the given value into the given offset of a memory handle.
	 * 
	 * @param __memHandle The memory handle to store in.
	 * @param __off The offset to place.
	 * @param __iVal The value to store.
	 * @throws IndexOutOfBoundsException If the offset is out of bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/10
	 */
	public void writeInteger(MemHandle __memHandle, int __off,
		int __iVal)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__memHandle == null)
			throw new NullPointerException("NARG");
		
		if (__off < 0 || (__off + 3) > __memHandle.byteSize)
			throw new IndexOutOfBoundsException("IOOB " + __off);
		
		this.get(__memHandle).writeInteger(__off, __iVal);
	}
	
	/**
	 * Writes the given value into the given offset of a memory handle.
	 * 
	 * @param __memHandle The memory handle to store in.
	 * @param __off The offset to place.
	 * @param __hVal The value to store.
	 * @throws IndexOutOfBoundsException If the offset is out of bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/10
	 */
	public void writeInteger(MemHandle __memHandle, int __off,
		MemHandle __hVal)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__memHandle == null || __hVal == null)
			throw new NullPointerException("NARG");
		
		if (__off < 0 || (__off + 3) > __memHandle.byteSize)
			throw new IndexOutOfBoundsException("IOOB " + __off);
		
		this.get(__memHandle).writeInteger(__off, __hVal);
	}
}
