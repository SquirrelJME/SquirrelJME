// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.IOException;
import java.io.Reader;

/**
 * This is a reader which can read from a string.
 *
 * This class is not thread safe.
 *
 * @since 2018/11/04
 */
public class StringReader
	extends Reader
{
	/** The string to read from. */
	protected final String string;
	
	/** The string length. */
	protected final int length;
	
	/** The current position. */
	private int _at;
	
	/**
	 * Initializes the reader.
	 *
	 * @param __s The input string.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/04
	 */
	public StringReader(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		this.string = __s;
		this.length = __s.length();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/04
	 */
	@Override
	public void close()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/04
	 */
	@Override
	public int read(char[] __c, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __c.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Determine the current position and string length
		int at = this._at,
			length = this.length,
			left = length - at;
		
		// EOF?
		if (at >= length)
			return -1;
		
		// Can only read so many characters
		String string = this.string;
		int max = Math.min(__l, left),
			limit = at + max;
		for (int o = __o; at < limit; at++, o++)
			__c[o] = string.charAt(at);
		
		// Set position for next time
		this._at = at;
		
		return max;
	}
}

