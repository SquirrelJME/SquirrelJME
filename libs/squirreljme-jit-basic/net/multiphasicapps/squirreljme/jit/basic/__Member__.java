// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

import net.multiphasicapps.squirreljme.classformat.MemberFlags;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MemberTypeSymbol;

/**
 * This contains the base storage for member types since they share
 * common details.
 *
 * @since 2016/09/14
 */
@Deprecated
abstract class __Member__<F extends MemberFlags, S extends MemberTypeSymbol>
{
	/** The flags used. */
	final F _flags;
	
	/** The member name. */
	final BasicConstantEntry<IdentifierSymbol> _name;
	
	/** The member type. */
	final BasicConstantEntry<S> _type;
	
	/**
	 * Initializes the member information.
	 *
	 * @param __f The member flags.
	 * @param __n The member name.
	 * @param __t The member type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/14
	 */
	__Member__(F __f, BasicConstantEntry<IdentifierSymbol> __n,
		BasicConstantEntry<S> __t)
		throws NullPointerException
	{
		// Check
		if (__f == null || __n == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._flags = __f;
		this._name = __n;
		this._type = __t;
	}
}

