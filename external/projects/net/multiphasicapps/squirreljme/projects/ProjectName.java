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
		
		throw new Error("TODO");
	}
	
	/**
	 * Initializes the project name from the namespace and name individually.
	 *
	 * @param __namespace The project namespace.
	 * @param __name The project name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/20
	 */
	public ProjectName(String __namespace, String __name)
		throws NullPointerException
	{
		this(Objects.<String>requireNonNull(__namespace, "NARG") + "@" +
			Objects.<String>requireNonNull(__name, "NARG"));
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
}

