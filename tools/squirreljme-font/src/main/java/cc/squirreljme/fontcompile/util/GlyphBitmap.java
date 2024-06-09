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
import cc.squirreljme.fontcompile.out.rafoces.HuffBits;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.io.ASCII85Decoder;
import net.multiphasicapps.io.StringReader;

/**
 * Bitmap that represents the glyph.
 *
 * @since 2024/05/24
 */
public class GlyphBitmap
{
	/** The glyph width. */ 
	public final int width;
	
	/** The glyph height. */
	public final int height;
	
	/** The scanline length. */
	public final int scanLen;
	
	/** The font bitmap. */
	private final byte[] _bitmap;
	
	/**
	 * Initializes the bitmap.
	 *
	 * @param __width The bitmap width.
	 * @param __height The bitmap height.
	 * @param __bitmap The actual bitmap data.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/26
	 */
	private GlyphBitmap(int __width, int __height, byte[] __bitmap)
		throws NullPointerException
	{
		if (__bitmap == null)
			throw new NullPointerException("NARG");
		
		this.width = __width;
		this.height = __height;
		this.scanLen = GlyphBitmap.calcScan(__width);
		this._bitmap = Arrays.copyOf(__bitmap,
			__height * this.scanLen);
	}
	
	/**
	 * Dumps the bitmap.
	 *
	 * @param __ps The stream to output to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/26
	 */
	public void dump(PrintStream __ps)
		throws NullPointerException
	{
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		int w = this.width;
		int h = this.height;
		
		for (int x = 0; x < w; x++)
			__ps.print('-');
		__ps.printf("(%d x %d)%n", w, h);
		
		for (int y = 0; y < h; y++)
		{
			for (int x = 0; x < w; x++)
				__ps.print((this.get(x, y) ? '#' : '.'));
			__ps.println();
		}
		
		for (int x = 0; x < w; x++)
			__ps.print('-');
		__ps.println();
	}
	
	/**
	 * Gets the pixel value from the given position.
	 *
	 * @param __x The X position.
	 * @param __y The Y position.
	 * @return If the pixel is set, if out of bounds this will return
	 * always {@code false}.
	 * @since 2024/05/26
	 */
	public boolean get(int __x, int __y)
	{
		// Check bounds first, treat outside of bitmap as always unset
		int w = this.width;
		int h = this.height;
		if (__x < 0 || __x >= w || __y < 0 || __y >= h)
			return false;
		
		// Get pixel here
		return ((this._bitmap[this.getIndex(__x, __y)] >>>
			this.getShift(__x, __y)) & 1) != 0;
	}
	
	/**
	 * Returns the index position
	 *
	 * @param __x The X position.
	 * @param __y The Y position.
	 * @return The index position.
	 * @since 2024/05/26
	 */
	public int getIndex(int __x, int __y)
	{
		return GlyphBitmap.calcIndex(__x, __y, this.width, this.height);
	}
	
	/**
	 * Returns the shift position
	 *
	 * @param __x The X position.
	 * @param __y The Y position.
	 * @return The shift position.
	 * @since 2024/05/26
	 */
	public int getShift(int __x, int __y)
	{
		return GlyphBitmap.calcShift(__x, __y, this.width, this.height);
	}
	
	/**
	 * Returns the bytes which make up the bitmap.
	 *
	 * @return The bytes which make up the bitmap.
	 * @since 2024/06/07
	 */
	public byte[] toByteArray()
	{
		return this._bitmap.clone();
	}
	
	/**
	 * Returns the uncompressed bits that make up a glyph.
	 *
	 * @return The uncompressed glyph bits.
	 * @since 2024/06/03
	 */
	public List<HuffBits> uncompressedBits()
	{
		List<HuffBits> result = new ArrayList<>();
		
		for (byte in : this._bitmap)
			result.add(HuffBits.of(in & 0xFF, 8));
		
		return result;
	}
	
