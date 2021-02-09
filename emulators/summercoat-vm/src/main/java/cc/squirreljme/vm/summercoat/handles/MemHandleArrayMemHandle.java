// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat.handles;

import cc.squirreljme.jvm.summercoat.constants.MemHandleKind;
import cc.squirreljme.vm.summercoat.MemHandle;
import cc.squirreljme.vm.summercoat.MemHandleManager;
import cc.squirreljme.vm.summercoat.MemHandleReference;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Memory handle references.
 *
 * @since 2021/01/17
 */
public class MemHandleArrayMemHandle
	extends MemHandleArray
{
	/** The array values. */
	protected final MemHandle[] values;
	
	/** The manager to lookup handles. */
	protected final Reference<MemHandleManager> manager;
	
	/**
	 * Initializes a new handle.
	 *
	 * @param __man The manager for memory handles.
	 * @param __id The identifier for this handle.
	 * @param __base The base array size.
	 * @param __array The array used.
	 * @throws IllegalArgumentException If the kind is not valid or the
	 * requested size is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/17
	 */
	public MemHandleArrayMemHandle(MemHandleManager __man, int __id,
		int __base, MemHandle... __array)
		throws IllegalArgumentException, NullPointerException
	{
		super(__id, MemHandleKind.OBJECT_ARRAY,
			__base, 4, __array.length);
			
		if (__man == null)
			throw new NullPointerException("NARG");
		
		this.manager = new WeakReference<>(__man);
		this.values = __array;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/01/17
	 */
	@Override
	public void memWriteHandle(int __addr, MemHandleReference __v)
	{
		this.memWriteHandle(__addr, this.__lookup(__v));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/01/17
	 */
	@Override
	public void memWriteHandle(int __addr, MemHandle __v)
	{
		int relBase = __addr - super.rawSize;
		if (relBase < 0)
			super.memWriteHandle(__addr, __v);
		else
			this.values[relBase / super.cellSize] = __v;
	}
	
	/**
	 * Looks up the given handle.
	 * 
	 * @param __v The handle to lookup.
	 * @return The handle for the given value.
	 * @since 2021/01/17
	 */
	private MemHandle __lookup(MemHandleReference __v)
	{
		if (__v == null)
			return null;
		
		MemHandleManager manager = this.manager.get();
		if (manager == null)
			throw new IllegalStateException("Manager was GCed.");
		
		return manager.get(__v.id);
	}
}
