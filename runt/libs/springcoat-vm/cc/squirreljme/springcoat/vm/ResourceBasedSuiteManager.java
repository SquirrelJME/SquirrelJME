// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

/**
 * This is a suite manager which is based on resources for accessing various
 * classes and other resources.
 *
 * @since 2018/11/14
 */
public final class ResourceBasedSuiteManager
	implements SpringSuiteManager
{
	/**
	 * Initializes the suite manager.
	 *
	 * @param __act The class to source resources from.
	 * @param __prefix The prefix for library lookup.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/14
	 */
	public ResourceBasedSuiteManager(Class<?> __act, String __prefix)
		throws NullPointerException
	{
		if (__act == null || __prefix == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/14
	 */
	@Override
	public final String[] listLibraryNames()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/14
	 */
	@Override
	public final SpringClassLibrary loadLibrary(String __s)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
}

