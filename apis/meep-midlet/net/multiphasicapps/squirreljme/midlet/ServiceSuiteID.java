// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.midlet;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * This represents a suite ID which is used for system services.
 *
 * Vendors and versions are optional.
 *
 * @since 2017/02/22
 */
public final class ServiceSuiteID
	implements Comparable<ServiceSuiteID>
{
	/** The service name. */
	protected final MidletSuiteName name;
	
	/** The service vendor. */
	protected final MidletSuiteVendor vendor;
	
	/** The service version. */
	protected final MidletVersion version;
	
	/** The string representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the service suite.
	 *
	 * @param __n The name of the service.
	 * @throws NullPointerException If no name was specified.
	 * @since 2017/02/22
	 */
	public ServiceSuiteID(MidletSuiteName __n)
		throws NullPointerException
	{
		this(__n, null, null);
	}
	
	/**
	 * Initializes the service suite.
	 *
	 * @param __n The name of the service.
	 * @param __e The vendor of the service, this is optional.
	 * @throws NullPointerException If no name was specified.
	 * @since 2017/02/22
	 */
	public ServiceSuiteID(MidletSuiteName __n, MidletSuiteVendor __e)
		throws NullPointerException
	{
		this(__n, __e, null);
	}
	
	/**
	 * Initializes the service suite.
	 *
	 * @param __n The name of the service.
	 * @param __e The vendor of the service, this is optional.
	 * @param __v The version of the service, this is optional.
	 * @throws NullPointerException If no name was specified.
	 * @since 2017/02/22
	 */
	public ServiceSuiteID(MidletSuiteName __n, MidletSuiteVendor __e,
		MidletVersion __v)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
		this.vendor = __e;
		this.version = __v;
	}
	
	/**
	 * Parses a service identifier from the given string.
	 *
	 * @param __s The string to parse the identifier for.
	 * @throws IllegalArgumentException If the format is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/22
	 */
	public ServiceSuiteID(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AD0k Expected a separator to appear in the
		// specified string. (The input string; The separator)}
		int cola = __s.indexOf(';');
		if (cola < 0)
			throw new IllegalArgumentException(String.format("AD0k %s %c",
				__s, ';'));
		
		// {@squirreljme.error AD0l Expected a second separator to appear in
		// the specified string. (The input string; The separator)}
		int colb = __s.indexOf(';', cola + 1);
		if (colb < 0)
			throw new IllegalArgumentException(String.format("AD0l %s %c",
				__s, ';'));
		
		// Some fields are optional, so if they are blank, ignore them
		String inven = __s.substring(cola + 1, colb);
		String inver = __s.substring(colb + 1);
		
		// Split and parse
		this.name = new MidletSuiteName(__s.substring(0, cola));
		this.vendor = (inven.length() <= 0 ? null :
			new MidletSuiteVendor(inven));
		this.version = (inver.length() <= 0 ? null :
			new MidletVersion(inver));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/22
	 */
	@Override
	public int compareTo(ServiceSuiteID __o)
	{
		// Compare name
		int rv = this.name.compareTo(__o.name);
		if (rv != 0)
			return rv;
		
		// Then vendor
		MidletSuiteVendor ae = this.vendor,
			be = __o.vendor;
		if ((ae == null) != (be == null))
			return (ae == null ? -1 : 1);
		else if (ae == null)
			return 0;
		else if ((rv = ae.compareTo(be)) != 0)
			return rv;
		
		// Then the version last
		MidletVersion av = this.version,
			bv = __o.version;
		if ((av == null) != (bv == null))
			return (av == null ? -1 : 1);
		else if (av == null)
			return 0;
		return av.compareTo(bv);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/22
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof ServiceSuiteID))
			return false;
		
		return 0 == (compareTo((ServiceSuiteID)__o));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/22
	 */
	@Override
	public int hashCode()
	{
		return this.name.hashCode() ^ Objects.hashCode(this.vendor) ^
			Objects.hashCode(this.version);
	}
	
	/**
	 * Returns the suite name.
	 *
	 * @return The suite name.
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
			this._string = new WeakReference<>((rv = this.vendor + ";" +
				Objects.toString(this.name, "") + ";" +
				Objects.toString(this.version, "")));
		}
		
		// Return it
		return rv;
	}
	
	/**
	 * Returns the suite vendor.
	 *
	 * @return The suite vendor or {@code null} if not specified.
	 * @since 2017/02/22
	 */
	public MidletSuiteVendor vendor()
	{
		return this.vendor;
	}
	
	/**
	 * Returns the suite version.
	 *
	 * @return The suite version or {@code null} if not specified.
	 * @since 2017/02/22
	 */
	public MidletVersion version()
	{
		return this.version;
	}
}

