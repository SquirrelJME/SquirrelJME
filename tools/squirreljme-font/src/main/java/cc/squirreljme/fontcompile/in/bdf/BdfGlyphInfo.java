// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.in.bdf;

import cc.squirreljme.fontcompile.InvalidFontException;
import cc.squirreljme.fontcompile.in.GlyphInfo;
import cc.squirreljme.fontcompile.util.GlyphBitmap;
import cc.squirreljme.fontcompile.util.LineTokenizer;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.util.Arrays;

/**
 * Represents a BDF glyph.
 *
 * @since 2024/05/18
 */
public class BdfGlyphInfo
	extends GlyphInfo
{
	/**
	 * Parses the BDF glyph information.
	 *
	 * @param __tokens The tokens for the {@code STARTCHAR}.
	 * @param __tokenizer The tokenizer to initialize from.
	 * @return The resultant glyph.
	 * @throws InvalidFontException If the font is not valid.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/24
	 */
	public static BdfGlyphInfo parse(String[] __tokens,
		LineTokenizer __tokenizer)
		throws InvalidFontException, IOException, NullPointerException
	{
		if (__tokens == null || __tokenizer == null)
			throw new NullPointerException("NARG");
		
		// Bitmap for the glyph
		GlyphBitmap bitmap = null;
		int bbw = Integer.MIN_VALUE;
		int bbh = Integer.MIN_VALUE;
		int bbx = Integer.MIN_VALUE;
		int bby = Integer.MIN_VALUE;
		
		// Token parsing loop for this glyph
__outer:
		for (;;)
		{
			String[] tokens = __tokenizer.next();
			
			// EOF? This is bad!
			if (tokens == null)
				throw new InvalidFontException("Unexpected EOF.");
			
			// Depends on the token
			switch (tokens[0])
			{
					// We do not care about these
				case "ENCODING":
				case "SWIDTH":
				case "DWIDTH":
					continue;
					
					// Font bounding box (BBX 2 7 0 -1)
				case "BBX":
					throw Debugging.todo();
					
					// Font bitmap
				case "BITMAP":
					bitmap = GlyphBitmap.parseBdf(bbw, bbh, bbx, bby,
						__tokenizer);
					break;
						
					// End of character
				case "ENDCHAR":
					break __outer;
				
				default:
					throw new InvalidFontException(String.format(
						"Invalid BDF glyph.", Arrays.asList(tokens)));
			}
		}
		
		// There must be a bitmap
		if (bitmap == null)
			throw new InvalidFontException("Glyph has no bitmap.");
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
}
