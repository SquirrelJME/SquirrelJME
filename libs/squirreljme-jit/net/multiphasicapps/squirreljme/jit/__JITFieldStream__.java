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

import net.multiphasicapps.squirreljme.classformat.FieldDescriptionStream;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.linkage.FieldFlags;

/**
 * This is for decoding and storing the information for a field in a class.
 *
 * @since 2017/02/07
 */
class __JITFieldStream__
	extends __JITMemberStream__<FieldFlags, FieldSymbol>
	implements FieldDescriptionStream
{
	/** The constant value. */
	volatile Object _constant;
	
	/**
	 * Initializes the field information.
	 *
	 * @param __c The owning class stream.
	 * @param __f The field flags.
	 * @param __name The field name.
	 * @param __type The field type.
	 * @since 2017/02/07
	 */
	__JITFieldStream__(__JITClassStream__ __c, FieldFlags __f,
		IdentifierSymbol __name, FieldSymbol __type)
	{
		super(__c, __f, __name, __type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/07
	 */
	@Override
	public void constantValue(Object __v)
	{
		this._constant = __v;
	}
}

