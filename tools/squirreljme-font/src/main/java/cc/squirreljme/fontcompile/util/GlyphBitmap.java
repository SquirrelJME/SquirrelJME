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
import java.io.IOException;
import java.io.PrintStream;

/**
 * Bitmap that represents the glyph.
 *
 * @since 2024/05/24
 */
public class GlyphBitmap
{
	/** The glyph width. */ 
	protected final int width;
	
	/** The glyph height. */
	protected final int height;
	
	/** The scanline length. */
	protected final int scanLen;
	
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
		this.scanLen = (__width / 8) + ((__width % 8) != 0 ? 1 : 0);
		this._bitmap = __bitmap;
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
		int scanLen = this.scanLen;
		
		for (int x = 0; x < w; x++)
			__ps.print('-');
		__ps.printf("(%d x %d)%n", w, h);
		
		byte[] bitmap = this._bitmap;
		for (int y = 0; y < h; y++)
		{
			int base = scanLen * y;
			
			for (int x = 0; x < w; x++)
			{
				if (((bitmap[base + (x / 8)] >> (x % 8)) & 1) != 0)
					__ps.print('#');
				else
					__ps.print('.');
			}
				
			__ps.println();
		}
		
		for (int x = 0; x < w; x++)
			__ps.print('-');
		__ps.println();
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
		
		// Determine hex count per row
		int bytesPerRow = (__bbw / 8) + ((__bbw % 8) != 0 ? 1 : 0); 
		int hexPerRow = bytesPerRow * 2;
		
		// Allocate target bitmap
		byte[] bitmap = new byte[bytesPerRow * __bbh];
		
		// Read each bitmap row
		for (int y = 0, s = 0; y < __bbh; y++)
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
			for (int x = 0; x < hexPerRow; x++, s += 4)
			{
				// Decode hex digit
				int dig = Character.digit(hex.charAt(x), 16);
				if (dig < 0)
					throw new InvalidFontException(String.format(
						"Token %s has invalid hex character.", hex));
				
				// Place into bitmap
				bitmap[(s / 8)] |= (byte)(dig << (s % 8));
			}
		}
		
		// Must be the end of the character
		String[] check = __tokenizer.next();
		if (check == null || check.length != 1 || !"ENDCHAR".equals(check[0]))
			throw new InvalidFontException("Expected ENDCHAR.");
		
		// Create bitmap
		return new GlyphBitmap(__bbw, __bbh, bitmap);
	}
}
