// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.suite;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This represents the name of a standard.
 *
 * @since 2017/12/05
 */
public final class StandardName
	implements Comparable<StandardName>
{
	/**
	 * Initializes the standard name.
	 *
	 * @param __s The name of the standard.
	 * @throws InvalidSuiteException If the name is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/05
	 */
	public StandardName(String __s)
		throws InvalidSuiteException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/05
	 */
	@Override
	public int compareTo(StandardName __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/05
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/05
	 */
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/05
	 */
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
}

