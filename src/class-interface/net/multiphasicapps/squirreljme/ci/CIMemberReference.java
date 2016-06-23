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
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MemberTypeSymbol;

/**
 * This references a member in the current or another class.
 *
 * @param <T> The symbol type for the member type.
 * @since 2016/04/24
 */
public abstract class CIMemberReference<T extends MemberTypeSymbol>
	implements CIPoolEntry
{
	/** The referenced class. */
	protected final ClassNameSymbol inclass;
	
	/** The member name. */
	protected final IdentifierSymbol name;
	
	/** The member type. */
	protected final T type;
	
	/** The string representation of this. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the member reference.
	 *
	 * @param __cl The containing class.
	 * @param __n The name of the member.
	 * @param __t The type of the member.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/24
	 */
	CIMemberReference(ClassNameSymbol __cl, IdentifierSymbol __n, T __t)
		throws NullPointerException
	{
		// Check
		if (__cl == null || __n == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Set
		inclass = __cl;
		name = __n;
		type = __t;
	}
	
	/**
	 * Returns the class the member is in.
	 *
	 * @return The member's class.
	 * @since 2016/04/24
	 */
	public final ClassNameSymbol memberClass()
	{
		return inclass;
	}
	
	/**
	 * Returns the name of the member.
	 *
	 * @return The member's name.
	 * @since 2016/04/24
	 */
	public final IdentifierSymbol memberName()
	{
		return name;
	}
	
	/**
	 * Returns the type of the member.
	 *
	 * @return The member's type.
	 * @since 2016/04/24
	 */
	public final T memberType()
	{
		return type;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public final String toString()
	{
		// Get reference
		Reference<String> ref = _string;
		String rv;
		
		// Needs to be cached?
		if (ref == null || null == (rv = ref.get()))
			_string = new WeakReference<>((rv = inclass + "::" + name + ":" +
				type));
		
		// Return it
		return rv;
	}
}

