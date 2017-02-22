// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.suiteid;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * This represents a dependency that a LIBlet or MIDlet may depend on.
 *
 * @since 2017/02/22
 */
public final class MidletDependency
{
	/** The dependency type. */
	protected final MidletDependencyType type;
	
	/** The dependency level. */
	protected final MidletDependencyLevel level;
	
	/** The name. */
	protected final MidletSuiteName name;
	
	/** The vendor. */
	protected final MidletSuiteVendor vendor;
	
	/** The version range. */
	protected final MidletVersionRange version;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the dependency which is parsed from the given input string.
	 *
	 * @param __s The string to parse.
	 * @throws IllegalArgumentException If the string is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/22
	 */
	public MidletDependency(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Trim whitespace
		__s = __s.trim();
		
		// Extract all semicolon positions
		int[] sc = new int[4];
		for (int i = 0; i < 5; i++)
		{
			int lastpos = (i == 0 ? 0 : sc[i - 1] + 1);
			
			// {@squirreljme.error CC0e Expected four semi-colons in the
			// dependency field. (The input dependency)}
			int com = __s.indexOf(';', lastpos);
			if ((i < 4 && com < 0) || (i >= 4 && com >= 0))
				throw new IllegalArgumentException(String.format(
					"CC0e %s", __s));
			
			// Stop
			if (i == 4)
				break;
			
			// Store
			sc[i] = com;
		}
		
		// Split fields
		String intype = __s.substring(0, sc[0]).trim(),
			inlevel = __s.substring(sc[0] + 1, sc[1]).trim(),
			inname = __s.substring(sc[1] + 1, sc[2]).trim(),
			invendor = __s.substring(sc[2] + 1, sc[3]).trim(),
			inversion = __s.substring(sc[3] + 1).trim();
		
		// Required fields
		this.type = MidletDependencyType.of(intype);
		this.level = MidletDependencyLevel.of(inlevel);
		
		// Optional fields
		this.name = (inname.isEmpty() ? null : new MidletSuiteName(inname));
		this.vendor = (invendor.isEmpty() ? null :
			new MidletSuiteVendor(invendor));
		this.version = (inversion.isEmpty() ? null :
			new MidletVersionRange(inversion));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/22
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof MidletDependency))
			return false;
		
		// Compare
		MidletDependency o = (MidletDependency)__o;
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/22
	 */
	@Override
	public int hashCode()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the dependency level.
	 *
	 * @return The dependency level.
	 * @since 2017/02/22
	 */
	public MidletDependencyLevel level()
	{
		return this.level;
	}
	
	/**
	 * Returns the dependency name.
	 *
	 * @return The dependency name, may be {@code null}.
	 * @since 2017/02/22
	 */
	public MidletSuiteName name()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/22
	 */
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the dependency type.
	 *
	 * @return The dependency type.
	 * @since 2017/02/22
	 */
	public MidletDependencyType type()
	{
		return this.type;
	}
	
	/**
	 * Returns the dependency vendor.
	 *
	 * @return The dependency vendor, may be {@code null}.
	 * @since 2017/02/22
	 */
	public MidletSuiteVendor vendor()
	{
		return this.vendor;
	}
	
	/**
	 * Returns the dependency version.
	 *
	 * @return The dependency version, may be {@code null}.
	 * @since 2017/02/22
	 */
	public MidletVersionRange version()
	{
		return this.version;
	}
}

