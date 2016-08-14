// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;

/**
 * This represents a reference to a field.
 *
 * @since 2016/08/14
 */
public final class JITFieldReference
	extends JITMemberReference
{
	/**
	 * Initializes the field reference.
	 *
	 * @param __cn The class name.
	 * @param __mn The member name.
	 * @param __mt the member type.
	 * @since 2016/08/14
	 */
	JITFieldReference(ClassNameSymbol __cn, IdentifierSymbol __mn,
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
		return super.equals(__o) && (__o instanceof JITFieldReference);
	}
	
	/**
	 * Returns the member type.
	 *
	 * @return The member type.
	 * @since 2016/08/14
	 */
	public FieldSymbol memberType()
	{
		return (FieldSymbol)this.membertype;
	}
}

