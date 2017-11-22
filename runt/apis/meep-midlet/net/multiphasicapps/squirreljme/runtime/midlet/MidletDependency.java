// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.midlet;

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
			
			// {@squirreljme.error AD07 Expected four semi-colons in the
			// dependency field. (The input dependency)}
			int com = __s.indexOf(';', lastpos);
			if ((i < 4 && com < 0) || (i >= 4 && com >= 0))
				throw new IllegalArgumentException(String.format(
					"AD07 %s", __s));
			
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
		MidletDependencyType type;
		this.type = (type = MidletDependencyType.of(intype));
		this.level = MidletDependencyLevel.of(inlevel);
		
		// Optional fields
		MidletSuiteName name;
		MidletSuiteVendor vendor;
		MidletVersionRange version;
		this.name = (name = (inname.isEmpty() ? null :
			new MidletSuiteName(inname)));
		this.vendor = (vendor = (invendor.isEmpty() ? null :
			new MidletSuiteVendor(invendor)));
		this.version = (version = (inversion.isEmpty() ? null :
			new MidletVersionRange(inversion)));
		
		// {@squirreljme.error AD08 Dependencies on LIBlets must have the
		// name, vendor, and version set. (The input string)}
		if (type == MidletDependencyType.LIBLET && (name == null ||
			vendor == null || version == null))
			throw new IllegalArgumentException(String.format("AD08 %s", __s));
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
		return this.type.equals(o.type) &&
			this.level.equals(o.level) &&
			Objects.equals(this.name, o.name) &&
			Objects.equals(this.vendor, o.vendor) &&
			Objects.equals(this.version, o.version);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/22
	 */
	@Override
	public int hashCode()
	{
		return this.type.hashCode() ^
			this.level.hashCode() ^
			Objects.hashCode(this.name) ^
			Objects.hashCode(this.vendor) ^
			Objects.hashCode(this.version);
	}
	
	/**
	 * Checks if this dependency matches the specified name, vendor, and
	 * version.
	 *
	 * @param __n The name.
	 * @param __e The vendor.
	 * @param __v The version.
	 * @return {@code true} if the details match for a library.
	 * @since 2017/02/22
	 */
	public boolean isCompatible(MidletSuiteName __n, MidletSuiteVendor __e,
		MidletVersion __v)
	{
		MidletVersionRange version = this.version;
		return Objects.equals(this.name, __n) &&
			Objects.equals(this.vendor, __e) &&
			(version != null && __v != null ? version.inRange(__v) :
				(version == null) == (__v == null));
	}
	
	/**
	 * Is this an optional dependency?
	 *
	 * @return {@code true} if this is an optional dependency.
	 * @since 2017/11/22
	 */
	public boolean isOptional()
	{
		return this.level.isOptional();
	}
	
	/**
	 * Is this an required dependency?
	 *
	 * @return {@code true} if this is an required dependency.
	 * @since 2017/11/22
	 */
	public boolean isRequired()
	{
		return this.level.isRequired();
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
		// Get
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// These are optional
			MidletSuiteName name = this.name;
			MidletSuiteVendor vendor = this.vendor;
			MidletVersionRange version = this.version;
			
			// Generate
			this._string = new WeakReference<>((rv = String.format(
				"%s;%s;%s;%s;%s", this.type, this.level,
				(name == null ? "" : name),
				(vendor == null ? "" : vendor),
				(version == null ? "" : version))));
		}
		
		// Return it
		return rv;
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

