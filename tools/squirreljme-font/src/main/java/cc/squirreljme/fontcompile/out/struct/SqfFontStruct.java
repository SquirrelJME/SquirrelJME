// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out.struct;

/**
 * Represents a raw structure of a SQF Font.
 *
 * @since 2024/06/04
 */
public class SqfFontStruct
{
	/** The pixel height of the font. */
	public final int pixelHeight;
	
	/** The ascent of the font. */
	public final int ascent;
	
	/** The descent of the font. */
	public final int descent;
	
	/** The bytes per scanline. */
	public final int bytesPerScan;
	
	/** The starting codepoint for this given SQF font. */
	public final int codepointStart;
	
	/** The number of codepoints which are in this SQF font. */
	public final int codepointCount;
	
	/** Widths for each character. */
	private final byte[] _charWidths;
	
	/** SQF Font Flags, per character. */
	private final byte[] _charFlags;
	
	/** Offset to the character bitmap for the given character. */
	private final short[] _charBmpOffset;
	
	/** Which characters make up the bitmap? */
	private final byte[] _charBmp;
	
	/**
	 * Initializes the font structure.
	 *
	 * @param __pixelHeight The pixel height of the font.
	 * @param __ascent The ascent of the font.
	 * @param __descent The descent of the font.
	 * @param __bytesPerScan The bytes per scanline.
	 * @param __codepointStart The starting codepoint for this font.
	 * @param __codepointCount The number of codepoints which are in this font.
	 * @param __charWidths Widths for each character.
	 * @param __charFlags SQF Font Flags, per character.
	 * @param __charBmpOffset Offset to the character bitmap for the given
	 * character.
	 * @param __charBmp Which characters make up the bitmap?
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/04
	 */
	public SqfFontStruct(int __pixelHeight, int __ascent, int __descent,
		int __bytesPerScan, int __codepointStart, int __codepointCount,
		byte[] __charWidths, byte[] __charFlags, short[] __charBmpOffset,
		byte[] __charBmp)
		throws NullPointerException
	{
		if (__charWidths == null || __charFlags == null ||
			__charBmpOffset == null || __charBmp == null)
			throw new NullPointerException("NARG");
		
		this.pixelHeight = __pixelHeight;
		this.ascent = __ascent;
		this.descent = __descent;
		this.bytesPerScan = __bytesPerScan;
		this.codepointStart = __codepointStart;
		this.codepointCount = __codepointCount;
		this._charWidths = __charWidths;
		this._charFlags = __charFlags;
		this._charBmpOffset = __charBmpOffset;
		this._charBmp = __charBmp;
	}
}
