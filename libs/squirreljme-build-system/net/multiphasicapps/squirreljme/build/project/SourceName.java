// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.project;

/**
 * This represents the name of a source project.
 *
 * @since 2017/10/31
 */
public final class SourceName
	implements Comparable<SourceName>
{
	/** The name string. */
	protected final String name;
	
	/**
	 * Initailizes the source name.
	 *
	 * @param __n The name of the source.
	 * @throws InvalidSourceNameException If the name is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	public SourceName(String __n)
		throws InvalidSourceNameException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Check for valid characters and lowercase
		StringBuilder sb = new StringBuilder();
		for (int i = 0, n = __n.length(); i < n; i++)
		{
			char c = __n.charAt(i);
			
			// {@squirreljme.error AO0h The project source name contains an
			// invalid character. (The specified character)}
			if (c <= ' ' || c >= 0x7F)
				throw new InvalidSourceNameException(
					String.format("AO0h %c", c));
			
			// Lowercase
			else if (c >= 'A' && c <= 'Z')
				c = (char)((c - 'A') + 'a');
			
			sb.append(c);
		}
		
		// Set
		this.name = sb.toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/31
	 */
	@Override
	public int compareTo(SourceName __n)
	{
		return this.name.compareTo(__n.name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/31
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (!(__o instanceof SourceName))
			return false;
		
		return this.name.equals(((SourceName)__o).name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/31
	 */
	@Override
	public int hashCode()
	{
		return this.name.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/31
	 */
	@Override
	public String toString()
	{
		return this.name;
	}
}