	/**
	 * Returns the index position
	 *
	 * @param __x The X position.
	 * @param __y The Y position.
	 * @param __w The bitmap width.
	 * @param __h The bitmap height.
	 * @return The index position.
	 * @since 2024/05/26
	 */
	public static int calcIndex(int __x, int __y, int __w, int __h)
	{
		int scanLen = GlyphBitmap.calcScan(__w);
		return (__y * scanLen) + (__x / 8);
	}
	
	/**
	 * Calculates the scanline.
	 *
	 * @param __w The width.
	 * @return The scanline width in bytes.
	 * @since 2024/05/26
	 */
	public static int calcScan(int __w)
	{
		return (__w / 8) + ((__w % 8) != 0 ? 1 : 0);
	}
	
	/**
	 * Returns the shift position
	 *
	 * @param __x The X position.
	 * @param __y The Y position.
	 * @param __w The bitmap width.
	 * @param __h The bitmap height.
	 * @return The shift position.
	 * @since 2024/05/26
	 */
	public static int calcShift(int __x, int __y, int __w, int __h)
	{
		return __x & 7;
	}
	
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
		
		// Initialize mutable bitmap
		MutableGlyphBitmap bitmap = new MutableGlyphBitmap(__bbw, __bbh);
		
		// Determine hex count per row 
		int hexPerRow = bitmap.bytesPerRow * 2;
		
		// Read each bitmap row
		for (int y = 0; y < __bbh; y++)
		{
			// Get next token sequence
			String[] init = __tokenizer.next();
			if (init == null || init.length != 1)
				throw new InvalidFontException("Unexpected token sequence.");
			
			// Get the hex characters
			String hex = init[0];
			if (hex.length() != hexPerRow)
				throw new InvalidFontException(String.format(
					"Expected %s to have %d hex characters.",
					hex, hexPerRow));
			
			// Copy in hex bytes
			for (int h = 0, x = 0; h < hexPerRow; h++)
			{
				// Decode hex digit
				int dig = Character.digit(hex.charAt(h), 16);
				if (dig < 0)
					throw new InvalidFontException(String.format(
						"Token %s has invalid hex character.", hex));
				
				// BDF is MSB
				dig = Integer.reverse(dig) >>> 28;
				
				// Mask into bitmap
				for (int s = 0; s < 4; s++, x++)
					if ((dig & (1 << s)) != 0)
						bitmap.draw(x, y);
			}
		}
		
		// Must be the end of the character
		String[] check = __tokenizer.next();
		if (check == null || check.length != 1 || !"ENDCHAR".equals(check[0]))
			throw new InvalidFontException("Expected ENDCHAR.");
		
		// Create bitmap
		return new GlyphBitmap(__bbw, __bbh, bitmap.bitmap);
	}
	
	/**
	 * Parses an SFD Glyph.
	 *
	 * @param __tokenizer The tokenizer used.
	 * @param __w The glyph width.
	 * @param __h The glyph height.
	 * @return The parsed bitmap.
	 * @throws InvalidFontException If the bitmap is not valid.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/08
	 */
	public static GlyphBitmap parseSfd(LineTokenizer __tokenizer,
		int __w, int __h)
		throws InvalidFontException, IOException, NullPointerException
	{
		if (__tokenizer == null)
			throw new NullPointerException("NARG");
		
		// Read in entire ASCII85 sequence
		StringBuilder seq = new StringBuilder();
		for (;;)
		{
			// Read in next set of tokens
			String[] tokens = __tokenizer.next();
			if (tokens == null)
				break;
			
			// There should be only a single set
			if (tokens.length != 1)
				throw new InvalidFontException(String.format(
					"Unexpected multiple tokens: %s", Arrays.asList(tokens)));
			
			// Append to the sequence to be decoded
			seq.append(tokens[0]);
		}
		
		// Decode everything
		byte[] decoded;
		try (InputStream in = new ASCII85Decoder(
			new StringReader(seq.toString())))
		{
			decoded = StreamUtils.readAll(in);
		}
		
		// FontForge encodes everything in reverse order
		for (int i = 0, n = decoded.length; i < n; i++)
			decoded[i] = (byte)(Integer.reverse(decoded[i]) >>> 24);
		
		// Directly load bitmap
		return new GlyphBitmap(__w, __h, decoded);
	}
}
