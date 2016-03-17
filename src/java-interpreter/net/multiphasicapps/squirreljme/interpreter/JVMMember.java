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
	
	/**
	 * Initializes the interpreted member.
	 *
	 * @param __owner The class which owns this.
	 * @param __st The descriptor symbol type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/17
	 */
	JVMMember(JVMClass __owner, Class<S> __st)
		throws NullPointerException
	{
		// Check
		if (__owner == null || __st == null)
			throw new NullPointerException();
		
		// Set
		inclass = __owner;
		symboltype = __st;
	}
}

