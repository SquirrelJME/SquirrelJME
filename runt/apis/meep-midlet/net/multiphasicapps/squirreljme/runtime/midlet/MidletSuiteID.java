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

/**
 * This is the identity for a midlet suite which contains a name, vendor,
 * and version.
 *
 * @since 2016/10/12
 */
public final class MidletSuiteID
	implements Comparable<MidletSuiteID>
{
	/** The suite name. */
	protected final MidletSuiteName name;
	
	/** The suite vendor. */
	protected final MidletSuiteVendor vendor;
	
	/** The suite version. */
	protected final MidletVersion version;
	
	/** The string representation for JARs. */
	private volatile Reference<String> _jar;
	
	/** The string representation used by the IMC Connection. */
	private volatile Reference<String> _imc;
	
	/** String representation for dependencies. */
	private volatile Reference<String> _dep;
	
	/**
	 * Initializes the suite identifier.
	 *
	 * @param __name The name of the suite.
	 * @param __vendor The vendor of the suite.
	 * @param __ver The version of the suite.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/22
	 */
	public MidletSuiteID(MidletSuiteName __name, MidletSuiteVendor __ven,
		MidletVersion __ver)
		throws NullPointerException
	{
		this(__ven, __name, __ver);
	}
	
	/**
	 * Initializes the suite identifier.
	 *
	 * @param __vendor The vendor of the suite.
	 * @param __name The name of the suite.
	 * @param __ver The version of the suite.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/12
	 */
	public MidletSuiteID(MidletSuiteVendor __ven, MidletSuiteName __name,
		MidletVersion __ver)
		throws NullPointerException
	{
		// Check
		if (__ven == null || __name == null || __ver == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __name;
		this.vendor = __ven;
		this.version = __ver;
	}
	
	/**
	 * Initializes the suite identifier from the specified string.
	 *
	 * @param __s The string to decode.
	 * @param __f The format of the suite identifier.
	 * @throws InvalidMidletException If the string is malformed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/13
	 */
	public MidletSuiteID(String __s, MidletSuiteIDFormat __f)
		throws InvalidMidletException, NullPointerException
	{
		// Check
		if (__s == null || __f == null)
			throw new NullPointerException("NARG");
		
		// Separator char depends
		char sep = (__f == MidletSuiteIDFormat.IMC ? ':' : ';');
		
		// {@squirreljme.error AD09 Expected a separator to appear in the
		// specified string. (The input string; The separator)}
		int cola = __s.indexOf(sep);
		if (cola < 0)
			throw new InvalidMidletException(
				String.format("AD09 %s %c", __s, sep));
		
		// {@squirreljme.error AD0a Expected a second separator to appear in
		// the specified string. (The input string; The separator)}
		int colb = __s.indexOf(sep, cola + 1);
		if (colb < 0)
			throw new InvalidMidletException(
				String.format("AD0a %s %c", __s, sep));
		
		// Depending on the format, the fields may be in a separate order
		// Dependencies have the name first
		String a = __s.substring(0, cola),
			b = __s.substring(cola + 1, colb);
		if (__f == MidletSuiteIDFormat.DEPENDENCY)
		{
			this.vendor = new MidletSuiteVendor(b);
			this.name = new MidletSuiteName(a);
		}
		
		// Everything else has the vendor first
		else
		{
			this.vendor = new MidletSuiteVendor(a);
			this.name = new MidletSuiteName(b);
		}
		
		// This is in the same location
		this.version = new MidletVersion(__s.substring(colb + 1));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public int compareTo(MidletSuiteID __o)
	{
		if (this == __o)
			return 0;
		
		// Compare name
		int rv = this.name.compareTo(__o.name);
		if (rv != 0)
			return rv;
		
		// Then vendor
		rv = this.vendor.compareTo(__o.vendor);
		if (rv != 0)
			return rv;
		
		// Then the version last
		return this.version.compareTo(__o.version);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		// Check
		if (!(__o instanceof MidletSuiteID))
			return false;
		
		return 0 == (compareTo((MidletSuiteID)__o));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public int hashCode()
	{
		return this.name.hashCode() ^ this.vendor.hashCode() ^
			this.version.hashCode();
	}
	
	/**
	 * Returns the suite name.
	 *
	 * @return The suite name.
	 * @since 2016/10/12
	 */
	public MidletSuiteName name()
	{
		return this.name;
	}
	
	/**
	 * This returns the suite ID as it is used in dependencies.
	 *
	 * @return A string that can be used as a dependency.
	 * @since 2017/02/22
	 */
	public String toDependencyString()
	{
		// Get
		Reference<String> ref = this._dep;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._dep = new WeakReference<>((rv = this.name + ";" +
				this.vendor + ";" + this.version));
		
		// Return it
		return rv;
	}
	
	/**
	 * This returns the suite ID as an inter-midlet communication string.
	 *
	 * @return A string able to be used in inter-midlet communication.
	 * @since 2016/10/12
	 */
	public String toIMCString()
	{
		// Get
		Reference<String> ref = this._imc;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._imc = new WeakReference<>((rv = this.vendor + ":" +
				this.name + ":" + this.version));
		
		// Return it
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public String toString()
	{
		// Get
		Reference<String> ref = this._jar;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._jar = new WeakReference<>((rv = this.vendor + ";" +
				this.name + ";" + this.version));
		
		// Return it
		return rv;
	}
	
	/**
	 * Returns the string format of the suite ID using the given format.
	 *
	 * @param __f The format to use.
	 * @return The string representation in the given format.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/22
	 */
	public String toString(MidletSuiteIDFormat __f)
		throws NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__f)
		{
				// Jar
			case JAR:
				return toString();
				
				// Dependency
			case DEPENDENCY:
				return toDependencyString();
				
				// Intermidlet communication
			case IMC:
				return toIMCString();
			
				// Should not happen
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * Returns the suite vendor.
	 *
	 * @return The suite vendor.
	 * @since 2016/10/12
	 */
	public MidletSuiteVendor vendor()
	{
		return this.vendor;
	}
	
	/**
	 * Returns the suite version.
	 *
	 * @return The suite version.
	 * @since 2016/10/12
	 */
	public MidletVersion version()
	{
		return this.version;
	}
}

