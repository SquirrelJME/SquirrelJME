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
 * This represents a dependency that source code may rely on.
 *
 * @since 2016/10/25
 */
public final class SourceDependency
	implements Comparable<SourceDependency>
{
	/** The project type. */
	protected final ProjectType type;
	
	/** The project name. */
	protected final ProjectName name;
	
	/** String representation of the dependency. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the dependency from a type and project name.
	 *
	 * @param __t The project type.
	 * @param __n The project name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/25
	 */
	public SourceDependency(ProjectType __t, ProjectName __n)
		throws NullPointerException
	{
		// Check
		if (__t == null || __n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.type = __t;
		this.name = __n;
	}
	
	/**
	 * Initializes the dependency from the string representation of it.
	 *
	 * @param __s The input string.
	 * @throws InvalidProjectException If the representation is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/25
	 */
	public SourceDependency(String __s)
		throws InvalidProjectException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CI0c Expected the source dependency to be in
		// the specified form of {@code type:name}. (The input string)}
		int co = __s.indexOf(':');
		if (co < 0)
			throw new InvalidProjectException(String.format("CI0c %s", __s));
		
		// Create
		ProjectType type;
		try
		{
			this.type = (type = ProjectType.of(__s.substring(0, co)));
		}
		
		// {@squirreljme.error CI0d Unknown project dependency type.}
		catch (IllegalArgumentException e)
		{
			throw new InvalidProjectException("CI0d");
		}
		
		// Refers to an API
		if (type == ProjectType.API)
			throw new Error("TODO");
		
		// Refers to a project
		else
			this.name = new ProjectName(__s.substring(co + 1));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/25
	 */
	@Override
	public int compareTo(SourceDependency __o)
	{
		// Compare type first
		int rv = this.type.compareTo(__o.type);
		if (rv != 0)
			return rv;
		
		// Then the name
		return this.name.compareTo(__o.name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/25
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof SourceDependency))
			return false;
		
		// Cast and check
		SourceDependency o = (SourceDependency)__o;
		return this.type.equals(o.type) && this.name.equals(o.name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/25
	 */
	@Override
	public int hashCode()
	{
		return this.type.hashCode() ^ this.name.hashCode();
	}
	
	/**
	 * Returns the project name.
	 *
	 * @return The project name.
	 * @since 2016/10/25
	 */
	public ProjectName projectName()
	{
		return this.name;
	}
	
	/**
	 * Returns the project type.
	 *
	 * @return The project type.
	 * @since 2016/10/25
	 */
	public ProjectType projectType()
	{
		return this.type;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/25
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = this.type + ":" +
				Objects.toString(this.name, "<FIXME>")));
		
		return rv;
	}
}

