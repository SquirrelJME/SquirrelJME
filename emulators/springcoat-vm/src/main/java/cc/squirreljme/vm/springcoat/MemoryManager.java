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
	/** The current lock. */
	private int _lock;
	
	/**
	 * Locks the memory manager.
	 *
	 * @param __code The locking code.
	 * @return If this was locked.
	 * @since 2020/03/04
	 */
	public boolean lock(int __code)
	{
		synchronized (this)
		{
			// Is this unlocked?
			if (this._lock == 0)
			{
				this._lock = __code;
				return true;
			}
		}
		
		return false;
	}
	
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
	 * Unlocks the allocator.
	 *
	 * @param __code The locking code.
	 * @since 2020/03/14
	 */
	public void unlock(int __code)
	{
		synchronized (this)
		{
			// Unlocking only happens if we matched the right code
			if (this._lock == __code)
			{
				this._lock = 0;
				
				// Signal any threads which are lock-polling this lock
				this.notify();
			}
		}
	}
	
	/**
	 * Waits for the lock to be cleared before continuing.
	 *
	 * @param __code The locking code we are using.
	 * @since 2020/03/14
	 */
	public void waitLock(int __code)
	{
		synchronized (this)
		{
			for (;;)
			{
				// Did we claim this lock?
				if (this.lock(__code))
					return;
				
				// Wait for a signal or to try this lock again
				try
				{
					this.wait(1000);
				}
				catch (InterruptedException e)
				{
					// Ignore
				}
			}
		}
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
