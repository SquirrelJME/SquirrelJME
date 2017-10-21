// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui.font;

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
	 * @return The width of the codepoint in pixels.
	 * @since 2017/10/21
	 */
	public final int codePointWidth(int __p)
	{
		// Some characters have special widths
		switch (__p)
		{
				// Zero width characters
			case 0x200B: return 0;
			
			default:
				throw new todo.TODO();
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
		
		// Decode the sequence of characters
		CharSequenceDecoder csd = new CharSequenceDecoder(__s, __o, __l);
		int next,
			width = 0,
			rvmax = 0;
		while ((next = csd.next()) >= 0)
		{
			// New line resets the width
			if (next == '\r' || width == '\n')
				width = 0;
			
			// Otherwise it is added to
			else
				width += codePointWidth(next);
			
			// Use the maximum width
			if (width > rvmax)
				rvmax = width;
		}
		
		// Use the given width
		return rvmax;
	}
}

