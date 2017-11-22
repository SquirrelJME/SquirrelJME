// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.support;

/**
 * This represents a dependency which a project has, it is based on how the
 * dependency system for MEEP operates.
 *
 * @since 2017/11/17
 */
public final class Dependency
{
	/**
	 * Initializes the dependency from the decoded manifest string.
	 *
	 * @param __s The String to decode.
	 * @throws InvalidDependencyException If the dependency is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/21
	 */
	public Dependency(String __s)
		throws InvalidDependencyException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns whether this dependency is optional.
	 *
	 * @return Whether this an optional dependency.
	 * @since 2017/11/21
	 */
	public boolean isOptional()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns whether this dependency is required.
	 *
	 * @return Whether this is a required dependency.
	 * @since 2017/11/21
	 */
	public boolean isRequired()
	{
		throw new todo.TODO();
	}
}

