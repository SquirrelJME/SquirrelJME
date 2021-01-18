// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

/**
 * This is used to provide an interface for reading memory.
 *
 * @since 2019/04/21
 */
public interface WritableMemory
	extends ReadableMemory
{
	/**
	 * Writes a value to memory.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/21
	 */
	void memWriteByte(int __addr, int __v);
	
	/**
	 * Writes multiple bytes to memory
	 *
	 * @param __addr The address to write to.
	 * @param __b The input bytes.
	 * @param __o The offset.
	 * @param __l The length.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/21
	 */
	void memWriteBytes(int __addr, byte[] __b, int __o, int __l);
	
	/**
	 * Writes the memory handle to the address.
	 * 
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2021/01/17
	 */
	void memWriteHandle(int __addr, MemHandleReference __v);
	
	/**
	 * Writes the memory handle to the address.
	 * 
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2021/01/17
	 */
	void memWriteHandle(int __addr, MemHandle __v);
	
	/**
	 * Writes a value to memory.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/21
	 */
	void memWriteInt(int __addr, int __v);
	
	/**
	 * Writes a value to memory.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2021/01/17
	 */
	void memWriteLong(int __addr, long __v);
	
	/**
	 * Writes a value to memory.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/21
	 */
	void memWriteShort(int __addr, int __v);
}

