// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.io.ChunkFuture;

/**
 * Actions for a single {@link MemHandle}.
 *
 * @since 2021/01/10
 */
public final class MemHandleActions
{
	/** The size of the area stored. */
	protected final int byteCount;
	
	/** Storage for written values. */
	private final List<Object> _iStore =
		new ArrayList<>();
	
	/** The index value mapping. */
	private final byte[] _iMap;
	
	/**
	 * Initializes the memory handle actions.
	 * 
	 * @param __byteSize The number of bytes to store.
	 * @throws IllegalArgumentException If the byte count is negative.
	 * @since 2021/01/10
	 */
	public MemHandleActions(int __byteSize)
		throws IllegalArgumentException
	{
		if (__byteSize < 0)
			throw new IllegalArgumentException("NEGV");
		
		this.byteCount = __byteSize;
		this._iMap = new byte[__byteSize];
		
		// The first value is always null as it is a null reference!
		this._iStore.add(null);
	}
	
	/**
	 * Reads from the given handle.
	 * 
	 * @param <V> The class type of the represented value.
	 * @param __cl The class type of the represented value.
	 * @param __type The type of value to read.
	 * @param __off The offset of the value.
	 * @return The read value.
	 * @throws ClassCastException If the class is not the expected class.
	 * @throws IllegalStateException If the address contains multiple values.
	 * @throws IndexOutOfBoundsException If the offset is out of bounds.
	 * @since 2021/01/10
	 */
	protected <V> V read(Class<V> __cl, MemoryType __type, int __off)
		throws ClassCastException, IllegalStateException,
			IndexOutOfBoundsException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		if (__off < 0 || (__off + __type.lastOffset) >= this.byteCount)
			throw new IndexOutOfBoundsException("IOOB " + __off);
		if ((__off % __type.byteCount) != 0)
			throw new IllegalArgumentException("ALGN " + __off);
		
		// The read storage ID
		byte storageId = 0;
		
		// Check to make sure a value is not being overwritten so that
		// everywhere is only written to once ever.
		byte[] iMap = this._iMap;
		for (int i = 0, n = __type.byteCount; i < n; i++)
		{
			byte readId = iMap[__off + i];
			
			// On the first read, all addresses must be the same as read
			if (storageId == 0)
				storageId = readId;
			
			// {@squirreljme.error BC0d Address contains multiple values.}
			else if (readId != storageId)
				throw new IllegalStateException("BC0d");
		}
		
		// No value is contained here
		if (storageId == 0)
			return null;
		
		// Read whatever storage was specified as being here and make sure it
		// is the type we expected it to be.
		return __cl.cast(this._iStore.get(storageId & 0xFF));
	}
	
	/**
	 * Writes the given value at the given address.
	 * 
	 * @param __type The type of value write.
	 * @param __off The offset to write at.
	 * @param __oVal The value to store.
	 * @throws ClassCastException If the input type is not valid.
	 * @throws IllegalArgumentException If the offset is not an aligned
	 * multiple of four.
	 * @throws IllegalStateException If the value has already been written to.
	 * @throws IndexOutOfBoundsException If the offset is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/10
	 */
	public void write(MemoryType __type, int __off, Object __oVal)
		throws ClassCastException, IllegalArgumentException,
			IllegalStateException, IndexOutOfBoundsException,
			NullPointerException
	{
		if (__oVal == null || __type == null)
			throw new NullPointerException("NARG");
		if (__off < 0 || (__off + __type.lastOffset) >= this.byteCount)
			throw new IndexOutOfBoundsException("IOOB " + __off);
		if ((__off % __type.byteCount) != 0)
			throw new IllegalArgumentException("ALGN " + __off);
		
		// {@squirreljme.error BC07 Invalid object specified. (The object)}
		if (!((__oVal instanceof Integer) || (__oVal instanceof MemHandle) ||
			(__oVal instanceof ChunkFuture)))
			throw new ClassCastException("BC07 " + __oVal);
		
		// Check for storage ID overflow
		// {@squirreljme.error BC0b Too many values have been written to the
		// memory handle. (The written value count)}
		List<Object> iStore = this._iStore;
		int nextId = iStore.size();
		if ((byte)nextId == 0)
			throw new IllegalStateException("BC0b " + nextId);
		
		// Check to make sure a value is not being overwritten so that
		// everywhere is only written to once ever.
		// {@squirreljme.error BC0c Value is being overwritten.}
		byte[] iMap = this._iMap;
		for (int i = 0, n = __type.byteCount; i < n; i++)
			if (iMap[__off + i] != 0)
				throw new IllegalStateException("BC0c");
		
		// Value is stored here
		iStore.add(nextId, __oVal);
		
		// Store value here
		for (int i = 0, n = __type.byteCount; i < n; i++)
			iMap[__off + i] = (byte)nextId; 
	}
}
