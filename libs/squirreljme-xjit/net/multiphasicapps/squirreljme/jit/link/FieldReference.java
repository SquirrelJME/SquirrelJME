// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.link;

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;

/**
 * This represents a reference to a field.
 *
 * @since 2016/08/14
 */
public final class FieldReference
	extends MemberReference
{
	/**
	 * Initializes the field reference.
	 *
	 * @param __cn The class name.
	 * @param __mn The member name.
	 * @param __mt the member type.
	 * @since 2016/08/14
	 */
	public FieldReference(ClassNameSymbol __cn, IdentifierSymbol __mn,
		FieldSymbol __mt)
	{
		super(__cn, __mn, __mt);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/14
	 */
	@Override
	public boolean equals(Object __o)
	{
		return super.equals(__o) && (__o instanceof FieldReference);
	}
	
	/**
	 * Returns the type of field that this is.
	 *
	 * @return The field type.
	 * @since 2016/09/06
	 */
	public FieldSymbol fieldType()
	{
		return (FieldSymbol)this.membertype;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/14
	 */
	@Override
	public FieldSymbol memberType()
	{
		return (FieldSymbol)this.membertype;
	}
}

