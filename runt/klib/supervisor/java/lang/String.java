// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.Assembly;

/**
 * This represents a string.
 *
 * @since 2019/05/25
 */
public final class String
{
	/** The backing array. */
	transient final char[] _chars;
	
	/**
	 * Initializes an empty string.
	 *
	 * @since 2019/05/26
	 */
	public String()
	{
		this._chars = new char[0];
	}
	
	/**
	 * Initializes string decoded from the given UTF-8 byte.
	 *
	 * @param __b The UTF-8 bytes to decode.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/26
	 */
	public String(byte[] __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Create temporary output which has the input characters and such so
		// this will be the maximum used
		int bn = __b.length;
		char[] temp = new char[bn];
		
		// Translate UTF-8 sequences
		int nc = 0;
		for (int i = 0; i < bn;)
		{
			// Get character
			int c = __b[i++] & 0xFF;
			
			// Single byte
			if ((c & 0b1000_0000) == 0)
				temp[nc++] = (char)c;
			
			// Double byte
			else if ((c & 0b1110_0000) == 0b1100_0000)
			{
				c = ((c & 0b0001_1111) << 6);
				c |= (__b[i++] & 0b111111);
				temp[nc++] = (char)c;
			}
			
			// Triple byte
			else if ((c & 0b1111_0000) == 0b1110_0000)
			{
				c = ((c & 0b0000_1111) << 12);
				c |= ((__b[i++] & 0b111111) << 6);
				c |= (__b[i++] & 0b111111);
				temp[nc++] = (char)c;
			}
		}
		
		// Use direct array if the same length
		if (nc == bn)
			this._chars = temp;
		
		// Too short, copy only used chars
		else
		{
			char[] chars = new char[nc];
			for (int i = 0; i < nc; i++)
				chars[i] = temp[i];
			this._chars = chars;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/26
	 */
	@Override
	public final boolean equals(Object __o)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/26
	 */
	@Override
	public final int hashCode()
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Returns a string which is a unique internal representation of a string.
	 *
	 * @return The unique interned string.
	 * @since 2019/05/26
	 */
	public final String intern()
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/26
	 */
	@Override
	public final String toString()
	{
		return this;
	}
}

