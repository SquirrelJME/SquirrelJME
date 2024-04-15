// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

/**
 * Represents a Fossil version.
 *
 * @since 2020/06/25
 */
public class FossilVersion
	implements Comparable<FossilVersion>
{
	/** Major version. */
	public final int major;
	
	/** Minor version. */
	public final int minor;
	
	/**
	 * Parses the fossil version.
	 * 
	 * @param __s The string to parse.
	 * @throws IllegalArgumentException If the string is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/25
	 */
	public FossilVersion(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// We need this dot!
		int dot = __s.indexOf('.');
		if (dot < 0)
			throw new IllegalArgumentException("Expected dot in: " + __s);
		
		// We need to try to parse numbers
		int major, minor;
		try
		{
			// Decode major version
			major = Integer.parseInt(__s.substring(0, dot), 10);
			
			// Get the positions of the next possible characters
			String sub = __s.substring(dot + 1);
			int sDot = sub.indexOf('.');
			int sSlash = sub.indexOf('-');
			int sSpace = sub.indexOf(' ');
			
			// Try to find the sub index
			int dx = Math.min(Math.min((sDot >= 0 ? sDot : Integer.MAX_VALUE),
				(sSlash >= 0 ? sSlash : Integer.MAX_VALUE)),
				(sSpace >= 0 ? sSpace : Integer.MAX_VALUE));
			if (dx == Integer.MAX_VALUE)
				dx = sub.length();
			
			// Parse that
			minor = Integer.parseInt(sub.substring(0, dx));
		}
		
		// Did not parse a number
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException("Could not parse: " + __s, e);
		}
		
		// Perform some checks
		if (major <= 0 || minor < 0)
			throw new IllegalArgumentException("Illegal version: " + __s);
		
		// Use these
		this.major = major;
		this.minor = minor;
	}
	
	/**
	 * Initializes the version.
	 * 
	 * @param __maj The major version.
	 * @param __min The minor version.
	 * @since 2020/06/25
	 */
	public FossilVersion(int __maj, int __min)
	{
		this.major = __maj;
		this.minor = __min;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/25
	 */
	@Override
	public int compareTo(FossilVersion __other)
	{
		int rv = Integer.compare(this.major, __other.major);
		if (rv != 0)
			return rv;
		
		return Integer.compare(this.minor, __other.minor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/25
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof FossilVersion))
			return false;
		
		FossilVersion o = (FossilVersion)__o;
		return this.major == o.major && this.minor == o.minor;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/25
	 */
	@Override
	public final int hashCode()
	{
		return (this.major << 8) + this.minor;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/25
	 */
	@Override
	public final String toString()
	{
		return this.major + "." + this.minor;
	}
}
