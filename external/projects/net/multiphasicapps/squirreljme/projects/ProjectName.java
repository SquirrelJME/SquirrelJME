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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * This class represents the name of a package, which is case insensitive.
 *
 * The name is in the form of "namespace@project".
 *
 * @since 2016/06/15
 */
public final class ProjectName
	implements Comparable<ProjectName>
{
	/** The namespace the project is in. */
	protected final String namespace;
	
	/** The name of the project. */
	protected final String name;
	
	/** The full string representation. */
	private volatile Reference<String> _full;
	
	/**
	 * Initializes the project name from the full project form.
	 *
	 * @param __s The name of the project.
	 * @throws InvalidProjectException If the specified name is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/15
	 */
	public ProjectName(String __s)
		throws InvalidProjectException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CI0q The project name and namespace pair must
		// contain the at sign to be in the form of {@code namespace@name}.
		// (The input string to be parsed as a pair)}
		int at = __s.indexOf('@');
		if (at < 0)
			throw new InvalidProjectException(String.format("CI0q %s", __s));
		
		// Set
		this.namespace = __correctName(__s.substring(0, at));
		this.name = __correctName(__s.substring(at + 1));
	}
	
	/**
	 * Initializes the project name from the namespace and name individually.
	 *
	 * @param __namespace The project namespace.
	 * @param __name The project name.
	 * @throws InvalidProjectException If the namespace or project name
	 * contains an invalid character.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/20
	 */
	public ProjectName(String __namespace, String __name)
		throws InvalidProjectException, NullPointerException
	{
		// Check
		if (__namespace == null || __name == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.namespace = __correctName(__namespace);
		this.name = __correctName(__name);
	}
	
	/**
	 * {@inheritDoc]}
	 * @since 2016/06/15
	 */
	@Override
	public int compareTo(ProjectName __pn)
	{
		return __compareTo(__pn);
	}
	
	/**
	 * {@inheritDoc]}
	 * @since 2016/06/15
	 */
	@Override
	public boolean equals(Object __o)
	{
		// If another package, use normal comparison
		if (__o instanceof ProjectName)
			return 0 == __compareTo((ProjectName)__o);
		
		// Will never match
		return false;
	}
	
	/**
	 * {@inheritDoc]}
	 * @since 2016/06/15
	 */
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
	/**
	 * Returns the name of the project.
	 *
	 * @return The project name.
	 * @since 2016/10/20
	 */
	public String name()
	{
		return this.name;
	}
	
	/**
	 * Returns the namespace that this project is within.
	 *
	 * @return The namespace this exists in.
	 * @since 2016/10/20
	 */
	public String namespace()
	{
		return this.namespace;
	}
	
	/**
	 * {@inheritDoc]}
	 * @since 2016/06/15
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._full;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._full = new WeakReference<>((rv = this.namespace + "@" +
				this.name));
		
		return rv;
	}
	
	/**
	 * Compares this package name against another.
	 *
	 * @param __pn The name to compare against.
	 * @return The result of the comparison.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/15
	 */
	private final int __compareTo(ProjectName __pn)
		throws NullPointerException
	{
		// Check
		if (__pn == null)
			throw new NullPointerException("NARG");
		
		// Compare it
		return __compareTo(__pn.name);
	}
	
	/**
	 * Compares the name of this package against another.
	 *
	 * @param __pn The other name to compare against.
	 * @return The result of the comparison.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/15
	 */
	private final int __compareTo(String __pn)
		throws NullPointerException
	{
		// Check
		if (__pn == null)
			throw new NullPointerException("NARG");
		
		// Get both
		String a = toString(),
			b = __pn.toString();
		
		// Get lengths
		int na = a.length(),
			nb = b.length();
		int lim = Math.min(na, nb);
		
		// Compare
		for (int i = 0; i < lim; i++)
		{
			// Get
			char ca = a.charAt(i),
				cb = b.charAt(i);
			
			// Can lowercase?
			if (ca >= 'A' && ca <= 'Z')
				ca = (char)((ca - 'A') + 'a');
			
			// Lower
			if (ca < cb)
				return -1;
			
			// Higher?
			else if (ca > cb)
				return 1;
		}
		
		// Shorter?
		if (na < nb)
			return -1;
		
		// Longer?
		else if (na > nb)
			return 1;
		
		// Strings are equal
		return 0;
	}
	
	/**
	 * Checks and returns the correct name.
	 *
	 * @param __n The name to check.
	 * @return The corrected name, if no uppercase characters are in the
	 * string then {@code __n} is returned.
	 * @throws InvalidProjectException If there is an invalid character in
	 * the name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/20
	 */
	private static String __correctName(String __n)
		throws InvalidProjectException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CI0p The namespace or project name cannot be
		// an empty string.}
		int n = __n.length();
		if (n <= 0)
			throw new InvalidProjectException("CI0p");
		
		// Go through all characters
		boolean dolower = false;
		for (int i = 0; i < n; i++)
		{
			char c = __n.charAt(i);
			
			// Is fine
			if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '-')
				continue;
			
			// Lowercase?
			else if (c >= 'A' && c <= 'Z')
				dolower = true;
			
			// {@squirreljme.error CI05 An invalid character is in the
			// project namespace or name. (The input name)}
			else
				throw new InvalidProjectException(String.format("CI05 %s",
					__n));
		}
		
		// No lowercasing, return the same string
		if (!dolower)
			return __n;
		
		// Lowercase all characters
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++)
		{
			char c = __n.charAt(i);
			
			// Lowercase
			if (c >= 'A' && c <= 'Z')
				sb.append((char)((c - 'A') + 'a'));
			
			// Keep
			else
				sb.append(c);
		}
		
		// Build it
		return sb.toString();
	}
}

