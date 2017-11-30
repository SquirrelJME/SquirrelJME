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
import net.multiphasicapps.strings.StringUtils;

/**
 * This represents a dependency that a LIBlet or MIDlet may depend on.
 *
 * @since 2017/02/22
 */
public final class MidletDependency
	implements ManifestedDependency
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
	 * @throws InvalidMidletException If the string is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/22
	 */
	public MidletDependency(String __s)
		throws InvalidMidletException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Trim whitespace
		__s = __s.trim();
		
		// Extract all semicolon positions
		int[] sc = StringUtils.multipleIndexOf(__s, ';');
		
		// {@squirreljme.error AD0o Expected four semi-colons in the
		// dependency field. (The input dependency)}
		if (sc.length != 4)
			throw new InvalidMidletException(String.format(
				"AD0o %s", __s));
		
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
		
		// Check
		__check(type, level, name, vendor, version);
	}
	
	/**
	 * Initializes the depedency with the given type, level, and where the
	 * remainder of the dependencies are parsed from the specified string.
	 *
	 * @param __type The type of dependency this is.
	 * @param __level The level of the dependency.
	 * @param __s The string to decode for the remainder of the dependency.
	 * @throws InvalidMidletException If the input parameters are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/26
	 */
	public MidletDependency(MidletDependencyType __type,
		MidletDependencyLevel __level, String __s)
		throws InvalidMidletException, NullPointerException
	{
		if (__type == null || __level == null || __s == null)
			throw new NullPointerException("NARG");
		
		// Trim whitespace
		__s = __s.trim();
		
		// Extract all semicolon positions
		int[] sc = StringUtils.multipleIndexOf(__s, ';');
		
		// {@squirreljme.error AD08 Expected two semi-colons in the
		// dependency field. (The input dependency)}
		if (sc.length != 2)
			throw new InvalidMidletException(String.format(
				"AD08 %s", __s));
		
		// Split fields
		String inname = __s.substring(0, sc[0]).trim(),
			invendor = __s.substring(sc[0] + 1, sc[1]).trim(),
			inversion = __s.substring(sc[1] + 1).trim();
		
		// Parse areas fields
		MidletSuiteName name;
		MidletSuiteVendor vendor;
		MidletVersionRange version;
		this.name = (name = (inname.isEmpty() ? null :
			new MidletSuiteName(inname)));
		this.vendor = (vendor = (invendor.isEmpty() ? null :
			new MidletSuiteVendor(invendor)));
		this.version = (version = (inversion.isEmpty() ? null :
			new MidletVersionRange(inversion)));
		
		__check(__type, __level, name, vendor, version);
		
		// Set
		this.type = __type;
		this.level = __level;
	}
	
	/**
	 * Initializes the dependency using the given parameters.
	 *
	 * @param __type The type of dependency this is.
	 * @param __level The level of the dependency.
	 * @param __name The name.
	 * @param __vendor The vendor.
	 * @param __version The version.
	 * @throws InvalidMidletException If the input parameters are not valid.
	 * @throws NullPointerException If no type and/or name were specified.
	 * @since 2017/11/26
	 */
	public MidletDependency(MidletDependencyType __type,
		MidletDependencyLevel __level, MidletSuiteName __name,
		MidletSuiteVendor __vendor, MidletVersionRange __version)
		throws InvalidMidletException, NullPointerException
	{
		if (__type == null || __level == null)
			throw new NullPointerException("NARG");
		
		__check(__type, __level, __name, __vendor, __version);
		
		// Set
		this.type = __type;
		this.level = __level;
		this.name = __name;
		this.vendor = __vendor;
		this.version = __version;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/22
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
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
	
	public boolean isCompatible(APIStandard __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/22
	 */
	@Override
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
	 * Returns a dependency which is the same as this one except that it is
	 * required.
	 *
	 * @return This dependency but required.
	 * @since 2017/11/26
	 */
	public MidletDependency toRequired()
	{
		if (isRequired())
			return this;
		return new MidletDependency(this.type, MidletDependencyLevel.REQUIRED,
			this.name, this.vendor, this.version);
	}
	
	/**
	 * Returns a dependency which is the same as this one except that it is
	 * optional.
	 *
	 * @return This dependency but optional.
	 * @since 2017/11/26
	 */
	public MidletDependency toOptional()
	{
		if (isOptional())
			return this;
		return new MidletDependency(this.type, MidletDependencyLevel.OPTIONAL,
			this.name, this.vendor, this.version);
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
	
	/**
	 * Checks whether the provided parameters are correct.
	 *
	 * @param __type The type of dependency this is.
	 * @param __level The level of the dependency.
	 * @param __name The name.
	 * @param __vendor The vendor.
	 * @param __version The version.
	 * @throws InvalidMidletException If the input parameters are not valid.
	 * @since 2017/11/26
	 */
	private static final void __check(MidletDependencyType __type,
		MidletDependencyLevel __level, MidletSuiteName __name,
		MidletSuiteVendor __vendor, MidletVersionRange __version)
		throws InvalidMidletException
	{
		// {@squirreljme.error AD09 Dependencies on LIBlets must have the
		// name, vendor, and version set. (The type; The level; The name;
		// The vendor; The version)}
		if (__type == MidletDependencyType.LIBLET && (__name == null ||
			__vendor == null || __version == null))
			throw new InvalidMidletException(
				String.format("AD09 %s %s %s %s %s", __type, __level, __name,
					__vendor, __version));
	}
}

