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
 * This class is used as the base handle for fonts which are visible to
 * LCDUI.
 *
 * Handles are identified by a single ID.
 *
 * All fonts are in pixels. The height of the font is specified as the distance
 * in pixels between the baselines of two unadjusted lines of text, which is
 * also called the em box.
 *
 * @since 2017/10/20
 */
@Deprecated
public final class FontHandle
{
	/** The identifier of the handle. */
	protected final int id;
	
	/** The family of the font. */
	protected final FontFamily family;
	
	/** The font style. */
	protected final int style;
	
	/** The size of the font. */
	protected final int size;
	
	/**
	 * Initializes the font handle.
	 *
	 * @param __id The ID of the font.
	 * @param __fam The font family.
	 * @param __style The style of the font.
	 * @param __size The size of the font.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/20
	 */
	public FontHandle(int __id, FontFamily __fam, int __style, int __size)
		throws NullPointerException
	{
		if (__fam == null)
			throw new NullPointerException("NARG");
		
		this.id = __id;
		this.family = __fam;
		this.style = __style;
		this.size = __size;
	}
	
	/**
	 * Returns the width of the given codepoint.
	 *
	 * @param __p The codepoint to get the pixel width of.
	 * @param __k The second character to use which can be used to modify the
	 * width to adjust for kerning, will be a negative value if there is no
	 * known second characater.
	 * @return The width of the codepoint in pixels.
	 * @since 2017/10/21
	 */
	public final int codepointWidth(int __p, int __k)
	{
		// Some characters have special widths
		switch (__p)
		{
				// Zero width characters
			case 0x200B: return 0;
			
				// Ask the font family to return the length
			default:
				return this.family.codepointWidth(this, __p, __k);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (!(__o instanceof FontHandle))
			return false;
		
		return this.id == ((FontHandle)__o).id;
	}
	
	/**
	 * This returns the ascent of the font from the top of the line to the
	 * average point where the tops of characters are.
	 *
	 * @return The ascent of the font.
	 * @since 2017/10/24
	 */
	public int getAscent()
	{
		return this.family.getAscent(this);
	}
	
	/**
	 * This returns the descent of the font from the baseline of the font to
	 * the bottom of most alphanumeric characters.
	 *
	 * @return The descent of the font.
	 * @since 2017/10/24
	 */
	public int getDescent()
	{
		return this.family.getDescent(this);
	}
	
	/**
	 * Returns the standard leading of the font in pixels. The leading is the
	 * standard number of pixels which are between each line. The space is
	 * reserved between the descent of the first line and the ascent of the
	 * next line.
	 *
	 * @return The standard leading.
	 * @since 2017/10/24
	 */
	public int getLeading()
	{
		return this.size / 5;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public final int hashCode()
	{
		return this.id;
	}
	
	/**
	 * Checks if the given specifiers are compatible.
	 *
	 * @param __f The font family.
	 * @param __style The style of the font.
	 * @param __size The size of the font.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/21
	 */
	public final boolean isCompatible(FontFamily __f, int __style, int __size)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the size of the font.
	 *
	 * @return The font size.
	 * @since 2017/10/24
	 */
	public final int size()
	{
		return this.size;
	}
	
	/**
	 * Returns the width of the character sequence in pixels.
	 *
	 * @param __s The characters to get the width of.
	 * @param __o The offset into the sequence.
	 * @param __l The number of characters to count.
	 * @return The width of the sequence in pixels.
	 * @throws IndexOutOfBoundsException If the specified indexes are not
	 * within bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/21
	 */
	public final int sequencePixelWidth(CharSequence __s, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// See if the font family has a proprietary means of determining the
		// font width
		FontFamily family = this.family;
		int prv = family.sequencePixelWidth(this, __s, __o, __l);
		if (prv != FontFamily.INVALID_WIDTH)
			return prv;
	
		// Decode the sequence of characters
		CharSequenceDecoder csd = new CharSequenceDecoder(__s, __o, __l);
		int width = 0,
			rvmax = 0;
		DecodedCharacter next, peek;
		while ((next = csd.next()) != null)
		{
			// Peek the next for potential kerning
			peek = csd.peek();
			int codepoint = next.codepoint,
				peekedcp = (peek != null ? peek.codepoint : -1);
			
			// New line resets the width
			if (codepoint == '\r' || codepoint == '\n')
				width = 0;
			
			// Otherwise it is added to
			else
				width += codepointWidth(codepoint, peekedcp);
			
			// Use the maximum width
			if (width > rvmax)
				rvmax = width;
		}
		
		// Use the given width
		return rvmax;
	}
}

