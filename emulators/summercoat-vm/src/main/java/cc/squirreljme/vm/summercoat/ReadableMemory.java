// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

/**
 * This interface is used to represent anything which provides memory which
 * can be read.
 *
 * @since 2019/04/21
 */
public interface ReadableMemory
	extends Memory
{
	/**
	 * Reads the memory at the specified address.
	 *
	 * @param __addr The address to read from.
	 * @return The read value.
	 * @since 2019/04/21
	 */
	int memReadByte(int __addr);
	
	/**
	 * Bulk read of memory bytes.
	 *
	 * @param __ad The address to read from.
	 * @param __b The output bytes.
	 * @param __o The offset.
	 * @param __l The length.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/21
	 */
	void memReadBytes(int __ad, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException;
	
	/**
	 * Reads the memory at the specified address.
	 *
	 * @param __addr The address to read from.
	 * @return The read value.
	 * @since 2019/04/21
	 */
	int memReadInt(int __addr);
	
	/**
	 * Reads the memory at the specified address.
	 *
	 * @param __addr The address to read from.
	 * @return The read value.
	 * @since 2019/04/21
	 */
	int memReadShort(int __addr);
}

