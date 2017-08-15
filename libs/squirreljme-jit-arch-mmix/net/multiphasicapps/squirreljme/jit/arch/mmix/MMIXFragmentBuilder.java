// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.arch.mmix;

import net.multiphasicapps.squirreljme.jit.bin.FragmentBuilder;

/**
 * This is a fragment builder which more easily generates MMIX machine code
 * without needing the machine code generator to have instruction generation.
 *
 * @since 2017/08/14
 */
public class MMIXFragmentBuilder
	extends FragmentBuilder
{
	/** Temporary buffer. */
	private final byte[] _buf =
		new byte[4];
	
	/**
	 * Appends a MMIX operation.
	 *
	 * @param __op The operation code.
	 * @param __x The X parameter, masked to 8-bits.
	 * @throws IllegalArgumentException If the input operations are within
	 * bounds.
	 * @since 2017/08/15
	 */
	public final void appendX(int __op, int __x)
		throws IllegalArgumentException
	{
		appendXYZ(__op, __x, 0, 0);
	}
	
	/**
	 * Appends a MMIX operation.
	 *
	 * @param __op The operation code.
	 * @param __x The X parameter, masked to 8-bits.
	 * @param __y The Y parameter, masked to 8-bits.
	 * @throws IllegalArgumentException If the input operations are within
	 * bounds.
	 * @since 2017/08/15
	 */
	public final void appendXY(int __op, int __x, int __y)
		throws IllegalArgumentException
	{
		appendXYZ(__op, __x, __y, 0);
	}
	
	/**
	 * Appends a MMIX operation.
	 *
	 * @param __op The operation code.
	 * @param __x The X parameter, masked to 8-bits.
	 * @param __y The Y parameter, masked to 8-bits.
	 * @param __z The Z parameter, masked to 8-bits.
	 * @throws IllegalArgumentException If the input operations are within
	 * bounds.
	 * @since 2017/08/15
	 */
	public final void appendXYZ(int __op, int __x, int __y, int __z)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BL01 The MMIX operation code is out of range,
		// (The operation)}
		if (__op < 0 || __op > 255)
			throw new IllegalArgumentException(String.format("BL01 %d", __op));
		
		// Build operation and append
		byte[] buf = this._buf;
		buf[0] = (byte)__op;
		buf[1] = (byte)__x;
		buf[2] = (byte)__y;
		buf[3] = (byte)__z;
		append(buf, 0, 4);
	}
	
	/**
	 * Appends a MMIX operation.
	 *
	 * @param __op The operation code.
	 * @param __x The X parameter, masked to 8-bits.
	 * @param __yz The YZ parameter, masked to 16-bits.
	 * @throws IllegalArgumentException If the input operations are within
	 * bounds.
	 * @since 2017/08/15
	 */
	public final void appendXYZ16(int __op, int __x, int __yz)
		throws IllegalArgumentException
	{
		appendXYZ(__op, __x, __yz >> 8, __yz & 0x255);
	}
	
	/**
	 * Appends a MMIX operation.
	 *
	 * @param __op The operation code.
	 * @param __xyz The XYZ parameter, masked to 24-bits.
	 * @throws IllegalArgumentException If the input operations are within
	 * bounds.
	 * @since 2017/08/15
	 */
	public final void appendXYZ24(int __op, int __xyz)
		throws IllegalArgumentException
	{
		appendXYZ(__op, __xyz >> 16, (__xyz >> 8) & 255, __xyz & 0x255);
	}
}

