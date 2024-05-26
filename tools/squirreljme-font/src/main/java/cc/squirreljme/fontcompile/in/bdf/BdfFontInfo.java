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
import cc.squirreljme.fontcompile.in.FontInfo;
import cc.squirreljme.fontcompile.util.FontUtils;
import cc.squirreljme.fontcompile.util.LineTokenizer;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Map;

/**
 * Represents a BDF font.
 *
 * @since 2024/05/18
 */
public class BdfFontInfo
	extends FontInfo
{
	/**
	 * Parses the given font.
	 *
	 * @param __in The input path.
	 * @return The parsed font.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/19
	 */
	public static BdfFontInfo parse(Path __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Glyphs in the font
		Map<Integer, BdfGlyphInfo> glyphs = new SortedTreeMap<>();
		
		// Font information
		int pixelSize = Integer.MIN_VALUE;
		int bbw = Integer.MIN_VALUE;
		int bbh = Integer.MIN_VALUE;
		int bbx = Integer.MIN_VALUE;
		int bby = Integer.MIN_VALUE;
		int defaultChar = Integer.MIN_VALUE;
		int ascent = Integer.MIN_VALUE;
		int descent = Integer.MIN_VALUE;
		
		// Temporaries
		int t = 0;
		
		// Parse BDF file
		try (InputStream in = Files.newInputStream(__in,
				StandardOpenOption.READ); 
			LineTokenizer tokenizer = new LineTokenizer(in))
		{
			for (;;)
			{
				String[] tokens = tokenizer.next();
				
				// EOF?
				if (tokens == null)
					break;
				
				// Which token to handle?
				switch (tokens[0])
				{
						// We do not care about these
					case "ADD_STYLE_NAME":
					case "AVERAGE_WIDTH":
					case "CHARS":
					case "CHARSET_ENCODING":
					case "CHARSET_REGISTRY":
					case "COPYRIGHT":
					case "ENDFONT":
					case "ENDPROPERTIES":
					case "FAMILY_NAME":
					case "FONT":
					case "FONTNAME_REGISTRY":
					case "FOUNDRY":
					case "POINT_SIZE":
					case "RESOLUTION_X":
					case "RESOLUTION_Y":
					case "SETWIDTH_NAME":
					case "SIZE":
					case "SLANT":
					case "SPACING":
					case "STARTFONT":
					case "STARTPROPERTIES":
					case "WEIGHT_NAME":
						continue;
						
						// Bounding box (FONTBOUNDINGBOX 8 8 0 -2)
					case "FONTBOUNDINGBOX":
						bbw = FontUtils.parseInteger(tokens, 1);
						bbh = FontUtils.parseInteger(tokens, 2);
						bbx = FontUtils.parseInteger(tokens, 3);
						bby = FontUtils.parseInteger(tokens, 4);
						break;
						
						// Pixel size (PIXEL_SIZE 8)
					case "PIXEL_SIZE":
						t = FontUtils.parseInteger(tokens, 1);
						if (t <= 0)
							throw new InvalidFontException(String.format(
								"Invalid pixel size %d.", t));
						if (pixelSize <= 0)
							pixelSize = t;
						else if (t != pixelSize)
							throw new InvalidFontException(String.format(
								"Mismatched pixel size %d != %d.", t,
								pixelSize));
						break;
						
						// Default character if invalid (DEFAULT_CHAR 3000)
					case "DEFAULT_CHAR":
						defaultChar = FontUtils.parseInteger(tokens, 1);
						break;
						
						// Descent (FONT_DESCENT 2)
					case "FONT_DESCENT":
						descent = FontUtils.parseInteger(tokens, 1);
						break;
						
						// Ascent (FONT_ASCENT 6)
					case "FONT_ASCENT":
						ascent = FontUtils.parseInteger(tokens, 1);
						break;
					
						// Start of glyph?
					case "STARTCHAR":
						// Load glyph
						BdfGlyphInfo glyph = BdfGlyphInfo.parse(tokens,
							tokenizer);
						
						// Store
						glyphs.put(glyph.codepoint(), glyph);
						break;
					
						// Unknown
					default:
						throw new InvalidFontException(String.format(
							"Unknown BDF data: %s", Arrays.asList(tokens[0])));
				}
			}
			
			throw Debugging.todo();
		}
	}
}
