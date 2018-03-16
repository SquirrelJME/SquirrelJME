// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.font;

/**
 * This represents the family of a font and is used to provide a native
 * representation of a given family.
 *
 * @since 2017/10/20
 */
@Deprecated
public abstract class FontFamily
{
	/** Used to represent that the string has an invalid width. */
	public static final int INVALID_WIDTH =
		Integer.MIN_VALUE;
	
	/** The name of the family. */
	protected final FontFamilyName name;
	
	/**
	 * Initializes the base font family.
	 *
	 * @param __n The name of the font.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/21
	 */
	public FontFamily(FontFamilyName __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		this.name = __n;
	}
	
	/**
	 * Returns the width of the given codepoint.
	 *
	 * @param __h The handle to the font information.
	 * @param __p The codepoint to get the pixel width of.
	 * @param __k The second character to use which can be used to modify the
	 * width to adjust for kerning, will be a negative value if there is no
	 * known second characater.
	 * @return The width of the codepoint in pixels.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/24
	 */
	public abstract int codepointWidth(FontHandle __h, int __p, int __k)
		throws NullPointerException;
	
	/**
	 * This returns the ascent of the font from the top of the line to the
	 * average point where the tops of characters are.
	 *
	 * @param __h The handle to the font display information.
	 * @return The ascent of the font.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/24
	 */
	public abstract int getAscent(FontHandle __h)
		throws NullPointerException;
	
	/**
	 * This returns the descent of the font from the baseline of the font to
	 * the bottom of most alphanumeric characters.
	 *
	 * @param __h The handle to the font display information.
	 * @return The descent of the font.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/24
	 */
	public abstract int getDescent(FontHandle __h)
		throws NullPointerException;
	
	/**
	 * Returns the width of the character sequence in pixels.
	 *
	 * @param __h The handle to the character details.
	 * @param __s The characters to get the width of.
	 * @param __o The offset into the sequence.
	 * @param __l The number of characters to count.
	 * @return The width of the sequence in pixels or {@link #INVALID_WIDTH}
	 * if it is not calculated by a proprietary font system.
	 * @throws IndexOutOfBoundsException If the specified indexes are not
	 * within bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/24
	 */
	public abstract int sequencePixelWidth(FontHandle __h, CharSequence __s,
		int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException;
}

