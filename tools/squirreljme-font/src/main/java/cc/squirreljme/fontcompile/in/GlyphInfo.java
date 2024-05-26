// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.in;

import cc.squirreljme.fontcompile.util.GlyphBitmap;
import cc.squirreljme.fontcompile.util.GlyphId;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Base class for glyph information.
 *
 * @since 2024/05/17
 */
public abstract class GlyphInfo
{
	/** The glyph bitmap. */
	protected final GlyphBitmap bitmap;
	
	/** The glyph codepoint. */
	protected final GlyphId codepoint;
	
	/** The display width of the glyph. */
	protected final int displayWidth;
	
	/** The X offset. */
	protected final int offX;
	
	/** The Y offset. */
	protected final int offY;
	
	/**
	 * Initializes the glyph.
	 *
	 * @param __codepoint The glyph codepoint.
	 * @param __displayWidth The display width of the glyph.
	 * @param __bitmap The glyph bitmap.
	 * @param __offX The X offset.
	 * @param __offY The Y offset.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/26
	 */
	public GlyphInfo(GlyphId __codepoint, int __displayWidth,
		GlyphBitmap __bitmap, int __offX, int __offY)
		throws NullPointerException
	{
		if (__codepoint == null || __bitmap == null)
			throw new NullPointerException("NARG");
		
		this.codepoint = __codepoint;
		this.displayWidth = __displayWidth;
		this.bitmap = __bitmap;
		this.offX = __offX;
		this.offY = __offY;
	}
	
	/**
	 * Returns the glyph bitmap.
	 *
	 * @return The glyph's bitmap.
	 * @since 2024/05/24
	 */
	public final GlyphBitmap bitmap()
	{
		return this.bitmap;
	}
	
	/**
	 * Returns the codepoint of this glyph.
	 *
	 * @return The glyph codepoint.
	 * @since 2024/05/24
	 */
	public final GlyphId codepoint()
	{
		return this.codepoint;
	}
}
