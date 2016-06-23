// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ci;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MemberTypeSymbol;

/**
 * This is used to identify a member by its name and its type.
 *
 * @param <S> The descriptor type that the member uses.
 * @since 2016/04/22
 */
public abstract class CIMemberID<S extends MemberTypeSymbol>
{
	/** The name of this member. */
	protected final IdentifierSymbol name;
	
	/** The type of this member. */
	protected final S type;
	
	/** The string representation. */
	private volatile Reference<String> _strep;
	
	/**
	 * Initializes the member ID information.
	 *
	 * @param __n The name of this member.
	 * @param __t The type of this member.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/22
	 */
	CIMemberID(IdentifierSymbol __n, S __t)
		throws NullPointerException
	{
		// Check
		if (__n == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Set
		name = __n;
		type = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/22
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Not this?
		if (!(__o instanceof CIMemberID))
			return false;
		
		// Check types
		CIMemberID<?> o = (CIMemberID<?>)__o;
		return name.equals(o.name()) && type.equals(o.type());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/22
	 */
	@Override
	public final int hashCode()
	{
		return name.hashCode() ^ type.hashCode();
	}
	
	/**
	 * Returns the name of this member.
	 *
	 * @return The member name.
	 * @since 2016/04/22
	 */
	public final IdentifierSymbol name()
	{
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/22
	 */
	@Override
	public final String toString()
	{
		// Get reference
		Reference<String> ref = _strep;
		String rv;
		
		// Needs to be cached?
		if (ref == null || null == (rv = ref.get()))
			_strep = new WeakReference<>((rv = (name + ":" + type)));
		
		// Return it
		return rv;
	}
	
	/**
	 * Returns the type of this member.
	 *
	 * @return The member type.
	 * @since 2016/04/22
	 */
	public final S type()
	{
		return type;
	}
}

