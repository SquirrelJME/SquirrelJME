// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.jit.link.FieldSymbol;
import net.multiphasicapps.squirreljme.jit.link.IdentifierSymbol;
import net.multiphasicapps.squirreljme.jit.link.MemberTypeSymbol;
import net.multiphasicapps.squirreljme.jit.link.MethodSymbol;

/**
 * This represents name and type information.
 *
 * @since 2016/08/14
 */
public final class NameAndType
{
	/** The member name. */
	protected final IdentifierSymbol name;
	
	/** The member type. */
	protected final MemberTypeSymbol type;
	
	/** The string representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the name and type information.
	 *
	 * @param __n The member name.
	 * @param __t The member type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/14
	 */
	public NameAndType(IdentifierSymbol __n, MemberTypeSymbol __t)
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
	 * @since 2016/08/14
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Must be this class
		if (!(__o instanceof NameAndType))
			return false;
		
		// Check
		NameAndType o = (NameAndType)__o;
		return this.name.equals(o.name) &&
			this.type.equals(o.type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/14
	 */
	@Override
	public final int hashCode()
	{
		return this.name.hashCode() ^
			this.type.hashCode();
	}
	
	/**
	 * Returns the name.
	 *
	 * @return The name.
	 * @since 2016/08/14
	 */
	public final IdentifierSymbol name()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/14
	 */
	@Override
	public final String toString()
	{
		// Get
		Reference<String> ref = this._string;
		String rv;
		
		// Create?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv =
				String.format("{name=%s, type=%s}",this.name, this.type)));
		
		// Return
		return rv;
	}
	
	/**
	 * Returns the type.
	 *
	 * @return The type.
	 * @since 2016/08/14
	 */
	public final MemberTypeSymbol type()
	{
		return this.type;
	}
}

