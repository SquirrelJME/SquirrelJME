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
	 * @param __pixelSize The pixel size of the font.
	 * @return The resultant glyph.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/08
	 */
	public static SfdGlyphInfo parse(Path __path, int __pixelSize)
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
		int minX = Integer.MIN_VALUE;
		int minY = Integer.MIN_VALUE;
		
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
				// BDFChar: 5   65  6 0 4 0 8 <-- A
				// BDFChar: 174 198 8 0 6 0 8 <-- AE
				// BDFChar: 144 122 6 0 4 0 4 <-- z
				if ("BDFChar".equals(tokens[0]))
				{
					// Read in offsets
					minX = FontUtils.parseInteger(tokens, 4);
					minY = FontUtils.parseInteger(tokens, 5);
					int maxX = FontUtils.parseInteger(tokens, 6);
					int maxY = FontUtils.parseInteger(tokens, 7);
					
					// The FontForge reference says that the bitmap data is
					// (maxX - minX), however in practice this is never the
					// case and the width specified is the one that is valid
					width = FontUtils.parseInteger(tokens, 3);
					
					// Parse SFD bitmap
					// Use the pixel size as the height since although
					// the documentation says the height is ((maxY - minY) * 1)
					// this in practice is not the case
					bitmap = GlyphBitmap.parseSfd(tokenizer,
						width, __pixelSize);
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
			minX,
			__pixelSize - minY);
	}
}
