// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This represents the name of a package a class resides in.
 *
 * This class is immutable.
 *
 * @since 2017/06/15
 */
public final class PackageName
	implements Comparable<PackageName>
{
	/**
	 * Initializes the package name.
	 *
	 * @param __n The name of the package.
	 * @throws JITException If the package name is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/07
	 */
	public PackageName(String __n)
		throws JITException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/15
	 */
	@Override
	public int compareTo(PackageName __o)
	{
		throw new todo.TODO();
	}
}

