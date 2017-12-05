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

import java.util.Objects;
import net.multiphasicapps.squirreljme.runtime.midlet.id.APIName;
import net.multiphasicapps.squirreljme.runtime.midlet.id.SuiteVersion;
import net.multiphasicapps.strings.StringUtils;

/**
 * This represents a profile that may be implemented, such as MIDP.
 *
 * @since 2016/12/14
 */
public final class Profile
	implements Comparable<Profile>
{
	/** Name. */
	protected final APIName name;
	
	/** Version. */
	protected final SuiteVersion version;
	
	/**
	 * Initializes the profile using the given API name and version.
	 *
	 * @param __n The name to use.
	 * @param __v The version of the suite, this is optional.
	 * @throws NullPointerException If no name was specified.
	 * @since 2017/11/30
	 */
	public Profile(APIName __n, SuiteVersion __v)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		this.name = __n;
		this.version = __v;
	}
	
	/**
	 * Initializes the profile by parsing the given string.
	 *
	 * @param __s The string to parse.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/30
	 */
	public Profile(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// No version specified
		int n = __n.length(),
			dx = __n.lastIndexOf('-');
		char c;
		if (dx < 0 || dx + 1 >= n || (c = __n.charAt(dx + 1)) < '0' || c > '9')
		{
			this.name = new APIName(__n);
			this.version = null;
		}
		
		// There is a version
		else
		{
			this.name = new APIName(__n.substring(0, dx));
			this.version = new SuiteVersion(__n.substring(dx + 1));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/30
	 */
	@Override
	public int compareTo(Profile __o)
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
		return this.name.hashCode() ^
			Objects.hashCode(this.version);
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
	
	/**
	 * Returns the version of this profile.
	 *
	 * @return The profile version.
	 * @since 2017/12/05
	 */
	public SuiteVersion version()
	{
		return this.version;
	}
}

