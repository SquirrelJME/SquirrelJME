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
 * This class is used to provide simple writing for types other than integers
 * into memory.
 *
 * @since 2019/04/21
 */
public abstract class AbstractWritableMemory
	extends AbstractReadableMemory
	implements WritableMemory
{
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public void memWriteByte(int __addr, int __v)
	{
		// Read entire integer chunk
		int ra = __addr & (~3),
			rv = this.memReadInt(ra);
		
		// Clip value
		__v &= 0xFF;
		
		// Replace one of the chunks
		switch (__v & 3)
		{
			case 0: rv = (rv & 0x00FFFFFF) | (__v << 24); break;
			case 1: rv = (rv & 0xFF00FFFF) | (__v << 16); break;
			case 2: rv = (rv & 0xFFFF00FF) | (__v << 8); break;
			case 3: rv = (rv & 0xFFFFFF00) | (__v); break;
		}
		
		// Store integer chunk
		this.memWriteInt(ra, rv);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public void memWriteShort(int __addr, int __v)
	{
		// {@squirreljme.error AE12 Unaligned memory access.}
		if ((__addr & 1) != 0)
			throw new VMRuntimeException("AE12");
		
		// Read entire integer chunk
		int ra = __addr & (~3),
			rv = this.memReadInt(ra);
		
		// Clip value
		__v &= 0xFFFF;
		
		// Replace one of the chunks
		switch (__v & 0b10)
		{
			case 0:		rv = (rv & 0x0000FFFF) | (__v << 16); break;
			default:	rv = (rv & 0xFFFF0000) | __v; break;
		}
		
		// Store integer chunk
		this.memWriteInt(ra, rv);
	}
}

