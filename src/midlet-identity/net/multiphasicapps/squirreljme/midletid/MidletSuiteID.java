// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.midletid;

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
	
	/**
	 * Initializes the suite identifier.
	 *
	 * @param __name The name of the suite.
	 * @param __vendor The vendor of the suite.
	 * @param __ver The version of the suite.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/12
	 */
	public MidletSuiteID(MidletSuiteName __name, MidletSuiteVendor __ven,
		MidletVersion __ver)
		throws NullPointerException
	{
		// Check
		if (__name == null || __ven == null || __ver == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __name;
		this.vendor = __ven;
		this.version = __ver;
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
			this._string = new WeakReference<>((rv = this.name + ":" +
				this.vendor + ":" + this.version));
		
		// Return it
		return rv;
	}
}

