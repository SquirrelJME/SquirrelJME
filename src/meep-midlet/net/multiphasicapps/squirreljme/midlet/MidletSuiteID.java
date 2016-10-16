// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.midlet;

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
	
	/** The string representation. */
	private volatile Reference<String> _string;
	
	/** The string representation used by the IMC Connection. */
	private volatile Reference<String> _imc;
	
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
	 * Decodes a midlet suite ID, a reverse operation to the
	 * {@link #toString()} method.
	 * 
	 * @param __s The string to decode.
	 * @throws IllegalArgumentException If the string is malformed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/13
	 */
	public MidletSuiteID(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		this(__s, false);
	}
	
	/**
	 * Initializes the suite identifier from the specified string.
	 *
	 * @param __s The string to decode.
	 * @param __imc If {@code true} then the identifier is parsed as if it were
	 * part of an IMC address, otherwise it is read as if it were a JAR.
	 * @throws IllegalArgumentException If the string is malformed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/13
	 */
	public MidletSuiteID(String __s, boolean __imc)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Separator char depends
		char sep = (__imc ? ':' : ';');
		
		// {@squirreljme.error AD06 Expected a separator to appear in the
		// specified string. (The input string; The separator)}
		int cola = __s.indexOf(sep);
		if (cola < 0)
			throw new IllegalArgumentException(String.format("AD06 %s %c",
				__s, sep));
		
		// {@squirreljme.error AD07 Expected a second separator to appear in
		// the specified string. (The input string; The separator)}
		int colb = __s.indexOf(sep, cola + 1);
		if (colb < 0)
			throw new IllegalArgumentException(String.format("AD07 %s %c",
				__s, sep));
		
		// Split and parse
		this.vendor = new MidletSuiteVendor(__s.substring(0, cola));
		this.name = new MidletSuiteName(__s.substring(cola + 1, colb));
		this.version = new MidletVersion(__s.substring(colb + 1));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public int compareTo(MidletSuiteID __o)
	{
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
		// Check
		if (!(__o instanceof MidletSuiteID))
			return false;
		
		throw new Error("TODO");
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
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = this.vendor + ";" +
				this.name + ";" + this.version));
		
		// Return it
		return rv;
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

