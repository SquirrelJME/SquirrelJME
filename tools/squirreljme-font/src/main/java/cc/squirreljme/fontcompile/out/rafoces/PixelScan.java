// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out.rafoces;

import cc.squirreljme.fontcompile.util.GlyphBitmap;

/**
 * Not Described.
 *
 * @since 2024/05/27
 */
public final class PixelScan
{
	/** The bitmap to be parsed. */
	protected final GlyphBitmap bitmap;
	
	/** The shape identifiers. */
	private final byte[] _ident;
	
	/**
	 * Scans the input bitmap image and processes the individual pixels to
	 * find starting points and otherwise.
	 *
	 * @param __bitmap The bitmap to scan.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/27
	 */
	public PixelScan(GlyphBitmap __bitmap)
		throws NullPointerException
	{
		if (__bitmap == null)
			throw new NullPointerException("NARG");
		
		this.bitmap = __bitmap;
		this._ident = new byte[__bitmap.width * __bitmap.height];
	}
}
