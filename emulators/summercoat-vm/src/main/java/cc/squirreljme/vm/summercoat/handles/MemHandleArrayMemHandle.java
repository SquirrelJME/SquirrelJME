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
import cc.squirreljme.jvm.summercoat.ld.mem.MemHandleReference;
import cc.squirreljme.vm.summercoat.MemHandle;
import cc.squirreljme.vm.summercoat.MemHandleManager;
import java.lang.ref.ReferenceQueue;
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
	
	/** The manager for memory handles. */
	private final WeakReference<MemHandleManager> _memHandleManager;
	
	/**
	 * Initializes a new handle.
	 *
	 * @param __id The identifier for this handle.
	 * @param __base The base array size.
	 * @param __memHandleManager The manager used for memory handles.
	 * @param __array The array used.
	 * @throws IllegalArgumentException If the kind is not valid or the
	 * requested size is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/17
	 */
	public MemHandleArrayMemHandle(int __id, int __base,
		MemHandleManager __memHandleManager, MemHandle... __array)
		throws IllegalArgumentException, NullPointerException
	{
		super(__id, MemHandleKind.OBJECT_ARRAY,
			__base, 4, __array.length);
		
		if (__memHandleManager == null)
			throw new NullPointerException("NARG");
		
		this.values = __array;
		this._memHandleManager = new WeakReference<>(__memHandleManager); 
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/01/17
	 */
	@Override
	public void memWriteHandle(int __addr, MemHandleReference __v)
	{
		int relBase = __addr - super.rawSize;
		if (relBase < 0)
			super.memWriteHandle(__addr, __v);
		else
			this.values[relBase / super.cellSize] = this.__manager()
				.get(__v.id);
	}
	
	/**
	 * Returns the handle manager.
	 * 
	 * @return The handle manager.
	 * @throws IllegalStateException If the manager was GCed.
	 * @since 2021/02/09
	 */
	private MemHandleManager __manager()
		throws IllegalStateException
	{
		// This is pointless if it cannot access the manager
		MemHandleManager rv = this._memHandleManager.get();
		if (rv == null)
			throw new IllegalStateException("Handle manager was GCed.");
		
		return rv;
	}
}
