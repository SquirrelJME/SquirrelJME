// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This class contains HTTP utilities.
 *
 * @since 2022/10/11
 */
public final class HTTPUtils
{
	/**
	 * Not used.
	 * 
	 * @since 2022/10/11
	 */
	private HTTPUtils()
	{
	}
	
	/**
	 * Decodes the specified string to handle different parts of the URL which
	 * may be encoded or not.
	 *
	 * @param __charSet The character set to use.
	 * @param __p The string.
	 * @return The decoded string.
	 * @throws IllegalArgumentException If the string is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	public static String stringDecode(HTTPUrlCharacterSet __charSet,
		String __p)
		throws IllegalArgumentException, NullPointerException
	{
		if (__charSet == null || __p == null)
			throw new NullPointerException("NARG");
		
		// Build new decoded path
		StringBuilder sb = new StringBuilder();
		for (int i = 0, n = __p.length(); i < n;)
		{
			int c = __p.charAt(i++);
			
			// Potentially condense slashes, paths only
			if (c == '/' && __charSet == HTTPUrlCharacterSet.PATH)
			{
				// Do not condense
				int sbl = sb.length();
				if (sbl > 0 && sb.charAt(sbl - 1) == '/')
					continue;
				
				// Add slash
				sb.append('/');
			}
			
			// Percent encoded
			else if (c == '%')
			{
				throw Debugging.todo();
			}
			
			// Directly usable
			else if (__charSet.isValid((char)c))
				sb.append((char)c);
			
			// {@squirreljme.error EC01 String contains invalid character.
			// (The path)}
			else
				throw new IllegalArgumentException("EC01 " + __p);
		}
		
		// Done
		return sb.toString();
	}
}
