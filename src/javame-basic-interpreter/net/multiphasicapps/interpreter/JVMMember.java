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
import net.multiphasicapps.descriptors.MemberTypeSymbol;

/**
 * This is the base for all member types.
 *
 * @param <S> The symbol type.
 * @param <C> The base class file member type.
 * @param <F> The flag type.
 * @param <J> The virtual machine type.
 * @since 2016/04/04
 */
public abstract class JVMMember<S extends MemberTypeSymbol, 
	F extends CFMemberFlags, C extends CFMember<S, F>,
	J extends JVMMember<S, F, C, J>>
{
	/** Raw member list owner. */
	protected final JVMMembers<S, F, C, J> rawowner;
	
	/** The class file bit this is based on. */
	protected final C base;
	
	/**
	 * Initializes the base for the member.
	 *
	 * @param __ro The member mapping owning this.
	 * @param __b The type this is based on.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/05
	 */
	JVMMember(JVMMembers<S, F, C, J> __ro, C __b)
		throws NullPointerException
	{
		// Check
		if (__ro == null || __b == null)
			throw new NullPointerException("NARG");
		
		// Set
		rawowner = __ro;
		base = __b;
	}
	
	/**
	 * Returns the member flags.
	 *
	 * @return The member flags.
	 * @since 2016/04/04
	 */
	public F flags()
	{
		return base.flags();
	}
}

