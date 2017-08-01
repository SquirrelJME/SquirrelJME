// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.link;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents a link to the given method link's class table.
 *
 * The table contains linkages to classes, fields, and methods.
 *
 * @since 2017/03/22
 */
public final class MethodTableLinkage
	implements Linkage
{
	/** The method this links to. */
	protected final MethodLinkage link;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the table linkage.
	 *
	 * @param __l The method link.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/22
	 */
	public MethodTableLinkage(MethodLinkage __l)
		throws NullPointerException
	{
		// Check
		if (__l == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.link = __l;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/22
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof MethodTableLinkage))
			return false;
		
		return this.link.equals(((MethodTableLinkage)__o).link);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/22
	 */
	@Override
	public int hashCode()
	{
		return this.link.hashCode();
	}
	
	/**
	 * Returns the method that this table is for.
	 *
	 * @return The method that the table is for.
	 * @since 2017/03/22
	 */
	public MethodLinkage methodLink()
	{
		return this.link;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/22
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = "T[" + this.link + "]"));
		
		return rv;
	}
}

