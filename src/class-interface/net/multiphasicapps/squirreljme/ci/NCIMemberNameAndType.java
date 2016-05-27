// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classinterface;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MemberTypeSymbol;

/**
 * This contains the name and type of a member.
 *
 * @since 2016/04/24
 */
public final class NCIMemberNameAndType
	implements NCIPoolEntry
{
	/** The name of the member. */
	protected final IdentifierSymbol name;
	
	/** The type of the symbol. */
	protected final MemberTypeSymbol type;
	
	/** The string representation of this. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the member name and type information.
	 *
	 * @param __n The name of the member.
	 * @param __t The type of the member.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/24
	 */
	public NCIMemberNameAndType(IdentifierSymbol __n, MemberTypeSymbol __t)
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
	 * Returns the name of the referenced member.
	 *
	 * @return The name of the member.
	 * @since 2016/04/24
	 */
	public IdentifierSymbol name()
	{
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public NCIPoolTag tag()
	{
		return NCIPoolTag.NAMEANDTYPE;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public String toString()
	{
		// Get reference
		Reference<String> ref = _string;
		String rv;
		
		// Needs to be cached?
		if (ref == null || null == (rv = ref.get()))
			_string = new WeakReference<>((rv = name + ":" + type));
		
		// Return it
		return rv;
	}
	
	/**
	 * Returns the type of the referenced member.
	 *
	 * @return The type of the member.
	 * @since 2016/04/24
	 */
	public MemberTypeSymbol type()
	{
		return type;
	}
}

