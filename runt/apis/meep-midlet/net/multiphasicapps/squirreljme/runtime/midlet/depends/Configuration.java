// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.midlet.depends;

import net.multiphasicapps.squirreljme.runtime.midlet.id.APIName;
import net.multiphasicapps.squirreljme.runtime.midlet.id.SuiteVersion;
import net.multiphasicapps.strings.StringUtils;

/**
 * This represents a configuration such as CLDC which specifies which base
 * classes are available. Configurations may optionally be "compact" in which
 * they are a lighter version.
 *
 * @since 2016/12/14
 */
public final class Configuration
	implements Comparable<Configuration>
{
	/**
	 * Initializes the configuration using the given API name and version.
	 *
	 * @param __n The name to use.
	 * @param __v The version of the suite.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/30
	 */
	public Configuration(APIName __n, SuiteVersion __v)
		throws NullPointerException
	{
		if (n == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Initializes the configuration by parsing the given string.
	 *
	 * @param __s The string to parse.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/30
	 */
	public Configuration(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/30
	 */
	@Override
	public int compareTo(Configuration __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/30
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/30
	 */
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/30
	 */
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
}

