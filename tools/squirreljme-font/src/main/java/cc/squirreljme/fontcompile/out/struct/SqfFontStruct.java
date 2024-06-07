// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out.struct;

import cc.squirreljme.fontcompile.out.CompiledFont;
import cc.squirreljme.fontcompile.out.CompiledGlyph;
import cc.squirreljme.fontcompile.out.rafoces.ChainList;
import cc.squirreljme.fontcompile.out.rafoces.HuffBits;
import cc.squirreljme.fontcompile.out.rafoces.HuffTable;
import cc.squirreljme.fontcompile.util.GlyphId;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.io.ChunkSection;
import net.multiphasicapps.io.ChunkWriter;

/**
 * Represents a raw structure of a SQF Font.
 *
 * @since 2024/06/04
 */
public class SqfFontStruct
{
	/** The number of characters per codepage. */
	private static final int _CODEPAGE_SIZE =
		256;
	
	/** The name of the font. */
	public final String name;
	
	/** The pixel height of the font. */
	public final int pixelHeight;
	
	/** The ascent of the font. */
	public final int ascent;
	
	/** The descent of the font. */
	public final int descent;
	
	/** Bounding box X offset. */
	public final int bbx;
	
	/** Bounding box Y offset. */
	public final int bby;
	
	/** Bounding box width. */
	public final int bbw;
	
	/** Bounding box height. */
	public final int bbh;
	
	/** The starting codepoint for this given SQF font. */
	public final int codepointStart;
	
	/** The number of codepoints which are in this SQF font. */
	public final int codepointCount;
	
	/** The size of the {@code _charBmp} member. */
	public final int charBmpSize;
	
	/** Widths for each character. */
	private final byte[] _charWidths;
	
	/** X offset for character. */
	private final byte[] _charXOffset;
	
	/** Y offset for character. */
	private final byte[] _charYOffset;
	
	/** SQF Font Flags, per character. */
	private final byte[] _charFlags;
	
	/** Offset to the character bitmap for the given character. */
	private final int[] _charBmpOffset;
	
	/** The bytes per bitmap scan. */
	private final byte[] _charBmpScan;
	
	/** Which characters make up the bitmap? */
	private final byte[] _charBmp;
	
