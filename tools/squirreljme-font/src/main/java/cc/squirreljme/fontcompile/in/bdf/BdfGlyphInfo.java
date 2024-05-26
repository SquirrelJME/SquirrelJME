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
import cc.squirreljme.fontcompile.util.FontUtils;
import cc.squirreljme.fontcompile.util.GlyphBitmap;
import cc.squirreljme.fontcompile.util.GlyphId;
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
	public BdfGlyphInfo(GlyphId __codepoint, int __displayWidth,
		GlyphBitmap __bitmap, int __offX, int __offY)
		throws NullPointerException
	{
		super(__codepoint, __displayWidth, __bitmap, __offX, __offY);
	}
	
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
		
		if (__tokens.length != 2 || __tokens[1] == null)
			throw new InvalidFontException(String.format(
				"Expected glyph ID in %s.", Arrays.asList(__tokens)));
		
		// Which codepoint is this?
		GlyphId codepoint = GlyphId.parse(__tokens[1]);
		
		// Bitmap data for the glyph
		GlyphBitmap bitmap = null;
		int dwidth = Integer.MIN_VALUE;
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
					continue;
					
					// Display width
				case "DWIDTH":
					dwidth = FontUtils.parseInteger(tokens, 1);
					break;
					
					// Font bounding box (BBX 2 7 0 -1)
				case "BBX":
					bbw = FontUtils.parseInteger(tokens, 1);
					bbh = FontUtils.parseInteger(tokens, 2);
					bbx = FontUtils.parseInteger(tokens, 3);
					bby = FontUtils.parseInteger(tokens, 4);
					break;
					
					// Font bitmap, after this is read there is nothing
					// left to parse
				case "BITMAP":
					bitmap = GlyphBitmap.parseBdf(bbw, bbh, bbx, bby,
						__tokenizer);
					break __outer;
				
				default:
					throw new InvalidFontException(String.format(
						"Invalid BDF glyph: %s", Arrays.asList(tokens)));
			}
		}
		
		// There must be a bitmap
		if (bitmap == null)
			throw new InvalidFontException("Glyph has no bitmap.");
		
		// Thee must also be set
		if (dwidth == Integer.MIN_VALUE ||
			bbw == Integer.MIN_VALUE || bbh == Integer.MIN_VALUE ||
			bbx == Integer.MIN_VALUE || bby == Integer.MIN_VALUE)
			throw new InvalidFontException(
				"Missing important glyph properties.");
		
		// Debug
		if (Debugging.ENABLED)
		{
			System.err.printf("CHAR %s %d%n",
				__tokens[1], codepoint.codepoint);
			bitmap.dump(System.err);
		}
		
		// Setup glyph
		return new BdfGlyphInfo(codepoint, dwidth, bitmap, bbx, bby);
	}
}
