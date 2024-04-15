// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.IntegerArrayList;

/**
 * Contains all the ID sizes for the JDWP protocol.
 *
 * @since 2024/01/22
 */
public final class JDWPIdSizes
{
	/** All the various sizes. */
	private final int[] _sizes;
	
	/**
	 * Initializes the ID sizes.
	 *
	 * @param __sizes The sizes used.
	 * @throws IllegalArgumentException If the size length is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public JDWPIdSizes(int... __sizes)
		throws IllegalArgumentException, NullPointerException
	{
		if (__sizes == null)
			throw new NullPointerException("NARG");
		if (__sizes.length != JDWPIdKind.NUM_KINDS)
			throw new IllegalArgumentException("IOOB");
		
		// Normalize everything
		JDWPIdKind[] kinds = JDWPIdKind.values();
		int[] normalized = new int[kinds.length];
		System.arraycopy(__sizes, 0,
			normalized, 0, JDWPIdKind.NUM_KINDS);
		
		// Normalize some sizes that are aliases
		for (JDWPIdKind kind : kinds)
			if (kind.position < 0)
				normalized[kind.ordinal()] =
					normalized[JDWPIdKind.invert(kind.position)];
		
		// Store sizes
		this._sizes = normalized;
	}
	
	/**
	 * Returns the size of the given type.
	 *
	 * @param __idSize The identifier size used.
	 * @return The resultant size.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public int getSize(JDWPIdKind __idSize)
		throws NullPointerException
	{
		if (__idSize == null)
			throw new NullPointerException("NARG");
		
		return this._sizes[__idSize.ordinal()];
	}
	
	/**
	 * Returns the size of the given type.
	 *
	 * @param __idSize The identifier size used.
	 * @return The resultant size.
	 * @throws IndexOutOfBoundsException If the kind is not valid.
	 * @since 2024/01/22
	 */
	public int getSize(int __idSize)
		throws IndexOutOfBoundsException
	{
		if (__idSize < 0 || __idSize >= JDWPIdKind.NUM_KINDS)
			throw new IndexOutOfBoundsException("IOOB");
		
		return this._sizes[__idSize];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/23
	 */
	@Override
	public String toString()
	{
		return new IntegerArrayList(this._sizes).toString();
	}
}
