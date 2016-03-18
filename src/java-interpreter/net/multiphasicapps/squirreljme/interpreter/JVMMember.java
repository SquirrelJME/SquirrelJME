// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter;

import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MemberTypeSymbol;

/**
 * The base class represents members which exist in classes.
 *
 * @param <S> The type of symbol used for the descriptor.
 * @since 2016/03/17
 */
public abstract class JVMMember<S extends MemberTypeSymbol>
{
	/** The class this member is in. */
	protected final JVMClass inclass;
	
	/** The type of symbol to use as the descriptor. */
	protected final Class<S> symboltype;
	
	/** The member name. */
	protected final IdentifierSymbol name;
	
	/** The type of the member. */
	protected final S type;
	
	/**
	 * Initializes the interpreted member.
	 *
	 * @param __owner The class which owns this.
	 * @param __st The descriptor symbol type.
	 * @param __name The name of the member.
	 * @param __type The type of the member.
	 * @throws ClassCastException If the {@code __type} is not a sub-class of
	 * {@code __st}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/17
	 */
	JVMMember(JVMClass __owner, Class<S> __st, IdentifierSymbol __name,
		S __type)
		throws ClassCastException, NullPointerException
	{
		// Check
		if (__owner == null || __st == null || __name == null ||
			__type == null)
			throw new NullPointerException("NARG");
		
		// Set
		inclass = __owner;
		symboltype = __st;
		name = __name;
		type = symboltype.cast(__type);
	}
}

