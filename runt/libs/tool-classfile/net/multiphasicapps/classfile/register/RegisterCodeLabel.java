// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Label which refers to a location in code.
 *
 * @since 2019/03/16
 */
public final class RegisterCodeLabel
{
	/** The locality. */
	public final String locality;
	
	/** The associated address. */
	public final int address;
	
	/** String form. */
	private Reference<String> _string;
	
	/**
	 * Initializes the lable.
	 *
	 * @param __l The locality.
	 * @param __a The address.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/16
	 */
	public RegisterCodeLabel(String __l, int __a)
		throws NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		this.locality = __l;
		this.address = __a;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/22
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof RegisterCodeLabel))
			return false;
		
		RegisterCodeLabel o = (RegisterCodeLabel)__o;
		return this.locality.equals(o.locality) &&
			this.address == o.address;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/22
	 */
	@Override
	public final int hashCode()
	{
		return (~this.locality.hashCode()) - this.address;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/22
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = "@" + this.locality +
				":" + this.address));
		
		return rv;
	}
}

