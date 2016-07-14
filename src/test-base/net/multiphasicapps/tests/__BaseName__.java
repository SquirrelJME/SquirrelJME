// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests;

/**
 * DESCRIBE THIS.
 *
 * @since 2016/07/11
 */
class __BaseName__
{
	/** The string representing the name. */
	protected final String string;
	
	/**
	 * Initializes the test base name.
	 *
	 * @param __n The name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/10
	 */
	__BaseName__(String __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Check name first for bad characters
		int n = __n.length();
		boolean bad = false;
		for (int i = 0; i < n && !bad; i++)
		{
			char c = __n.charAt(i);
			
			// Illegal character?
			bad |= !__validChar(c);
		}
		
		// If no bad characters, use it as is
		if (!bad)
			this.string = __n;
		
		// Otherwise fix it
		else
		{
			// Cleanup
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < n; i++)
			{
				char c = __n.charAt(i);
				
				// Lowercase uppers
				if (c >= 'A' && c <= 'Z')
					sb.append((char)(('a' + (c - 'A'))));
				
				// only keep valid
				else if (__validChar(c))
					sb.append(c);
			}
			
			// Finish
			this.string = sb.toString();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof __BaseName__))
			return false;
		
		// Cast
		__BaseName__ o = (__BaseName__)__o;
		return this.string.equals(o.string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final int hashCode()
	{
		return this.string.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final String toString()
	{
		return this.string;
	}
	
	/**
	 * Checks whether the given character is valid.
	 *
	 * @param __c The character to check.
	 * @return {@code true} if the character is valid.
	 * @since 2016/07/10
	 */
	private static boolean __validChar(char __c)
	{
		return __c == '.' || __c == '_' || (__c >= '0' && __c <= '9') ||
			(__c >= 'a' && __c <= 'z') || __c == '*';
	}
}

