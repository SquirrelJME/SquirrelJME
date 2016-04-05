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

import net.multiphasicapps.classfile.CFMember;
import net.multiphasicapps.classfile.CFMemberFlags;
import net.multiphasicapps.classfile.CFMembers;
import net.multiphasicapps.classfile.CFMemberKey;
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
	F extends CFMemberFlags, C extends CFMember<S, F>, J extends JVMMember>
{
	/** The owning class. */
	protected final JVMClass outerclass;
	
	/** The source mappings. */
	protected final CFMembers<S, F, C> basemembers;
	
	/**
	 * Initializes the member mappings.
	 *
	 * @param __cl The class which contains the set of members.
	 * @param __bm The mapping of members.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/04
	 */
	JVMMembers(JVMClass __cl, CFMembers<S, F, C> __bm)
		throws NullPointerException
	{
		// Check
		if (__cl == null || __bm == null)
			throw new NullPointerException("NARG");
		
		// Set
		outerclass = __cl;
		basemembers = __bm;
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
		throw new Error("TODO");
	}
}

