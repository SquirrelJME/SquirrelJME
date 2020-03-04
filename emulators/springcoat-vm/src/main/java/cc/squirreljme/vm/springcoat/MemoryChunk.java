// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.memory.MemoryAccessException;
import cc.squirreljme.jvm.memory.ReadableByteMemory;
import cc.squirreljme.jvm.memory.WritableByteMemory;

/**
 * This is a standard chunk of memory which just has a basic set of bytes
 * associated with it.
 *
 * @since 2020/03/03
 */
public final class MemoryChunk
	implements ReadableByteMemory, WritableByteMemory
{
	/** The size of the memory chunk. */
	protected final int size;
	
	/** The chunk bytes. */
	protected final byte[] _bytes;
	
	/**
	 * Initializes the memory chunk.
	 *
	 * @param __size The size of the chunk.
	 * @throws IllegalArgumentException If the size is zero or negative.
	 * @since 2020/03/03
	 */
	public MemoryChunk(int __size)
		throws IllegalArgumentException
	{
		if (__size <= 0)
			throw new IllegalArgumentException("Negative or zero chunk size.");
		
		this.size = __size;
		this._bytes = new byte[__size];
	}
	
	@Override
	public byte read(long __addr)
		throws MemoryAccessException
	{
		throw new todo.TODO();
	}
	
	@Override
	public void read(long __addr, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, MemoryAccessException,
		NullPointerException
	{
		throw new todo.TODO();
	}
	
	@Override
	public void write(long __addr, byte __b)
		throws MemoryAccessException
	{
		throw new todo.TODO();
	}
	
	@Override
	public void write(long __addr, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, MemoryAccessException,
		NullPointerException
	{
		throw new todo.TODO();
	}
}
