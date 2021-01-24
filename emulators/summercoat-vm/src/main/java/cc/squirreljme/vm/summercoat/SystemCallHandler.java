// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.jvm.SystemCallError;
import cc.squirreljme.jvm.SystemCallIndex;
import cc.squirreljme.jvm.summercoat.constants.MemHandleKind;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler for system calls.
 *
 * @since 2021/01/24
 */
public enum SystemCallHandler
{
	/** {@link SystemCallIndex#ARRAY_ALLOCATION_BASE}. */
	ARRAY_ALLOCATION_BASE(SystemCallIndex.ARRAY_ALLOCATION_BASE)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/01/24
		 */
		@Override
		public long handle(NativeCPU __cpu, int... __args)
			throws VMSystemCallException
		{
			return __cpu.arrayBase;
		}
	},
	
	/** {@link SystemCallIndex#ERROR_GET}. */
	ERROR_GET(SystemCallIndex.ERROR_GET)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/01/24
		 */
		@Override
		public long handle(NativeCPU __cpu, int... __args)
			throws VMSystemCallException
		{
			// Normalize ID number
			SystemCallHandler handler = SystemCallHandler.of(__args[0]);
			int normalId = (handler == null ? SystemCallIndex.QUERY_INDEX :
				handler.id);
			
			// Obtain system call error
			int[] sysCallErrors = __cpu._sysCallErrors;
			synchronized (__cpu)
			{
				return sysCallErrors[normalId];
			}
		}
	},
	
	/** {@link SystemCallIndex#ERROR_SET}. */
	ERROR_SET(SystemCallIndex.ERROR_SET)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/01/24
		 */
		@Override
		public long handle(NativeCPU __cpu, int... __args)
			throws VMSystemCallException
		{
			// Normalize ID number
			SystemCallHandler handler = SystemCallHandler.of(__args[0]);
			int normalId = (handler == null ? SystemCallIndex.QUERY_INDEX :
				handler.id);
			
			// Obtain system call error
			int[] sysCallErrors = __cpu._sysCallErrors;
			synchronized (__cpu)
			{
				sysCallErrors[normalId] = __args[1];
			}
			
			return 0;
		}
	},
	
	/** {@link SystemCallIndex#MEM_HANDLE_NEW}. */
	MEM_HANDLE_NEW(SystemCallIndex.MEM_HANDLE_NEW)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/01/24
		 */
		@Override
		public long handle(NativeCPU __cpu, int... __args)
			throws VMSystemCallException
		{
			int kind = __args[0];
			int size = __args[1];
			
			// Are these not valid?
			if (kind <= 0 || kind >= MemHandleKind.NUM_KINDS)
				throw new VMSystemCallException(
					SystemCallError.INVALID_MEMHANDLE_KIND);
			else if (size < 0)
				throw new VMSystemCallException(
					SystemCallError.VALUE_OUT_OF_RANGE);
			
			// Allocate handle and count up so it is not instantly GCed
			MemHandle handle = __cpu.state.memHandles.alloc(kind, size);
			handle.count(true);
			 
			return handle.id;
		}
	},
	
	/** {@link SystemCallIndex#QUERY_INDEX}. */
	QUERY_INDEX(SystemCallIndex.QUERY_INDEX)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/01/24
		 */
		@Override
		public long handle(NativeCPU __cpu, int... __args)
			throws VMSystemCallException
		{
			// Just checks to see if the system call is implemented
			if (SystemCallHandler.of(__args[0]) != null)
				return 1;
			return 0;
		}
	},
	
	/** {@link SystemCallIndex#STATIC_VM_ATTRIBUTES}. */
	STATIC_VM_ATTRIBUTES(SystemCallIndex.STATIC_VM_ATTRIBUTES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/01/24
		 */
		@Override
		public long handle(NativeCPU __cpu, int... __args)
			throws VMSystemCallException
		{
			return __cpu.vmAttribHandle.id;
		}
	},
	
	/* End. */
	;
	
	/** Lookup for system call handlers by ID. */
	private static final SystemCallHandler[] _LOOKUP =
		SystemCallHandler.__buildLookupTable();
	
	/** The system call ID. */
	protected final int id;
	
	/**
	 * Initializes the handler instance.
	 * 
	 * @param __id The identifier for the system call.
	 * @since 2021/01/24
	 */
	SystemCallHandler(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * Handles the given system call.
	 * 
	 * @param __cpu The CPU used.
	 * @param __args The arguments to the call.
	 * @return The resultant system call value.
	 * @throws VMSystemCallException On any error.
	 * @since 2021/01/24
	 */
	public abstract long handle(NativeCPU __cpu, int... __args)
		throws VMSystemCallException;
	
	/**
	 * Returns the handler for the system call.
	 * 
	 * @param __id The ID to look for.
	 * @return The handler for the given ID or {@code null} if not supported.
	 * @since 2021/01/24
	 */
	public static SystemCallHandler of(int __id)
	{
		if (__id < 0)
			return null;
		
		// Out of bounds?
		SystemCallHandler[] lookup = SystemCallHandler._LOOKUP;
		if (__id >= lookup.length)
			return null;
		
		return lookup[__id];
	}
	
	/**
	 * Builds a lookup table for system calls.
	 * 
	 * @return The built lookup table.
	 * @since 2021/01/24
	 */
	private static SystemCallHandler[] __buildLookupTable()
	{
		// Build lookup table which maps indexes to handlers
		List<SystemCallHandler> lookup = new ArrayList<>(
			SystemCallIndex.NUM_SYSCALLS);
		for (SystemCallHandler handler : SystemCallHandler.values())
		{
			int id = handler.id;
			
			// Grow the list until it can fit this ID
			while (lookup.size() <= id)
				lookup.add(null);
			
			// Set lookup at position
			if (lookup.set(id, handler) != null)
				throw Debugging.oops("Duplicate ID: " + handler);
		}
		
		// Store into the lookup table
		return lookup.<SystemCallHandler>toArray(
			new SystemCallHandler[lookup.size()]);
	}
}
