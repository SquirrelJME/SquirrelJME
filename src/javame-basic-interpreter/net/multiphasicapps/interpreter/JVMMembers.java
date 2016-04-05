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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.classfile.CFMember;
import net.multiphasicapps.classfile.CFMemberFlags;
import net.multiphasicapps.classfile.CFMembers;
import net.multiphasicapps.classfile.CFMemberKey;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MemberTypeSymbol;

/**
 * This represents a group of members of a specific type which are available to
 * this class.
 *
 * @param <S> The symbol type used for the member.
 * @param <F> The type of flags the member uses.
 * @param <C> The class file member type.
 * @param <J> The JVM member type.
 * @since 2016/04/04
 */
public abstract class JVMMembers<S extends MemberTypeSymbol,
	F extends CFMemberFlags, C extends CFMember<S, F>,
	J extends JVMMember<S, F, C, J>>
	extends AbstractMap<CFMemberKey<S>, J>
{
	/** The owning class. */
	protected final JVMClass outerclass;
	
	/** The source mappings. */
	protected final CFMembers<S, F, C> basemembers;
	
	/** Wrapped members. */
	protected final Map<CFMemberKey<S>, J> wrapped;
	
	/**
	 * Initializes the member mappings.
	 *
	 * @param __cl The class which contains the set of members.
	 * @param __bm The mapping of members.
	 * @throws NullPointerException On null arguments, except for {@code __bm}.
	 * @since 2016/04/04
	 */
	JVMMembers(JVMClass __cl, CFMembers<S, F, C> __bm)
		throws NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Set
		outerclass = __cl;
		basemembers = __bm;
		
		// Setup wrapper
		Map<CFMemberKey<S>, J> wx = new LinkedHashMap<>();
		if (__bm != null)
			for (Map.Entry<CFMemberKey<S>, C> e : __bm.entrySet())
				wx.put(e.getKey(), wrapInternally(e.getValue()));
		
		// Lock in
		wrapped = MissingCollections.<CFMemberKey<S>, J>unmodifiableMap(wx);
	}
	
	/**
	 * Internally wraps the given entry.
	 *
	 * @param __cf The entry to be wrapped.
	 * @return The wrapped entry.
	 * @since 2016/04/05
	 */
	protected abstract J wrapInternally(C __cf);
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/05
	 */
	@Override
	public final Set<Map.Entry<CFMemberKey<S>, J>> entrySet()
	{
		return wrapped.entrySet();
	}
	
	/**
	 * Obtains a member by the given name and type.
	 *
	 * @param __id The name of the member.
	 * @param __ty The type of the member.
	 * @since 2016/04/04
	 */
	public final J get(IdentifierSymbol __id, S __ty)
	{
		return get(new CFMemberKey<S>(__id, __ty));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/05
	 */
	@Override
	public int size()
	{
		return wrapped.size();
	}
}

