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
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Base class for glyph information.
 *
 * @since 2024/05/17
 */
public abstract class GlyphInfo
{
	/**
	 * Returns the glyph bitmap.
	 *
	 * @return The glyph's bitmap.
	 * @since 2024/05/24
	 */
	public final GlyphBitmap bitmap()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the codepoint of this glyph.
	 *
	 * @return The glyph codepoint.
	 * @since 2024/05/24
	 */
	public final int codepoint()
	{
		throw Debugging.todo();
	}
}
