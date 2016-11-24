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

import java.nio.file.Path;
import java.util.Objects;

/**
 * This represents the base for all projects which are associated with the
 * project manager.
 *
 * @since 2016/11/20
 */
public abstract class BaseProject
{
	/** The location representing the project. */
	protected final Path path;
	
	/** The basic project name. */
	protected final String basicname;
	
	/**
	 * Initializes the base project.
	 *
	 * @param __p The path to the project.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/20
	 */
	BaseProject(Path __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.path = __p;
		
		// The basic name is just the file name of the project directory or
		// similar
		this.basicname = __basify(Objects.toString(__p.getFileName(),
			__p.toString()));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/11/24
	 */
	@Override
	public abstract int hashCode();
	
	/**
	 * Returns the basic name of the project, which is derived from the base
	 * path.
	 *
	 * @return The basic project name.
	 * @since 2016/11/24
	 */
	public final String basicName()
	{
		return this.basicname;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/11/24
	 */
	@Override
	public final boolean equals(Object __o)
	{
		return this == __o;
	}
	
	/**
	 * Returns the path to this project.
	 *
	 * @return The project path.
	 * @since 2016/11/20
	 */
	public final Path path()
	{
		return this.path;
	}
	
	/**
	 * Turns a path file name to a basic project name.
	 *
	 * @param __s The string to make basic.
	 * @return The basic form of the given string.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/24
	 */
	private static String __basify(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Lowercase letters
		int n = __s.length();
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++)
		{
			char c = __s.charAt(i);
			
			// Lowercase
			if (c >= 'A' && c <= 'Z')
				c = (char)('a' + (c - 'A'));
			
			sb.append(c);
		}
		
		// Make sure JAR is not at the end
		String rv = sb.toString();
		if (rv.endsWith(".jar"))
			return rv.substring(0, rv.length() - 4);
		return rv;
	}
}

