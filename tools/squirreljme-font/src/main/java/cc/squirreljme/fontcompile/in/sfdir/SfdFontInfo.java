// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.in.sfdir;

import cc.squirreljme.fontcompile.in.FontInfo;
import cc.squirreljme.fontcompile.in.bdf.BdfFontInfo;
import cc.squirreljme.fontcompile.in.bdf.BdfGlyphInfo;
import cc.squirreljme.fontcompile.util.GlyphId;
import cc.squirreljme.fontcompile.util.LineTokenizer;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

/**
 * Represents a {@code .sfdir} font.
 *
 * @since 2024/05/18
 */
public class SfdFontInfo
	extends FontInfo
{
	/**
	 * Initializes the base font info.
	 *
	 * @param __name The name of the font.
	 * @param __glyphs The glyphs to use.
	 * @param __invalidCodepoint The invalid glyph ID.
	 * @param __pixelSize The pixel size of the font.
	 * @param __bbw The bounding box width.
	 * @param __bbh The bounding box height.
	 * @param __bbx The X offset.
	 * @param __bby The Y offset.
	 * @param __ascent The font ascent.
	 * @param __descent The font descent.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/26
	 */
	protected SfdFontInfo(String __name, Map<GlyphId, SfdGlyphInfo> __glyphs,
		GlyphId __invalidCodepoint, int __pixelSize, int __bbw, int __bbh,
		int __bbx, int __bby, int __ascent, int __descent)
		throws NullPointerException
	{
		super(__name, __glyphs, __invalidCodepoint, __pixelSize, __bbw, __bbh,
			__bbx, __bby, __ascent, __descent);
		
		throw Debugging.todo();
	}
	
	/**
	 * Parses the given font.
	 *
	 * @param __in The input path.
	 * @return The parsed font.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/19
	 */
	public static SfdFontInfo parse(Path __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Process font properties
		try (InputStream in = Files.newInputStream(
				__in.resolveSibling("font.props"),
				StandardOpenOption.READ);
			LineTokenizer tokenizer = new LineTokenizer(in))
		{
			for (;;)
			{
				String[] tokens = tokenizer.next();
				
				// EOF?
				if (tokens == null)
					break;
				
				throw Debugging.todo();
			}
		}
		
		// Process strike properties
		try (InputStream in = Files.newInputStream(
				__in.resolve("strike.props"),
				StandardOpenOption.READ);
			LineTokenizer tokenizer = new LineTokenizer(in))
		{
			for (;;)
			{
				String[] tokens = tokenizer.next();
				
				// EOF?
				if (tokens == null)
					break;
				
				throw Debugging.todo();
			}
		}
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
}
