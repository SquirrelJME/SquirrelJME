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
