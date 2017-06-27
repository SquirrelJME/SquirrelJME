// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

import java.lang.ref.Reference;
import net.multiphasicapps.util.datadeque.ByteDeque;

/**
 * This class is used to build sections which are placed in the section table.
 * Since {@link Section} data should be as immutable as possible, this allows
 * such sections to be dynamically created.
 *
 * @since 2017/06/23
 */
public class SectionBuilder
	extends __SubState__
{
	/** Deque for the bytes within the section. */
	protected final ByteDeque bytes =
		new ByteDeque();
	
	/**
	 * Initializes the section builder.
	 *
	 * @param __ls The owning linker state.
	 * @since 2017/06/23
	 */
	SectionBuilder(Reference<LinkerState> __ls)
	{
		super(__ls);
	}
	
	/**
	 * Appends a single non-dynamic byte to the output section.
	 *
	 * @param __v The byte to add.
	 * @since 2017/06/27
	 */
	public void append(byte __v)
	{
		this.bytes.addLast(__v);
	}
	
	/**
	 * Appends multiple non-dynamic bytes to the output section.
	 *
	 * @param __v The bytes to add.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to add.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceeds the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/27
	 */
	public void append(byte[] __v, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __v.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		this.bytes.addLast(__v, __o, __l);
	}
}

