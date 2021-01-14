// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.jvm.summercoat.constants.BootstrapConstants;
import cc.squirreljme.jvm.summercoat.constants.MemHandleKind;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.io.ChunkSection;

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
	 * @throws IllegalArgumentException If {@code __kind} is not valid or
	 * the size is negative.
	 * @since 2020/12/21
	 */
	public ChunkMemHandle alloc(int __kind, int __sz)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BC08 Invalid kind. (The kind}}
		if (__kind <= MemHandleKind.UNDEFINED ||
			__kind >= MemHandleKind.NUM_KINDS)
			throw new IllegalArgumentException("BC08 " + __kind);
		
		return this.<ChunkMemHandle>__register(new ChunkMemHandle(__kind,
			this.__nextId(), this.memActions, __sz));
	}
	
	/**
	 * This allocates a handle for class information.
	 * 
	 * @param __cl The class to refer to.
	 * @return The allocated class information handle.
	 * @since 2020/12/20
	 */
	public ClassInfoHandle allocClassInfo(ClassState __cl)
	{
		return this.<ClassInfoHandle>__register(
			new ClassInfoHandle(this.__nextId(), this.memActions, __cl));
	}
	
	/**
	 * Allocates storage for referring to multiple classes.
	 * 
	 * @param __count The number of classes to store for.
	 * @return Allocated class information data.
	 * @since 2021/01/10
	 */
	public ClassInfoListHandle allocClassInfos(int __count)
	{
		return this.<ClassInfoListHandle>__register(
			new ClassInfoListHandle(this.__nextId(), this.memActions,
			__count));
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
		
		// Setup array which is a copy of the values
		int n = __classes.length;
		ClassInfoListHandle rv = this.allocClassInfos(n);
		
		// Initialize all handles
		for (int i = 0; i < n; i++)
			rv.set(i, __classes[i]._classInfoHandle);
		
		return rv;
	}
	
	/**
	 * Allocates storage for field data.
	 * 
	 * @param __sz The number of bytes to allocate.
	 * @return The memory handle for field storage.
	 * @since 2020/12/21
	 */
	public ChunkMemHandle allocFields(int __sz)
	{
		return this.alloc(MemHandleKind.STATIC_FIELD_DATA, __sz);
	}
	
	/**
	 * Allocates the object data.
	 * 
	 * @param __sz The number of bytes to allocate.
	 * @return The allocated object.
	 * @since 2021/01/10
	 */
	public ChunkMemHandle allocObject(int __sz)
	{
		return this.alloc(MemHandleKind.OBJECT_INSTANCE, __sz);
	}
	
	/**
	 * Allocates a pool handle.
	 * 
	 * @param __count The number of entries to place in.
	 * @return The allocated handle.
	 * @throws IllegalArgumentException If size is zero or negative.
	 * @since 2020/12/29
	 */
	public PoolHandle allocPool(int __count)
		throws IllegalArgumentException
	{
		return this.<PoolHandle>__register(
			new PoolHandle(this.__nextId(), this.memActions, __count));
	}
	
	/**
	 * Writes the memory handles and actions to the output chunk.
	 * 
	 * @param __outData The destination.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/13
	 */
	protected void chunkOut(ChunkSection __outData)
		throws IOException, NullPointerException
	{
		if (__outData == null)
			throw new NullPointerException("NARG");
		
		// Process every handle that is available
		MemActions memActions = this.memActions;
		for (MemHandle handle : this._handles.values())
		{
			// Write handle information
			__outData.writeInt(handle.id);
			__outData.writeUnsignedShortChecked(handle.byteSize);
			
			// Write all of the various actions out
			MemHandleActions actions = memActions.optional(handle);
			if (actions != null)
			{
				// Internal short storage
				List<Object> iStore = actions._iStore;
				short[] iMap = actions._iMap;
				
				// Go through the mapping table to find entries to record
				for (int i = 0, n = iMap.length; i < n; i++)
				{
					throw Debugging.todo();
				}
			}
			
			// End of action sequence and guard
			__outData.writeByte(0);
			__outData.writeInt(0xE3F4C2B1);
		}
		
		// End of sequence and guard
		__outData.writeInt(0);
		__outData.writeInt(0xFEFFEFFF);
	}
	
	/**
	 * Returns the next ID for memory handles.
	 * 
	 * @return The next ID for handles.
	 * @since 2021/01/02
	 */
	private int __nextId()
	{
		synchronized (this)
		{
			// Always use the same class of numbers (even/odd)
			int rv = this._nextId;
			this._nextId = rv + 2;
			
			return BootstrapConstants.HANDLE_SECURITY_BITS | rv;
		}
	}
	
	/**
	 * Registers the given memory handle.
	 * 
	 * @param <H> The type of handle to register.
	 * @param __handle The handle to register.
	 * @return {@code __handle}.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/02
	 */
	private <H extends MemHandle> H __register(H __handle)
		throws NullPointerException
	{
		if (__handle == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			// {@squirreljme.error BC02 Duplicated handle ID. (The ID)}
			if (null != this._handles.put(__handle.id, __handle))
				throw new IllegalStateException("BC02 " + __handle.id);
		}
		
		return __handle;
	}
}
