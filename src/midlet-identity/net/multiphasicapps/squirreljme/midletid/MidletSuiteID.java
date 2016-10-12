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
	
	/**
	 * Initializes the suite identifier.
	 *
	 * @param __name The name of the suite.
	 * @param __vendor The vendor of the suite.
	 * @param __ver The version of the suite.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/12
	 */
	public MidletUID(MidletSuiteName __name, MidletSuiteVendor __ven,
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
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public int hashCode()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
}

