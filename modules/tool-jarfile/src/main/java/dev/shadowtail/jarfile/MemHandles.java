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
import net.multiphasicapps.io.ChunkFuture;
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
		
		// Pre-load handle IDs, so that all of them are known at once
		MemActions memActions = this.memActions;
		for (MemHandle handle : this._handles.values())
		{
			// Write handle information
			__outData.writeInt(handle.id);
			__outData.writeUnsignedShortChecked(handle._refCount);
			__outData.writeUnsignedShortChecked(handle.byteSize);
			__outData.writeByte(handle.kind);
		}
		
		// End of preload guard
		__outData.writeInt(0);
		__outData.writeInt(BootstrapConstants.PRE_SEQ_GUARD);
			
		// Process every handle that is available
		for (MemHandle handle : this._handles.values())
		{
			// Which handle is this for?
			__outData.writeInt(handle.id);
			
			// Write all of the various actions out
			MemHandleActions actions = memActions.optional(handle);
			if (actions != null)
			{
				// If there is no big endian constant data, then just
				// initialize with all zeroes
				boolean hasBigData = actions._hasBigData;
				if (!hasBigData)
					__outData.writeByte(0);
				
				// Otherwise write all the constant data
				else
				{
					__outData.writeByte(1);
					__outData.write(actions._dataBig);
				}
				
				// Internal short storage
				List<Object> iStore = actions._iStore;
				List<Integer> iAddr = actions._iAddr;
				List<MemoryType> iType = actions._iType;
				
				// Write all the address information and potential swapping
				// if any...
				for (int i = 1, n = iStore.size(); i < n; i++)
				{
					// Get what we are writing
					Object store = iStore.get(i);
					int addr = iAddr.get(i);
					MemoryType type = iType.get(i);
					boolean isConst = MemHandleActions.isConstant(store);
					
					// Indicate flipping via negative data
					if (isConst)
						__outData.writeByte(-type.byteCount);
					
					// Is a late constant
					else if (store instanceof ChunkFuture)
						__outData.writeByte(type.byteCount);
					
					// Is a memory handle
					else if (store instanceof MemHandle)
						__outData.writeByte(type.byteCount |
							BootstrapConstants.ACTION_MEMHANDLE_FLAG);
					
					// Should not occur
					else
						throw Debugging.oops(store);
					
					// Address of this position
					__outData.writeUnsignedShortChecked(addr);
					
					// Non-const data (positive) follows
					if (!isConst)
					{
						// A later future value
						if (store instanceof ChunkFuture)
							__outData.writeFuture(type.toChunkDataType(),
								(ChunkFuture)store);
						
						// Memory handle reference
						else if (store instanceof MemHandle)
						{
							MemHandle ref = (MemHandle)store;
							__outData.writeInt(ref.id);
						}
						
						// Should not occur
						else
							throw Debugging.oops(store);
					}
				}
			}
			
			// End of action sequence and guard
			__outData.writeByte(0);
			__outData.writeInt(BootstrapConstants.ACTION_SEQ_GUARD);
		}
		
		// End of sequence and guard
		__outData.writeInt(0);
		__outData.writeInt(BootstrapConstants.MEMORY_SEQ_GUARD);
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
