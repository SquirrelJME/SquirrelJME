// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

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
		if (__sizes.length != JDWPIdKind.values().length)
			throw new IllegalArgumentException("IOOB");
		
		// Store sizes
		this._sizes = __sizes.clone();
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
}
