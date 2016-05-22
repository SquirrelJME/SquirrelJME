// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.imagereader.xpm;

import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;

/**
 * This is a character stripper which is used to remove C/C++ comments and only
 * return the character data that is between the first C string array
 * compatible type.
 *
 * If a comma appears inside of the string array, then the character that would
 * be returned in its place is a {@code '\n'} character. This is because XPM
 * is a line based format.
 *
 * @since 2016/05/22
 */
class __CharStripper__
	extends Reader
{
	/** Lock to make sure there is a consistent state. */
	protected final Object lock =
		new Object();
	
	/** The input reader. */
	protected final Reader in;
	
	/**
	 * Initializes the character stripper.
	 *
	 * @param __r The source to strip characters from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/22
	 */
	__CharStripper__(Reader __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.in = __r;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public void close()
		throws IOException
	{
		// Lock
		synchronized (this.lock)
		{
			this.in.close();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public int read()
		throws IOException
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public int read(char[] __b, int __o, int __l)
		throws IOException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (this.lock)
		{
			// Read loop
			for (int i = 0; i < __l; i++)
				try
				{
					// Read single character
					int c = read();
				
					// EOF?
					if (c < 0)
						return (i > 0 ? i : -1);
				
					// Set
					__b[__o + i] = (char)c;
				}
			
				// Failed read
				catch (IOException e)
				{
					// If nothing read, fail
					if (i <= 0)
						throw e;
				
					// Otherwise return the read count
					return i;
				}
		
			// All characters read
			return __l;
		}
	}
}

