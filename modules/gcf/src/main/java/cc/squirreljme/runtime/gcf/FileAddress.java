// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

/**
 * This is an address which represents a file.
 *
 * @since 2019/05/06
 */
public final class FileAddress
	implements SocketAddress
{
	/** The file. */
	public final String file;
	
	/**
	 * Initializes the file address.
	 *
	 * @param __p The file path.
	 * @throws IllegalArgumentException If the path is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	public FileAddress(String __p)
		throws IllegalArgumentException, NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Decode the path
		this.file = FileAddress.stringDecode(__p);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final String toString()
	{
		return this.file;
	}
	
	/**
	 * Decodes the specified string to handle the path.
	 *
	 * @param __p The path.
	 * @return The decoded path.
	 * @throws IllegalArgumentException If the path is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	public static final String stringDecode(String __p)
		throws IllegalArgumentException, NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Build new decoded path
		StringBuilder sb = new StringBuilder();
		for (int i = 0, n = __p.length(); i < n;)
		{
			int c = __p.charAt(i++);
			
			// Potentially condense slashes
			if (c == '/')
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
				throw new todo.TODO();
			}
			
			// Directly usable
			else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') ||
				(c >= '0' && c <= '9') || c == ':' || c == '@' || c == '-' ||
				c == '.' || c == '_' || c == '~' || c == '!' || c == '$' ||
				c == '&' || c == '\'' || c == '(' || c == ')' || c == '*' ||
				c == '+' || c == ',' || c == ';' || c == '=')
				sb.append((char)c);
			
			// {@squirreljme.error EC01 Path contains invalid character.
			// (The path)}
			else
				throw new IllegalArgumentException("EC01 " + __p);
		}
		
		// Done
		return sb.toString();
	}
	
	/**
	 * Decodes and build the file address.
	 *
	 * @param __p The string address.
	 * @return The resulting file address.
	 * @throws IllegalArgumentException If the path is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	public static final FileAddress of(String __p)
		throws IllegalArgumentException, NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		return new FileAddress(__p);
	}
}

