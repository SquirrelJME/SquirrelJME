// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

import cc.squirreljme.emulator.vm.VMException;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Represents an allocation link.
 *
 * @since 2023/12/14
 */
public final class AllocLink
	implements Destructable, Pointer
{
	/** The pointer to the memory block. */
	private final long _blockPtr;
	
	/** The pointer to the link block. */
	private final long _linkPtr;
	
	/**
	 * Initializes the allocation link.
	 *
	 * @param __blockPtr The block pointer address.
	 * @param __linkPtr The link pointer address.
	 * @throws NullPointerException If either address is null.
	 * @since 2023/12/14
	 */
	AllocLink(long __blockPtr, long __linkPtr)
		throws NullPointerException
	{
		if (__blockPtr == 0 || __linkPtr == 0)
			throw new NullPointerException("Null AllocLink.");
		
		this._blockPtr = __blockPtr;
		this._linkPtr = __linkPtr;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/16
	 */
	@Override
	public void close()
		throws VMException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the link address.
	 *
	 * @return The address of the link.
	 * @since 2023/12/14
	 */
	public long linkAddress()
	{
		return this._linkPtr;
	}
	
	/**
	 * Reads bytes at the given offset.
	 *
	 * @param __at The offset to read from.
	 * @param __b The buffer to read into.
	 * @throws IndexOutOfBoundsException If the address is not valid or the
	 * number of bytes exceeds the allocation link size.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If it could not be read.
	 * @since 2023/12/17
	 */
	public void read(int __at, byte[] __b)
		throws IndexOutOfBoundsException, NullPointerException, VMException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		this.read(__at, __b, 0, __b.length);
	}
	
	/**
	 * Reads bytes at the given offset.
	 *
	 * @param __at The offset to read from.
	 * @param __b The buffer to read into.
	 * @param __o The offset into the buffer.
	 * @param __l The length of the buffer.
	 * @throws IndexOutOfBoundsException If the address is not valid or the
	 * number of bytes exceeds the allocation link size; or the offset
	 * and/or length are negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If it could not be read.
	 * @since 2023/12/17
	 */
	public void read(int __at, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException, VMException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Check size first
		int size = this.size();
		if (__at < 0 || __at >= size || (__at + __l) > size ||
			(__at + __l) < 0 || __o < 0 || __l < 0 ||
			(__o + __l) > __b.length || (__o + __l) < 0)
			throw new IndexOutOfBoundsException(
				String.format("read(%d, %h, %d, %d) in %d from %d",
					__at, __b, __o, __l, size, __b.length));
		
		// Call native read function
		AllocLink.__read(this._blockPtr, __at, __b, __o, __l);
	}
	
	public int readInt(int __at)
		throws IndexOutOfBoundsException, VMException
	{
		byte[] buf = new byte[4];
		
		// Read in bytes.
		this.read(__at, buf, 0, 4);
		
		// Map bytes.
		int result = 0;
		if (Utils.isBigEndian())
		{
			result |= ((buf[0] << 24) & 0xFF000000);
			result |= ((buf[1] << 16) & 0xFF0000);
			result |= ((buf[2] << 8) & 0xFF00);
			result |= ((buf[3]) & 0xFF);
		}
		else
		{
			result |= ((buf[0]) & 0xFF);
			result |= ((buf[1] << 8) & 0xFF00);
			result |= ((buf[2] << 16) & 0xFF0000);
			result |= ((buf[3] << 24) & 0xFF000000);
		}
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/14
	 */
	@Override
	public long pointerAddress()
	{
		return this._blockPtr;
	}
	
	/**
	 * Returns the size of the allocation link.
	 *
	 * @return The size of the link.
	 * @throws VMException If it could not be calculated.
	 * @since 2023/12/14
	 */
	public int size()
		throws VMException
	{
		return AllocLink.__size(this._linkPtr);
	}
	
	/**
	 * Writes the given bytes at the address.
	 *
	 * @param __at The address to write at.
	 * @param __b The bytes to write.
	 * @throws IndexOutOfBoundsException If the address is not valid or the
	 * number of bytes exceeds the allocation link size.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If it could not be written.
	 * @since 2023/12/16
	 */
	public void write(int __at, byte[] __b)
		throws IndexOutOfBoundsException, NullPointerException, VMException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		this.write(__at, __b, 0, __b.length);
	}
	
	/**
	 * Writes the given bytes at the address.
	 *
	 * @param __at The address to write at.
	 * @param __b The bytes to write.
	 * @param __o The offset into the buffer.
	 * @param __l The number of bytes to copy.
	 * @throws IndexOutOfBoundsException If the address is not valid or the
	 * number of bytes exceeds the allocation link size; or the offset
	 * and/or length are negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If it could not be written.
	 * @since 2023/12/16
	 */
	public void write(int __at, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException, VMException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Check size first
		int size = this.size();
		if (__at < 0 || __at >= size || (__at + __l) > size ||
			(__at + __l) < 0 || __o < 0 || __l < 0 ||
			(__o + __l) > __b.length || (__o + __l) < 0)
			throw new IndexOutOfBoundsException(
				String.format("write(%d, %h, %d, %d) in %d from %d",
					__at, __b, __o, __l, size, __b.length));
		
		// Call native write function
		AllocLink.__write(this._blockPtr, __at, __b, __o, __l);
	}
	
	/**
	 * Writes the value at the given address.
	 *
	 * @param __at The address to write at.
	 * @param __value The value to write.
	 * @throws IndexOutOfBoundsException If the address is out of range.
	 * @throws VMException If it could not be written.
	 * @since 2023/12/16
	 */
	public void writeInt(int __at, int __value)
		throws IndexOutOfBoundsException, VMException
	{
		int size = this.size();
		if (__at < 0 || (__at + 4) > size)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Map bytes
		byte[] buf = new byte[4];
		if (Utils.isBigEndian())
		{
			buf[0] = (byte)(__value >>> 24);
			buf[1] = (byte)(__value >>> 16);
			buf[2] = (byte)(__value >>> 8);
			buf[3] = (byte)(__value);
		}
		else
		{
			buf[0] = (byte)(__value);
			buf[1] = (byte)(__value >>> 8);
			buf[2] = (byte)(__value >>> 16);
			buf[3] = (byte)(__value >>> 24);
		}
		
		// Write them
		this.write(__at, buf, 0, 4);
	}
	
	/**
	 * Writes the value at the given address.
	 *
	 * @param __at The address to write at.
	 * @param __value The value to write.
	 * @throws IndexOutOfBoundsException If the address is out of range.
	 * @throws VMException If it could not be written.
	 * @since 2023/12/16
	 */
	public void writeLong(int __at, long __value)
		throws IndexOutOfBoundsException, VMException
	{
		int size = this.size();
		if (__at < 0 || (__at + 8) > size)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Map bytes
		byte[] buf = new byte[8];
		if (Utils.isBigEndian())
		{
			buf[0] = (byte)(__value >>> 56);
			buf[1] = (byte)(__value >>> 48);
			buf[2] = (byte)(__value >>> 40);
			buf[3] = (byte)(__value >>> 32);
			buf[4] = (byte)(__value >>> 24);
			buf[5] = (byte)(__value >>> 16);
			buf[6] = (byte)(__value >>> 8);
			buf[7] = (byte)(__value);
		}
		else
		{
			buf[0] = (byte)(__value);
			buf[1] = (byte)(__value >>> 8);
			buf[2] = (byte)(__value >>> 16);
			buf[3] = (byte)(__value >>> 24);
			buf[4] = (byte)(__value >>> 32);
			buf[5] = (byte)(__value >>> 40);
			buf[6] = (byte)(__value >>> 48);
			buf[7] = (byte)(__value >>> 56);
		}
		
		// Write them
		this.write(__at, buf, 0, 8);
	}
	
	/**
	 * Writes a pointer value.
	 *
	 * @param __at The address to write at.
	 * @param __pointer The pointer to write.
	 * @return The size of the pointer, the number of bytes to move forward
	 * for an adjacent write.
	 * @throws IndexOutOfBoundsException If the address is not valid.
	 * @throws VMException If the pointer could not be written.
	 * @since 2023/12/16
	 */
	public int writePointer(int __at, long __pointer)
		throws IndexOutOfBoundsException, VMException
	{
		// What is the pointer size?
		int pointerSize = Utils.pointerSize();
		switch (pointerSize)
		{
			case 4:
				this.writeInt(__at, (int)__pointer);
				return pointerSize;
				
			case 8:
				this.writeLong(__at, __pointer);
				return pointerSize;
				
			default:
				throw Debugging.todo(pointerSize);
		}
	}
	
	/**
	 * Initializes a link from the given block pointer.
	 *
	 * @param __blockPtr The block pointer.
	 * @return The resultant link.
	 * @since 2023/12/20
	 */
	public static AllocLink ofBlockPtr(long __blockPtr)
	{
		if (__blockPtr == 0)
			return null;
		
		return new AllocLink(__blockPtr, AllocPool.__getLink(__blockPtr));
	}
	
	/**
	 * Reads bytes at the given offset.
	 *
	 * @param __blockPtr The block pointer.
	 * @param __at The offset to read from.
	 * @param __b The buffer to read into.
	 * @param __o The offset into the buffer.
	 * @param __l The length of the buffer.
	 * @throws VMException If it could not be read.
	 * @since 2023/12/17
	 */
	private static native void __read(long __blockPtr, int __at,
		byte[] __b, int __o, int __l)
		throws VMException;
	
	/**
	 * Returns the size of the allocation pointer. 
	 *
	 * @param __linkPtr The link pointer.
	 * @return The resultant size.
	 * @throws VMException If it could not be determined.
	 * @since 2023/12/14
	 */
	private static native int __size(long __linkPtr)
		throws VMException;
	
	/**
	 * Writes the given bytes at the address.
	 *
	 * @param __blockPtr The block pointer.
	 * @param __at The address to write at.
	 * @param __b The buffer to write.
	 * @param __o The offset into the buffer.
	 * @param __l The length of the buffer.
	 * @throws VMException If it could not be written.
	 * @since 2023/12/16
	 */
	private static native void __write(long __blockPtr, int __at,
		byte[] __b, int __o, int __l)
		throws VMException;
}
