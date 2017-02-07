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

import net.multiphasicapps.squirreljme.classformat.MemberDescriptionStream;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MemberTypeSymbol;
import net.multiphasicapps.squirreljme.linkage.MemberFlags;

/**
 * This is the base class for field and method description streams.
 *
 * @param <F> The flag type.
 * @param <S> The symbol type.
 * @since 2017/02/07
 */
abstract class __JITMemberStream__<F extends MemberFlags,
	S extends MemberTypeSymbol>
	implements MemberDescriptionStream
{
	/** The owning class stream. */
	final __JITClassStream__ _classstream;
	
	/** Member flags. */
	final F _flags;
	
	/** Member name. */
	final IdentifierSymbol _name;
	
	/** Member type. */
	final S _type;
	
	/**
	 * Initializes the base memeber information.
	 *
	 * @param __c The owning class stream.
	 * @param __f The member flags.
	 * @param __name The member name.
	 * @param __type The member type.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/07
	 */
	__JITMemberStream__(__JITClassStream__ __c, F __f, IdentifierSymbol __name,
		S __type)
		throws NullPointerException
	{
		// Check
		if (__c == null || __f == null || __name == null || __type == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._classstream = __c;
		this._flags = __f;
		this._name = __name;
		this._type = __type;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/07
	 */
	@Override
	public void endMember()
	{
		// Nothing needs to be done
	}
}

