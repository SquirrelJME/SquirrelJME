// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.classformat.CodeDescriptionStream;
import net.multiphasicapps.squirreljme.classformat.MethodDescriptionStream;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.linkage.MethodFlags;

/**
 * This is used to describe the method along with the byte code that will need
 * to be decoded during parsing.
 *
 * @since 2017/02/07
 */
class __JITMethodStream__
	extends __JITMemberStream__<MethodFlags, MethodSymbol>
	implements MethodDescriptionStream
{
	/**
	 * Initializes the method information.
	 *
	 * @param __f The method flags.
	 * @param __name The method name.
	 * @param __type The method type.
	 * @since 2017/02/07
	 */
	__JITMethodStream__(MethodFlags __f, IdentifierSymbol __name,
		MethodSymbol __type)
	{
		super(__f, __name, __type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/07
	 */
	@Override
	public CodeDescriptionStream code()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/07
	 */
	@Override
	public void noCode()
	{
		throw new Error("TODO");
	}
}

