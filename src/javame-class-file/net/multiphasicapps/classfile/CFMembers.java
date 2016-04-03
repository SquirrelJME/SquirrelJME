// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MemberTypeSymbol;

/**
 * This is the base class for the set of members and such that a class may
 * have.
 *
 * @param <S> The type of symbol the member type uses.
 * @param <F> The flag type the member uses.
 * @param <E> The entry type.
 * @since 2016/03/19
 */
public abstract class CFMembers<S extends MemberTypeSymbol,
	F extends CFMemberFlags, E extends CFMember<S, F>>
	extends AbstractMap<CFMemberKey<S>, E>
{
	/** The owning class. */
	protected final CFClass owner;	
	
	/** The type the sub-class must be. */
	protected final Class<E> cast;
	
	/** Internal storage. */
	private final Map<CFMemberKey<S>, E> _store;
	
	/**
	 * Initializes the member list.
	 *
	 * @param __own The owning class to use.
	 * @param __cl The type the added elements must be.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/20
	 */
	public CFMembers(CFClass __own, Class<E> __cl)
		throws NullPointerException
	{
		// Check
		if (__own == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// Set
		owner = __own;
		cast = __cl;
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public final E get(Object __k)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Obtains a member using the given name and type.
	 *
	 * @param __n The name of the member.
	 * @param __t The type of the member.
	 * @return The member by the given name and type or {@code null} if not
	 * found.
	 * @since 2016/03/20
	 */
	public final E get(IdentifierSymbol __n, S __t)
	{
		return get(new CFMemberKey<>(__n, __t));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public final Set<Map.Entry<CFMemberKey<S>, E>> entrySet()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Adds the given member to the mapping.
	 *
	 * @param __e The member to add to the mapping.
	 * @return The old member this replaced, will be {@code null} if nothing
	 * was replaced.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/20
	 */
	public final E put(E __e)
		throws NullPointerException
	{
		// Check
		if (__e == null)
			throw new NullPointerException("NARG");
		
		// Place it in
		return put(__e.nameAndType(), __e);
	}
}

