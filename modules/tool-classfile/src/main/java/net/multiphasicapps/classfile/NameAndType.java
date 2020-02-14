// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This holds the name and type strings, the type descriptor is not checked.
 *
 * @since 2017/06/12
 */
public final class NameAndType
{
	/** The name. */
	protected final String name;
	
	/** The type. */
	protected final String type;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the name and type information.
	 *
	 * @param __n The name.
	 * @param __t The type.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/12
	 */
	public NameAndType(String __n, String __t)
		throws NullPointerException
	{
		// Check
		if (__n == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
		this.type = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof NameAndType))
			return false;
		
		NameAndType o = (NameAndType)__o;
		return this.name.equals(o.name) &&
			this.type.equals(o.type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public int hashCode()
	{
		return this.name.hashCode() ^ this.type.hashCode();
	}
	
	/**
	 * Returns the identifier.
	 *
	 * @return The identifier.
	 * @since 2017/06/12
	 */
	public String name()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = this.name + "." +
				this.type));
		
		return rv;
	}
	
	/**
	 * Returns the type.
	 *
	 * @return The type.
	 * @since 2017/06/12
	 */
	public String type()
	{
		return this.type;
	}
}

