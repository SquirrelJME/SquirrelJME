// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp;

import net.multiphasicapps.squirreljme.memory.AbstractMemoryPool;
import net.multiphasicapps.squirreljme.memory.MemoryIOException;
import net.multiphasicapps.squirreljme.memory.MemoryPool;

/**
 * This is a memory pool which is available for usage by the interpreter.
 *
 * @since 2016/06/05
 */
public class InterpreterMemoryPool
	extends AbstractMemoryPool
{
	/** The bytes which make up the active memory. */
	protected final byte[] memory;
	
	/** The base address of this pool. */
	protected final long baseaddr;
	
	/**
	 * Initializes the memory pool to use the given number of bytes.
	 *
	 * @param __bytes The bytes to use.
	 * @param __baseaddr The base address of the memory pool, this value is
	 * unsigned.
	 * @throws IllegalArgumentException On null arguments.
	 * @since 2016/06/05
	 */
	public InterpreterMemoryPool(int __bytes, long __baseaddr)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AN03 The number of bytes to use in the memory
		// pool cannot be negative or a non-power of two.}
		if (__bytes <= 0 || Integer.bitCount(__bytes) != 1)
			throw new IllegalArgumentException("AN03");
		
		// Allocate backing buffer
		this.memory = new byte[__bytes];
		this.baseaddr = __baseaddr;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/08
	 */
	@Override
	public long baseAddress()
	{
		return this.baseaddr;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/08
	 */
	@Override
	public boolean compareAndSetInt(long __addr, int __d, int __v)
		throws MemoryIOException
	{
		// Get
		byte[] memory = this.memory;
		int addr = (int)__addr;
		
		// Lock
		synchronized (memory)
		{
			// Read integer value
			int was = readInt(__addr, false);
			
			// Is the value?
			if (was == __d)
			{
				writeInt(__addr, true, __v);
				return true;
			}
			
			// Was not
			else
				return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public byte readByte(long __addr, boolean __at)
		throws MemoryIOException
	{
		// Check
		__checkRange(__addr, 1);
		
		// Get
		byte[] memory = this.memory;
		int addr = (int)__addr;
		
		// Atomic?
		if (__at)
			synchronized (memory)
			{
				return memory[addr];
			}
		
		// Not atomic?
		else
			return memory[addr];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/06
	 */
	@Override
	public int readInt(long __addr, boolean __at)
		throws MemoryIOException
	{
		// Check
		__checkRange(__addr, 4);
		
		// Get
		byte[] memory = this.memory;
		int addr = (int)__addr;
		
		// Atomic?
		if (__at)
			synchronized (this)
			{
				return ((memory[addr] << 24) |
					((memory[addr + 1] & 0xFF) << 16) |
					((memory[addr + 2] & 0xFF) << 8) |
					((memory[addr + 3] & 0xFF)));
			}
		
		// Not atomic?
		else
			return ((memory[addr] << 24) |
				((memory[addr + 1] & 0xFF) << 16) |
				((memory[addr + 2] & 0xFF) << 8) |
				((memory[addr + 3] & 0xFF)));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/06
	 */
	@Override
	public long readLong(long __addr, boolean __at)
		throws MemoryIOException
	{
		// Check
		__checkRange(__addr, 8);
		
		// Get
		byte[] memory = this.memory;
		int addr = (int)__addr;
		
		// Atomic?
		if (__at)
			synchronized (this)
			{
				return (long)((memory[addr] << 56) |
					(long)((memory[addr + 1] & 0xFF) << 48) |
					(long)((memory[addr + 2] & 0xFF) << 40) |
					(long)((memory[addr + 3] & 0xFF) << 32) |
					((memory[addr + 4] & 0xFF) << 24) |
					((memory[addr + 5] & 0xFF) << 16) |
					((memory[addr + 6] & 0xFF) << 8) |
					((memory[addr + 7] & 0xFF)));
			}
		
		// Not atomic?
		else
			return (long)((memory[addr] << 56) |
				(long)((memory[addr + 1] & 0xFF) << 48) |
				(long)((memory[addr + 2] & 0xFF) << 40) |
				(long)((memory[addr + 3] & 0xFF) << 32) |
				((memory[addr + 4] & 0xFF) << 24) |
				((memory[addr + 5] & 0xFF) << 16) |
				((memory[addr + 6] & 0xFF) << 8) |
				((memory[addr + 7] & 0xFF)));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/06
	 */
	@Override
	public short readShort(long __addr, boolean __at)
		throws MemoryIOException
	{
		// Check
		__checkRange(__addr, 2);
		
		// Get
		byte[] memory = this.memory;
		int addr = (int)__addr;
		
		// Atomic?
		if (__at)
			synchronized (this)
			{
				return (short)((memory[addr] << 8) |
					(memory[addr + 1] & 0xFF));
			}
		
		// Not atomic?
		else
			return (short)((memory[addr] << 8) |
				(memory[addr + 1] & 0xFF));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public final long size()
	{
		return this.memory.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/06
	 */
	@Override
	public void writeByte(long __addr, boolean __at, byte __v)
		throws MemoryIOException
	{
		// Check
		__checkRange(__addr, 1);
		
		// Get
		byte[] memory = this.memory;
		int addr = (int)__addr;
		
		// Atomic?
		if (__at)
			synchronized (this)
			{
				memory[addr] = __v;
			}
		
		// Not atomic?
		else
			memory[addr] = __v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/06
	 */
	@Override
	public void writeInt(long __addr, boolean __at, int __v)
		throws MemoryIOException
	{
		// Check
		__checkRange(__addr, 4);
		
		// Get
		byte[] memory = this.memory;
		int addr = (int)__addr;
		
		// Atomic?
		if (__at)
			synchronized (this)
			{
				memory[addr] = (byte)(__v >>> 24);
				memory[addr + 1] = (byte)(__v >>> 16);
				memory[addr + 2] = (byte)(__v >>> 8);
				memory[addr + 3] = (byte)__v;
			}
		
		// Not atomic?
		else
		{
			memory[addr] = (byte)(__v >>> 24);
			memory[addr + 1] = (byte)(__v >>> 16);
			memory[addr + 2] = (byte)(__v >>> 8);
			memory[addr + 3] = (byte)__v;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/06
	 */
	@Override
	public void writeLong(long __addr, boolean __at, long __v)
		throws MemoryIOException
	{
		// Check
		__checkRange(__addr, 8);
		
		// Get
		byte[] memory = this.memory;
		int addr = (int)__addr;
		
		// Atomic?
		if (__at)
			synchronized (this)
			{
				memory[addr] = (byte)(__v >>> 56);
				memory[addr + 1] = (byte)(__v >>> 48);
				memory[addr + 2] = (byte)(__v >>> 40);
				memory[addr + 3] = (byte)(__v >>> 32);
				memory[addr + 4] = (byte)(__v >>> 24);
				memory[addr + 5] = (byte)(__v >>> 16);
				memory[addr + 6] = (byte)(__v >>> 8);
				memory[addr + 7] = (byte)__v;
			}
		
		// Not atomic?
		else
		{
			memory[addr] = (byte)(__v >>> 56);
			memory[addr + 1] = (byte)(__v >>> 48);
			memory[addr + 2] = (byte)(__v >>> 40);
			memory[addr + 3] = (byte)(__v >>> 32);
			memory[addr + 4] = (byte)(__v >>> 24);
			memory[addr + 5] = (byte)(__v >>> 16);
			memory[addr + 6] = (byte)(__v >>> 8);
			memory[addr + 7] = (byte)__v;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/06
	 */
	@Override
	public void writeShort(long __addr, boolean __at, short __v)
		throws MemoryIOException
	{
		// Check
		__checkRange(__addr, 2);
		
		// Get
		byte[] memory = this.memory;
		int addr = (int)__addr;
		
		// Atomic?
		if (__at)
			synchronized (this)
			{
				memory[addr] = (byte)(__v >>> 8);
				memory[addr + 1] = (byte)__v;
			}
		
		// Not atomic?
		else
		{
			memory[addr] = (byte)(__v >>> 8);
			memory[addr + 1] = (byte)__v;
		}
	}
	
	/**
	 * Checks the range of the address and the length to make sure that it is
	 * a valid operation.
	 *
	 * @param __addr The address to read from.
	 * @param __long The number of bytes to read.
	 * @throws MemoryIOException If the address is out of range.
	 * @since 2016/06/05
	 */
	private void __checkRange(long __addr, int __len)
		throws MemoryIOException
	{
		// {@squirreljme.error AN04 Access of address by reading/writing the
		// given length is not within range. (The address; The length)}
		if (__addr < 0L || __len <= 0L ||
			(__addr + __len) >= (long)this.memory.length)
			throw new MemoryIOException(String.format("AN04 %d", __addr,
				__len));
	}
}

