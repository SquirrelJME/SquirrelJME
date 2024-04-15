// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.swm;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This represents a profile that may be implemented, such as MIDP.
 *
 * @since 2016/12/14
 */
public final class JavaMEProfile
	implements Comparable<JavaMEProfile>, MarkedDependency, MarkedProvided
{
	/** Name. */
	protected final APIName name;
	
	/** Version. */
	protected final SuiteVersion version;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the profile using the given API name and version.
	 *
	 * @param __n The name to use.
	 * @param __v The version of the suite, this is optional.
	 * @throws NullPointerException If no name was specified.
	 * @since 2017/11/30
	 */
	public JavaMEProfile(APIName __n, SuiteVersion __v)
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
	 * @param __n The string to parse.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/30
	 */
	public JavaMEProfile(String __n)
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
	public int compareTo(JavaMEProfile __o)
	{
		int rv = this.name.compareTo(__o.name);
		if (rv != 0)
			return rv;
		
		SuiteVersion a = this.version,
			b = __o.version;
		if ((a == null) != (b == null))
			return (a == null ? -1 : 1);
		else if (a != null)
			return a.compareTo(b);
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/30
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof JavaMEProfile))
			return false;
		
		JavaMEProfile o = (JavaMEProfile)__o;
		return this.name.equals(o.name) &&
			Objects.equals(this.version, o.version);
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
	 * @since 2017/12/31
	 */
	@Override
	public boolean isOptional()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public boolean matchesProvided(MarkedProvided __mp)
		throws NullPointerException
	{
		if (__mp == null)
			throw new NullPointerException("NARG");
		
		return this.equals(__mp);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/30
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = this.name + "-" + this.version));
		
		return rv;
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
	
	/**
	 * Parses a list of profiles from the string.
	 * 
	 * @param __input The string to parse from.
	 * @return The parsed profiles.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/09
	 */
	public static Set<JavaMEProfile> parseProfiles(String __input)
		throws NullPointerException
	{
		if (__input == null)
			throw new NullPointerException("NARG");
		
		Set<JavaMEProfile> result = new LinkedHashSet<>();
		for (String value : __input.split(" \t"))
			result.add(new JavaMEProfile(value));
		
		return result;
	}
	
	/**
	 * Converts the collection of profiles to a string used in property files.
	 * 
	 * @param __profiles The profile to convert.
	 * @return The converted property string.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/09
	 */
	public static String toString(Collection<JavaMEProfile> __profiles)
		throws NullPointerException
	{
		if (__profiles == null)
			throw new NullPointerException("NARG");
		
		StringBuilder sb = new StringBuilder();
		for (JavaMEProfile profile : __profiles)
		{
			if (sb.length() > 0)
				sb.append(' ');
			
			sb.append(profile);
		}
		
		return sb.toString();
	}
}