	/**
	 * Initializes the font structure.
	 *
	 * @param __name The name of the font.
	 * @param __pixelHeight The pixel height of the font.
	 * @param __ascent The ascent of the font.
	 * @param __descent The descent of the font.
	 * @param __bbx Bounding box X offset.
	 * @param __bby Bounding box Y offset.
	 * @param __bbw Bounding box width.
	 * @param __bbh Bounding box height.
	 * @param __codepointStart The starting codepoint for this font.
	 * @param __codepointCount The number of codepoints which are in this font.
	 * @param __charWidths Widths for each character.
	 * @param __charXOffset X offset for each character.
	 * @param __charYOffset Y offset for each character.
	 * @param __charFlags SQF Font Flags, per character.
	 * @param __charBmpOffset Offset to the character bitmap for the given
	 * character.
	 * @param __charBmpScan The bytes per bitmap scan.
	 * @param __charBmp Which characters make up the bitmap?
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/04
	 */
	public SqfFontStruct(String __name,
		int __pixelHeight, int __ascent, int __descent,
		int __bbx, int __bby, int __bbw, int __bbh,
		int __codepointStart, int __codepointCount,
		byte[] __charWidths, byte[] __charXOffset, byte[] __charYOffset,
		byte[] __charFlags, int[] __charBmpOffset,
		byte[] __charBmpScan,
		byte[] __charBmp)
		throws NullPointerException
	{
		this._charXOffset = __charXOffset;
		this._charYOffset = __charYOffset;
		if (__name == null || __charWidths == null || __charFlags == null ||
			__charBmpOffset == null || __charBmp == null ||
			__charBmpScan == null || __charXOffset == null ||
			__charYOffset == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
		this.pixelHeight = __pixelHeight;
		this.ascent = __ascent;
		this.descent = __descent;
		this.bbx = __bbx;
		this.bby = __bby;
		this.bbw = __bbw;
		this.bbh = __bbh;
		this.codepointStart = __codepointStart;
		this.codepointCount = __codepointCount;
		this.charBmpSize = __charBmp.length;
		this._charWidths = __charWidths;
		this._charFlags = __charFlags;
		this._charBmpOffset = __charBmpOffset;
		this._charBmpScan = __charBmpScan;
		this._charBmp = __charBmp;
	}
	
	/**
	 * Parses the given compiled font into a SQF structure.
	 *
	 * @param __font The compiled font to parse.
	 * @return The resultant font structures.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/04
	 */
	public static SqfFontStruct[] parse(CompiledFont __font)
		throws IOException, NullPointerException
	{
		if (__font == null)
			throw new NullPointerException("NARG");
		
		// Map glyphs to pages first, these are 256 codepoint regions
		// accordingly that should have similar characters to each other
		Map<GlyphId, CompiledGlyph[]> pages = new LinkedHashMap<>();
		for (CompiledGlyph compiledGlyph : __font)
		{
			// Where is this glyph?
			GlyphId pageId = compiledGlyph.glyph.codepoint().page();
			int pageOffset = compiledGlyph.glyph.codepoint().pageOffset();
			
			// Get the page for all the characters
			CompiledGlyph[] page = pages.get(pageId);
			if (page == null)
			{
				page = new CompiledGlyph[256];
				pages.put(pageId, page);
			}
			
			// Store in the page
			page[pageOffset] = compiledGlyph;
		}
		
		// Process glyphs into the structured format
		List<SqfFontStruct> result = new ArrayList<>();
		for (Map.Entry<GlyphId, CompiledGlyph[]> entry : pages.entrySet())
		{
			// Ignore completely blank codepages
			CompiledGlyph[] glyphs = entry.getValue();
			if (glyphs == null || glyphs.length == 0)
				continue;
			
			// Build structure for export
			result.add(SqfFontStruct.parse(__font,
				entry.getKey(), glyphs));
		}
		
		return result.toArray(new SqfFontStruct[result.size()]);
	}
	
	/**
	 * Parses the given page within the font structure.
	 *
	 * @param __font The font this is within.
	 * @param __pageId The page ID of these glyphs.
	 * @param __glyphs The glyphs.
	 * @return The font structure.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/05
	 */
	private static SqfFontStruct parse(CompiledFont __font, GlyphId __pageId,
		CompiledGlyph[] __glyphs)
		throws IOException, NullPointerException
	{
		if (__font == null || __pageId == null || __glyphs == null)
			throw new NullPointerException("NARG");
		
		// Everything goes into these!
		int n = SqfFontStruct._CODEPAGE_SIZE;
		byte[] charWidths = new byte[n];
		byte[] charXOffset = new byte[n];
		byte[] charYOffset = new byte[n];
		byte[] charFlags = new byte[n];
		int[] charBmpOffset = new int[n];
		byte[] charBmpScan = new byte[n];
		
		// Bitmap data is variable
		ChunkWriter charBmpChunk = new ChunkWriter();
		ChunkSection charBmp = charBmpChunk.addSection();
		
		// Existing offset to huffman tables
		Map<HuffTable, Integer> huffOffsets = new LinkedHashMap<>();
		
		// Process each glyph
		for (int i = 0; i < n; i++)
		{
			// Is there no glyph here?
			CompiledGlyph glyph = __glyphs[i];
			if (glyph == null)
				continue;
			
			// Copy properties
			charWidths[i] = (byte)Math.max(0,
				Math.min(127, glyph.glyph.displayWidth));
			charXOffset[i] = (byte)Math.max(-128,
				Math.min(127, glyph.glyph.offX));
			charYOffset[i] = (byte)Math.max(-128,
				Math.min(127, glyph.glyph.offY));
			
			// Bitmap scan is just how many bytes per scanline within the
			// bitmap
			byte scanLen = (byte)Math.max(1,
				Math.min(127, glyph.glyph.bitmap().scanLen));
			charBmpScan[i] = scanLen;
			
			// Raw bitmap?
			if (glyph.isReject())
			{
				// Mark valid
				charFlags[i] = SqfFontCharFlag.VALID;
				
				// Bitmap is started here
				charBmpOffset[i] = charBmp.size();
				
				// Write bitmap directly
				charBmp.write(glyph.glyph.bitmap().toByteArray());
			}
			
			// RaFoCES compressed?
			else
			{
				// Mark valid and compressed
				charFlags[i] = SqfFontCharFlag.VALID |
					SqfFontCharFlag.RAFOCES;
				
				// Using this table
				HuffTable huffman = glyph.huffman;
				
				// Does the huffman table need to be stored into the font?
				int huffOffset;
				Integer huffOffsetBox = huffOffsets.get(huffman);
				if (huffOffsetBox == null)
				{
					// Is stored here
					huffOffset = charBmp.size();
					huffOffsets.put(huffman, huffOffset);
					
					// Place down table size
					charBmp.writeInt(huffman.size());
					
					// Go through and write all sequences
					for (Map.Entry<ChainList, HuffBits> entry : huffman)
					{
						SqfFontStruct.__writeHuff(charBmp,
							entry.getKey().toHuffBits());
						SqfFontStruct.__writeHuff(charBmp,
							new HuffBits[]{entry.getValue()});
					}
				}
				
				// No it does not need to be written, so we can just use
				// a previously defined table
				else
					huffOffset = huffOffsetBox;
				
				// The bitmap is here, the huffman is elsewhere
				charBmpOffset[i] = charBmp.size();
				charBmp.writeInt(huffOffset);
				
				// Write the raw huffman bytes for the bitmap
				charBmp.write(HuffBits.toByteArray(glyph.bitNaive));
			}
		}
		
		// Initialize font structure
		return new SqfFontStruct(__font.original.name,
			__font.original.pixelSize,
			__font.original.ascent,
			__font.original.descent,
			__font.original.bbx,
			__font.original.bby,
			__font.original.bbw,
			__font.original.bbh,
			__pageId.codepoint,
			n,
			charWidths,
			charXOffset,
			charYOffset,
			charFlags,
			charBmpOffset,
			charBmpScan,
			charBmpChunk.toByteArray());
	}
	
	/**
	 * Normalizes the name of the font.
	 *
	 * @param __s The input font name.
	 * @return The resultant name.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/04
	 */
	public static final String normalizeName(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Lowercase and remove any special characters
		StringBuilder sb = new StringBuilder();
		for (int i = 0, n = __s.length(); i < n; i++)
		{
			char c = __s.charAt(0);
			
			if (c >= 'A' && c <= 'Z')
				sb.append('a' + (c - 'A'));
			else if (c == ' ' || c == '\t')
				continue;
			else if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') ||
				c == '_')
				sb.append(c);
			else
				sb.append('_');
		}
		
		// Truncate the length down
		while (sb.length() > 8)
			sb.deleteCharAt(4);
			
		return sb.toString();
	}
	
	/**
	 * Writes huffman bits into the output.
	 *
	 * @param __into Where is this being written to?
	 * @param __huffBits The bits to write.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/07
	 */
	private static void __writeHuff(ChunkSection __into, HuffBits[] __huffBits)
		throws IOException, NullPointerException
	{
		if (__into == null || __huffBits == null)
			throw new NullPointerException("NARG");
		
		int len = HuffBits.length(__huffBits);
		byte[] raw = HuffBits.toByteArray(__huffBits);
		
		// Write length coding information
		__into.writeByte(len);
		__into.writeByte(raw.length);
		
		// Then write in the raw sequence
		__into.write(raw);
	}
}
