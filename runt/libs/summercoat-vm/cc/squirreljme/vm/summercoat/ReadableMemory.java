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
	public abstract byte memReadByte(int __addr);
	
	/**
	 * Reads the memory at the specified address.
	 *
	 * @param __addr The address to read from.
	 * @return The read value.
	 * @since 2019/04/21
	 */
	public abstract int memReadInt(int __addr);
	
	/**
	 * Reads the memory at the specified address.
	 *
	 * @param __addr The address to read from.
	 * @return The read value.
	 * @since 2019/04/21
	 */
	public abstract short memReadShort(int __addr);
}

