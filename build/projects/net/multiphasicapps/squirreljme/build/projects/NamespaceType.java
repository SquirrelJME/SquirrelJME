// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.projects;

/**
 * This represents the types of namespaces which are avalable for usage.
 *
 * Namespaces may treat their project layouts differently compared to other
 * namespaces.
 *
 * @since 2016/10/28
 */
public enum NamespaceType
{
	/** Static assets such images and other non-compiling files. */
	ASSETS,
	
	/** Projects which are part of the build system. */
	BUILD,
	
	/** End. */
	;
	
	/** The string representation. */
	protected final String string =
		__lowerCase(name());
	
	/**
	 * {@inheritDoc}
	 * @since 201610/28
	 */
	@Override
	public String toString()
	{
		return this.string;
	}
	
	/**
	 * Lowercases all characters in the given string.
	 *
	 * @param __s The string to lowercase.
	 * @return The lowercase form of the given string.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/28
	 */
	private static String __lowerCase(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Lowercase all characters
		int n = __s.length();
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++)
		{
			char c = __s.charAt(i);
			
			// Lowercase
			if (c >= 'A' && c <= 'Z')
				sb.append((char)((c - 'A') + 'a'));
			
			// Keep
			else
				sb.append(c);
		}
		
		// Return it
		return sb.toString();
	}
}

