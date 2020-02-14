// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.common;

/**
 * This class is used to decode character sequences so that it is known which
 * parts of characters are what and which codepoints are to be attached to
 * text.
 *
 * @since 2017/10/21
 */
public final class CharSequenceDecoder
{
	/** The sequence to source from. */
	protected final CharSequence sequence;
	
	/** The position to end at. */
	protected final int end;
	
	/** The current marker position. */
	private volatile int _at;
	
	/** The next detail. */
	private volatile DecodedCharacter _next;
	
	/**
	 * Initializes the decoder for the given character sequence.
	 *
	 * @param __s The sequence to decode.
	 * @param __o The offset into the sequence.
	 * @param __l The number of characters to decode.
	 * @throws IndexOutOfBoundsException If the sequence is out of bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/21
	 */
	public CharSequenceDecoder(CharSequence __s, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Must be in bounds
		int sn = __s.length(),
			end = __o + __l;
		if (__o < 0 || __l < 0 || (end > sn))
			throw new IndexOutOfBoundsException("IOOB");
		
		// Set
		this.sequence = __s;
		this.end = end;
		this._at = __o;
	}
	
	/**
	 * Returns the next codepoint that is at the base of the sequence in
	 * that it is the most important character.
	 *
	 * @return The next codepoint or {@code -1} if there are no more code
	 * points remaining.
	 * @since 2017/10/21
	 */
	public DecodedCharacter next()
	{
		// Peek first always
		peek();
		
		// Has been read already?
		DecodedCharacter rv = this._next;
		if (rv != null)
		{
			this._next = null;
			return rv;
		}
		
		// No more characters left
		return null;
	}
	
	/**
	 * This returns the character which follows the next character.
	 *
	 * @return The character which follows the next character or {@code null}
	 * if there is no next character.
	 * @since 2017/10/24
	 */
	public DecodedCharacter peek()
	{
		// Already peeked?
		DecodedCharacter rv = this._next;
		if (rv != null)
			return rv;
		
		// Detect no more characters
		int at = this._at,
			end = this.end;
		if (at >= end)
			return null;
		
		// Read the next codepoint
		CharSequence sequence = this.sequence;
		int codepoint = sequence.charAt(at);
		this._at = at + 1;
		
		// Build information
		rv = new DecodedCharacter(codepoint);
		this._next = rv;
		return rv;
	}
}

