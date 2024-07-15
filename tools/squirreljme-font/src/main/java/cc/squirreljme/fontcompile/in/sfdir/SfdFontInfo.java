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
import cc.squirreljme.fontcompile.in.FontInfo;
import cc.squirreljme.fontcompile.util.FontFamily;
import cc.squirreljme.fontcompile.util.FontUtils;
import cc.squirreljme.fontcompile.util.GlyphId;
import cc.squirreljme.fontcompile.util.LineTokenizer;
import cc.squirreljme.io.file.FileUtils;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.LinkedHashMap;
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
	 * @param __family The font family.
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
	protected SfdFontInfo(String __name, FontFamily __family,
		Map<GlyphId, SfdGlyphInfo> __glyphs,
		GlyphId __invalidCodepoint, int __pixelSize, int __bbw, int __bbh,
		int __bbx, int __bby, int __ascent, int __descent)
		throws NullPointerException
	{
		super(__name, __family, __glyphs, __invalidCodepoint, __pixelSize,
			__bbw, __bbh, __bbx, __bby, __ascent, __descent);
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
		
		// Font and strike properties
		String name = null;
		
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
				
				// Remove ending colon
				if (tokens[0].endsWith(":"))
					tokens[0] = tokens[0].substring(0,
						tokens[0].length() - 1);
				
				// Which property?
				switch (tokens[0])
				{
						// Ignored
					case "SplineFontDB":
					case "FontName":
					case "FullName":
					case "Weight":
					case "Copyright":
					case "UComments":
					case "Version":
					case "ItalicAngle":
					case "UnderlinePosition":
					case "UnderlineWidth":
					case "Ascent":
					case "Descent":
					case "InvalidEm":
					case "LayerCount":
					case "Layer":
					case "XUID":
					case "FSType":
					case "OS2Version":
					case "OS2_WeightWidthSlopeOnly":
					case "OS2_UseTypoMetrics":
					case "CreationTime":
					case "ModificationTime":
					case "PfmFamily":
					case "TTFWeight":
					case "TTFWidth":
					case "LineGap":
					case "VLineGap":
					case "OS2TypoAscent":
					case "OS2TypoAOffset":
					case "OS2TypoDescent":
					case "OS2TypoDOffset":
					case "OS2TypoLinegap":
					case "OS2WinAscent":
					case "OS2WinAOffset":
					case "OS2WinDescent":
					case "OS2WinDOffset":
					case "HheadAscent":
					case "HheadAOffset":
					case "HheadDescent":
					case "HheadDOffset":
					case "OS2Vendor":
					case "MarkAttachClasses":
					case "DEI":
					case "LangName":
					case "Encoding":
					case "UnicodeInterp":
					case "NameList":
					case "DisplaySize":
					case "AntiAlias":
					case "FitToEm":
					case "OnlyBitmaps":
					case "WinInfo":
					case "BeginPrivate":
					case "EndPrivate":
					case "EndSplineFont":
						break;
						
						// The name of the font
					case "FamilyName":
						name = SfdFontInfo.__string(tokens);
						break;
					
					default:
						throw new InvalidFontException(String.format(
							"Unknown font property: %s",
							Arrays.asList(tokens)));
				}
			}
		}
		
		// More font properties
		int pixelSize = Integer.MIN_VALUE;
		int ascent = Integer.MIN_VALUE;
		int descent = Integer.MIN_VALUE;
		
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
				
				// Remove ending colon
				if (tokens[0].endsWith(":"))
					tokens[0] = tokens[0].substring(0,
						tokens[0].length() - 1);
				
				// Which property?
				switch (tokens[0])
				{
						// Ignore
					case "EndBitmapFont":
						break;
					
						// Font properties
						// The bitmap font line contains the following numbers: 
						// 1 the pixelsize of the font,
						// 2 the number of potential characters in the font,
						// 3 the ascent and
						// 4 the descent of the font and 
						// 5 the depth of font (number of bits in a pixel).
						// BitmapFont: 12 256 9 3 1 FontForge
					case "BitmapFont":
						pixelSize = FontUtils.parseInteger(tokens, 1);
						ascent = FontUtils.parseInteger(tokens, 3);
						descent = FontUtils.parseInteger(tokens, 4);
						break;
					
					default:
						throw new InvalidFontException(String.format(
							"Unknown strike property: %s",
							Arrays.asList(tokens)));
				}
			}
		}
		
		// Scan directory for glyphs to load
		Map<GlyphId, SfdGlyphInfo> glyphs = new LinkedHashMap<>();
		for (Path path : FileUtils.listFiles(__in))
		{
			// Debug
			Debugging.debugNote("Look at %s...", path);
			
			// Ignore directories
			if (Files.isDirectory(path))
				continue;
			
			// Not a glyph bitmap?
			String fileName = path.getFileName().toString();
			if (!fileName.endsWith(".bitmap"))
				continue;
			
			// Parse it
			SfdGlyphInfo glyph = SfdGlyphInfo.parse(path, pixelSize, ascent);
			glyphs.put(glyph.codepoint(), glyph);
		}
		
		// Determine bounding box width for glyph
		int bbw = 0;
		for (SfdGlyphInfo glyph : glyphs.values())
			bbw = Math.max(bbw, glyph.displayWidth);
		
		// Initialize font
		return new SfdFontInfo(name,
			FontFamily.of(name),
			glyphs,
			GlyphId.of(0),
			pixelSize,
			bbw,
			pixelSize,
			0,
			0,
			ascent,
			descent);
	}
	
	/**
	 * Turns tokens into a string.
	 *
	 * @param __tokens The input tokens.
	 * @return The resultant string.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/08
	 */
	private static String __string(String[] __tokens)
		throws NullPointerException
	{
		if (__tokens == null)
			throw new NullPointerException("NARG");
		
		StringBuilder sb = new StringBuilder();
		for (int i = 1, n = __tokens.length; i < n; i++)
		{
			if (i > 1)
				sb.append(' ');
			sb.append(__tokens[i]);
		}
		
		return sb.toString();
	}
}
