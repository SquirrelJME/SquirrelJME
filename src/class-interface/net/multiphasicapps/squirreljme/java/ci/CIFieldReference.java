// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.ci;

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;

/**
 * This refers to another field.
 *
 * @since 2016/04/24
 */
public final class CIFieldReference
	extends CIMemberReference<FieldSymbol>
{
	/**
	 * Initializes the field reference.
	 *
	 * @param __cl The class it is in.
	 * @param __name The name of the member.
	 * @param __type The type of the member.
	 * @since 2016/04/24
	 */
	public CIFieldReference(ClassNameSymbol __cl, IdentifierSymbol __name,
		FieldSymbol __type)
	{
		super(__cl, __name, __type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public CIPoolTag tag()
	{
		return CIPoolTag.FIELDREF;
	}
}

