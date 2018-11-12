// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.rms;

import cc.squirreljme.runtime.swm.SuiteName;
import cc.squirreljme.runtime.swm.SuiteVendor;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents the owner of a record store and is used to access record
 * stores.
 *
 * @since 2017/02/27
 */
public final class RecordStoreOwner
{
	/** The name of the suite. */
	protected final SuiteName name;
	
	/** The vendor of the suite. */
	protected final SuiteVendor vendor;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the owner of the record store.
	 *
	 * @param __n The name of the suite.
	 * @param __v The vendor of the suite.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/27
	 */
	public RecordStoreOwner(SuiteName __n, SuiteVendor __v)
		throws NullPointerException
	{
		// Check
		if (__n == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
		this.vendor = __v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/27
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof RecordStoreOwner))
			return false;
		
		// Cast
		RecordStoreOwner o = (RecordStoreOwner)__o;
		return this.name.equals(o.name) &&
			this.vendor.equals(o.vendor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/27
	 */
	@Override
	public int hashCode()
	{
		return this.name.hashCode() ^ this.vendor.hashCode();
	}
	
	/**
	 * Returns the name of the suite.
	 *
	 * @return The suite name.
	 * @since 2017/02/27
	 */
	public SuiteName name()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/27
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = this.name + ";" + this.vendor));
		
		return rv;
	}
	
	/**
	 * Returns the vendor of the suite.
	 *
	 * @return The suite vendor.
	 * @since 2017/02/27
	 */
	public SuiteVendor vendor()
	{
		return this.vendor;
	}
}

