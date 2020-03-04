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
import cc.squirreljme.jvm.memory.ReadableBasicMemory;
import cc.squirreljme.jvm.memory.WritableBasicMemory;

/**
 * This class manages the allocation of memory within SpringCoat, it is used to
 * allocate and access raw blocks as required.
 *
 * @since 2020/03/03
 */
public final class MemoryManager
	implements ReadableBasicMemory, WritableBasicMemory
{
	/**
	 * {@inheritDoc}
	 * @since 2020/03/03
	 */
	@Override
	public byte read(long __addr)
		throws MemoryAccessException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/03
	 */
	@Override
	public void read(long __addr, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, MemoryAccessException,
		NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/03
	 */
	@Override
	public int readInt(long __addr)
		throws MemoryAccessException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/03
	 */
	@Override
	public long readLong(long __addr)
		throws MemoryAccessException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/03
	 */
	@Override
	public short readShort(long __addr)
		throws MemoryAccessException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/03
	 */
	@Override
	public void write(long __addr, byte __b)
		throws MemoryAccessException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/03
	 */
	@Override
	public void write(long __addr, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, MemoryAccessException,
		NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/03
	 */
	@Override
	public void writeInt(long __addr, int __v)
		throws MemoryAccessException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/03
	 */
	@Override
	public void writeLong(long __addr, long __v)
		throws MemoryAccessException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/03
	 */
	@Override
	public void writeShort(long __addr, long __v)
		throws MemoryAccessException
	{
		throw new todo.TODO();
	}
}
