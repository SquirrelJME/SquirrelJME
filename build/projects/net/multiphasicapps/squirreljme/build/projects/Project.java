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

import java.io.IOException;
import java.nio.file.Path;

/**
 * This class represents the base for projects. A project may be an API, a
 * MIDlet, or a LIBlet. However, most projects from the these distinctions are
 * very much the same. A project points to optional source code and binary
 * projects which may or may not exist.
 *
 * @since 2016/12/14
 */
public final class Project
{
	/** The owning project manager. */
	protected final ProjectManager projectman;
	
	/** The type of project this is. */
	protected final NamespaceType type;
	
	/** The name of this project. */
	protected final ProjectName name;
	
	/**
	 * Initializes the project.
	 *
	 * @param __pm The owning project manager.
	 * @param __t The type of namespace the project is in.
	 * @param __n The name of the project.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/14
	 */
	Project(ProjectManager __pm, NamespaceType __t, ProjectName __n)
		throws NullPointerException
	{
		// Check
		if (__pm == null || __t == null || __n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.projectman = __pm;
		this.type = __t;
		this.name = __n;
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/14
	 */
	@Override
	public boolean equals(Object __o)
	{
		return this == __o;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/14
	 */
	@Override
	public int hashCode()
	{
		return this.name.hashCode();
	}
	
	/**
	 * Returns the name of this project.
	 *
	 * @return The project name.
	 * @since 2016/12/14
	 */
	public final ProjectName name()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/14
	 */
	@Override
	public final String toString()
	{
		return this.name.toString();
	}
	
	/**
	 * Returns the type of project that this is.
	 *
	 * @return The project type.
	 * @since 2016/12/14
	 */
	public final NamespaceType type()
	{
		return this.type;
	}
	
	/**
	 * Initializes the project source from the given path.
	 *
	 * @param __p The path to the source code root.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/14
	 */
	final void __initializeSource(Path __p)
		throws IOException, NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * This represents a binary compilation of source code.
	 *
	 * Note that the compiled binary might not be derived from the source code.
	 *
	 * @since 2016/12/14
	 */
	public abstract class Binary
	{
	}
	
	/**
	 * This represents source code within a project which may be used to
	 * construct a binary from.
	 *
	 * @since 2016/12/14
	 */
	public abstract class Source
	{
	}
}

