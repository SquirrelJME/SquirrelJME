// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MemberTypeSymbol;

/**
 * This is the base class for the set of .
 *
 * @param <S> The type of symbol the member type uses.
 * @param <F> The flag type the member uses.
 * @param <E> The entry type.
 * @since 2016/03/19
 */
public abstract class JVMMembers<S extends MemberTypeSymbol,
	F extends JVMMemberFlags, E extends JVMMember<S, F>>
	extends AbstractMap<JVMMemberKey<S>, E>
{
	/** Internal lock. */
	final Object lock;
	
	/** The owning class. */
	protected final JVMClass owner;	
	
	/** The type the sub-class must be. */
	protected final Class<? extends JVMMember> cast;
	
	/**
	 * Initializes the member list.
	 *
	 * @param __own The owning class to use.
	 * @param __cl The type the added elements must be.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/20
	 */
	public JVMMembers(JVMClass __own, Class<? extends JVMMember> __cl)
		throws NullPointerException
	{
		// Check
		if (__own == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// Set
		owner = __own;
		lock = owner.lock;
		cast = __cl;
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
		return get(new JVMMemberKey<>(__n, __t));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public final Set<Map.Entry<JVMMemberKey<S>, E>> entrySet()
	{
		throw new Error("TODO");
	}
}

