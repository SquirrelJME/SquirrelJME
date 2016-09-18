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
 * This is a project group which contains a reference to binary and/or
 * source projects.
 *
 * @since 2016/09/18
 */
public final class ProjectGroup
	implements Comparable<ProjectGroup>
{
	/** Internal lock. */
	protected final Object lock =
		new Object();
	
	/** The owning project list. */
	protected final ProjectList list;
	
	/** The name of the project group. */
	protected final ProjectName name;
	
	/** The current binary project. */
	private volatile ProjectInfo _bin;
	
	/** The current source project. */
	private volatile ProjectInfo _src;
	
	/**
	 * Initializes the project group.
	 *
	 * @param __pl The owning package list.
	 * @param __n The name of this package.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	ProjectGroup(ProjectList __pl, ProjectName __n)
		throws NullPointerException
	{
		// Check
		if (__pl == null || __n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.list = __pl;
		this.name = __n;
	}
	
	/**
	 * This returns the associated binary project which contains class files
	 * and other resource.
	 *
	 * @return The binary project information, or {@code null} if there is no
	 * binary project.
	 * @since 2016/09/18
	 */
	public final ProjectInfo binary()
	{
		// Lock
		synchronized (this.lock)
		{
			return this._bin;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/18
	 */
	@Override
	public final int compareTo(ProjectGroup __o)
	{
		return this.name.compareTo(__o.name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/18
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof ProjectGroup))
			return false;
		
		// Compare
		return this.name.equals(((ProjectGroup)__o).name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/18
	 */
	@Override
	public final int hashCode()
	{
		return this.name.hashCode();
	}
	
	/**
	 * This returns the associated source project which contains source code
	 * and other resource.
	 *
	 * @return The source project information, or {@code null} if there is no
	 * source project.
	 * @since 2016/09/18
	 */
	public final ProjectInfo source()
	{
		// Lock
		synchronized (this.lock)
		{
			return this._src;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/18
	 */
	@Override
	public final String toString()
	{
		return this.name.toString();
	}
	
	/**
	 * Sets the binary project.
	 *
	 * @param __pi The project to set.
	 * @throws IllegalStateException If the name does not match.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	final void __setBinary(ProjectInfo __pi)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__pi == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CI06 Attempt to set binary project to a group
		// of a different name.}
		if (!this.name.equals(__pi.name()))
			throw new IllegalStateException("CI06");
		
		// Lock
		synchronized (this.lock)
		{
			this._bin = __pi;
		}
	}
	
	/**
	 * Sets the source project.
	 *
	 * @param __pi The project to set.
	 * @throws IllegalStateException If the name does not match.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	final void __setSource(ProjectInfo __pi)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__pi == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CI09 Attempt to set source project to a group
		// of a different name.}
		if (!this.name.equals(__pi.name()))
			throw new IllegalStateException("CI09");
		
		// Lock
		synchronized (this.lock)
		{
			this._src = __pi;
		}
	}
}

