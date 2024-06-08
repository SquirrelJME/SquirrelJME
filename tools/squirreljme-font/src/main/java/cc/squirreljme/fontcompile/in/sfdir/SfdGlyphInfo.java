// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.in.sfdir;

import cc.squirreljme.fontcompile.InvalidFontException;
import cc.squirreljme.fontcompile.in.GlyphInfo;
import cc.squirreljme.fontcompile.util.FontUtils;
import cc.squirreljme.fontcompile.util.GlyphBitmap;
import cc.squirreljme.fontcompile.util.GlyphId;
import cc.squirreljme.fontcompile.util.LineTokenizer;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

/**
 * Represents a {@code .sfdir} glyph.
 *
 * @since 2024/05/18
 */
public class SfdGlyphInfo
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
	public SfdGlyphInfo(GlyphId __codepoint, int __displayWidth,
		GlyphBitmap __bitmap, int __offX, int __offY)
		throws NullPointerException
	{
		super(__codepoint, __displayWidth, __bitmap, __offX, __offY);
	}
	
	/**
	 * Parses the SFD Glyph.
	 *
	 * @param __path The input glyph data.
	 * @return The resultant glyph.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/08
	 */
	public static SfdGlyphInfo parse(Path __path)
		throws IOException, NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		// Determine the ID of this glyph
		String fileName = __path.getFileName().toString();
		GlyphId id = GlyphId.parse(fileName.substring(0,
			fileName.length() - ".bitmap".length()));
		
		// Glyph properties
		GlyphBitmap bitmap = null;
		int width = Integer.MIN_VALUE;
		int offX = Integer.MIN_VALUE;
		int offY = Integer.MIN_VALUE;
		
		// Parse glyph bitmap data
		try (InputStream in = Files.newInputStream(__path,
				StandardOpenOption.READ);
			 LineTokenizer tokenizer = new LineTokenizer(in))
		{
			for (;;)
			{
				String[] tokens = tokenizer.next();
				
				// EOF?
				if (tokens == null)
					break;
				
				// Remove ending colon
				if (tokens[0].endsWith(":"))
					tokens[0] = tokens[0].substring(0,
						tokens[0].length() - 1);
				
				// Character details?
				// 1 the original position (glyph ID),
				// 2 the encoding (local),
				// 3 the width,
				// 4 the minimum x value,
				// 5 the minimum y value,
				// 6 the maximum x value and
				// 7 the maximum y value.
				// BDFChar: 2 33 2 0 0 0 7
				if ("BDFChar".equals(tokens[0]))
				{
					// Read in offsets
					offX = FontUtils.parseInteger(tokens, 4);
					offY = FontUtils.parseInteger(tokens, 5);
					int maxX = FontUtils.parseInteger(tokens, 6);
					int maxY = FontUtils.parseInteger(tokens, 7);
					
					// The bitmap width is based on the offsets
					width = maxX - offX;
					
					// Parse SFD bitmap
					bitmap = GlyphBitmap.parseSfd(tokenizer,
						width, maxY - offY);
				}
				
				// Unknown?
				else
					throw new InvalidFontException(String.format(
						"Unknown glyph property: %s",
						Arrays.asList(tokens)));
			}
		}
		
		// Initialize glyph
		return new SfdGlyphInfo(id,
			width,
			bitmap,
			offX,
			offY);
	}
}
