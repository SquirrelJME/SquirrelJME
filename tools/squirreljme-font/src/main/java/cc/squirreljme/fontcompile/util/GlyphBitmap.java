// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.util;

import cc.squirreljme.fontcompile.InvalidFontException;
import java.io.IOException;

/**
 * Bitmap that represents the glyph.
 *
 * @since 2024/05/24
 */
public class GlyphBitmap
{
	/**
	 * Parses a BDF glyph bitmap.
	 *
	 * @param __bbw The bounding box width.
	 * @param __bbh The bounding box height.
	 * @param __bbx The character X offset.
	 * @param __bby The character Y offset.
	 * @param __tokenizer The tokenizer to use.
	 * @return The resultant bitmap.
	 * @throws InvalidFontException If the font is not valid.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/24
	 */
	public static GlyphBitmap parseBdf(int __bbw, int __bbh, int __bbx,
		int __bby, LineTokenizer __tokenizer)
		throws InvalidFontException, IOException, NullPointerException
	{
		if (__tokenizer == null)
			throw new NullPointerException("NARG");
		
		if (__bbw < 0 || __bbh < 0)
			throw new InvalidFontException("Invalid BDF bounding box.");
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
}
