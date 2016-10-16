// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.projects;

/**
 * This represents the namespace name of a project.
 *
 * @since 2016/10/16
 */
public final class ProjectNamespace
	implements Comparable<ProjectNamespace>
{
	/** The name of the namespace. */
	protected final String string;
	
	/**
	 * Initializes the namespace name.
	 *
	 * @param __n The name of the namespace.
	 * @throws InvalidProjectException If the name is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/16
	 */
	public ProjectNamespace(String __n)
		throws InvalidProjectException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Setup
		StringBuilder sb = new StringBuilder();
		int n = __n.length();
		for (int i = 0; i < n; i++)
		{
			char c = __n.charAt(i);
			
			// Keep as is
			if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '-')
				sb.append(c);
			
			// Lowercase
			else if (c >= 'A' && c <= 'Z')
				sb.append((char)('a' + (c - 'A')));
			
			// {@squirreljme.error CI0h The specified namespace contains an
			// illegal character. (The specified namespace)}
			else
				throw new InvalidProjectException(String.format("CI0h %s",
					__n));
		}
		
		// Use it
		this.string = sb.toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/16
	 */
	@Override
	public int compareTo(ProjectNamespace __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		return this.string.compareTo(__o.string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/16
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof ProjectNamespace))
			return false;
		
		// Compare
		return this.string.equals(((ProjectNamespace)__o).string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/16
	 */
	@Override
	public int hashCode()
	{
		return this.string.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/16
	 */
	@Override
	public String toString()
	{
		return this.string;
	}
}

