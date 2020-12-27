// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.jvm.summercoat.constants.MemHandleKind;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.util.Map;

/**
 * This class contains the state of memory handles within the boot system.
 *
 * @since 2020/12/16
 */
public final class MemHandles
{
	/** Memory actions. */
	protected final MemActions memActions;
	
	/** Memory handles that have been allocated, for later getting. */
	private final Map<Integer, MemHandle> _handles =
		new SortedTreeMap<>();
	
	/** The next memory handle ID. */
	private int _nextId =
		1;
	
	/**
	 * Initializes the memory handles.
	 * 
	 * @param __memActions The memory actions to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/22
	 */
	public MemHandles(MemActions __memActions)
		throws NullPointerException
	{
		if (__memActions == null)
			throw new NullPointerException("NARG");
		
		this.memActions = __memActions;
	}
	
	/**
	 * Allocates a raw memory handle.
	 * 
	 * @param __kind The {@link MemHandleKind}.
	 * @param __sz The number of bytes to allocate.
	 * @return A raw memory handle.
	 * @throws IllegalArgumentException If {@code __kind} is not valid.
	 * @since 2020/12/21
	 */
	public MemHandle alloc(int __kind, int __sz)
	{
		throw Debugging.todo();
	}
	
	/**
	 * This allocates a handle for class information.
	 * 
	 * @return The allocated class information handle.
	 * @since 2020/12/20
	 */
	public ClassInfoHandle allocClassInfo()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Allocates storage for referring to multiple classes.
	 * 
	 * @param __classes The classes to allocate.
	 * @return Allocated class information data.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/21
	 */
	public ClassInfoListHandle allocClassInfos(ClassState... __classes)
		throws NullPointerException
	{
		if (__classes == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * Allocates storage for field data.
	 * 
	 * @param __sz The number of bytes to allocate.
	 * @return The memory handle for field storage.
	 * @since 2020/12/21
	 */
	public MemHandle allocFields(int __sz)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Allocates a pool handle.
	 * 
	 * @param __sz The number of entries to place in.
	 * @return The allocated handle.
	 * @throws IllegalArgumentException If size is zero or negative.
	 * @since 2020/12/29
	 */
	public PoolHandle allocPool(int __sz)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
}
