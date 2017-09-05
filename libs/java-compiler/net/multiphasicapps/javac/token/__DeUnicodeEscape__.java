// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.token;

import java.io.IOException;
import java.io.Reader;

/**
 * This is used to de-escape unicode escape sequences so that the reader part
 * of the code has a consistent operation.
 *
 * @since 2017/09/04
 */
class __DeUnicodeEscape__
	extends Reader
{
	/** The reader to source from. */
	protected final Reader from;
	
	/**
	 * Initializes the de-escaper.
	 *
	 * @param __r The stream to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/04
	 */
	__DeUnicodeEscape__(Reader __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.from = __r;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/04
	 */
	@Override
	public void close()
		throws IOException
	{
		this.from.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/04
	 */
	@Override
	public int read(char[] __c, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		// Check
		if (__c == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __c.length)
			throw new IndexOutOfBoundsException("CAOB");
		
		// Potentially parse sequences
		Reader from = this.from;
		for (int i = 0, o = __o; i < __l; i++)
		{
			int c = from.read();
			
			// EOF?
			if (c < 0)
				return (i == 0 ? -1 : i);
			
			// Cannot be an escape sequence
			if (c != '\\')
			{
				__c[o++] = (char)c;
				continue;
			}
			
			// EOF? Give back the slash
			c = from.read();
			if (c < 0)
			{
				__c[o++] = '\\';
				return (o - __o);
			}
			
			// Not a unicode sequence
			if (c != 'u')
			{
				__c[o++] = (char)c;
				continue;
			}
			
			throw new todo.TODO();
		}
		
		return __l;
	}
}

