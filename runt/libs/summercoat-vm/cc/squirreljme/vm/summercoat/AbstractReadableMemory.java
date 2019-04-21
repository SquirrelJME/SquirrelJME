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
 * This is a class which handles reading of memory by using pure integers
 * instead of forcing all implementations to implement short and byte
 * readers.
 *
 * @since 2019/04/21
 */
public abstract class AbstractReadableMemory
	implements ReadableMemory
{
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public byte memReadByte(int __addr)
	{
		// Read entire integer chunk
		int ra = __addr & (~3),
			rv = this.memReadInt(ra);
		
		// Return only the desired part of it
		return (byte)(rv >>> (24 - (8 * (__addr & 3))));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public short memReadShort(int __addr)
	{
		// {@squirreljme.error AE0x Unaligned memory access.}
		if ((__addr & 1) != 0)
			throw new VMRuntimeException("AE0x");
		
		// Read entire integer chunk
		int ra = __addr & (~3),
			rv = this.memReadInt(ra);
		
		// Return only the desired part of it
		return (short)(rv >>> (16 - (16 * (__addr & 1))));
	}
}

