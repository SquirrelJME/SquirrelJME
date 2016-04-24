// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classinterface;

import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;

/**
 * This refers to another field.
 *
 * @since 2016/04/24
 */
public final class NCIFieldReference
	extends NCIMemberReference<FieldSymbol>
{
	/**
	 * Initializes the field reference.
	 *
	 * @param __cl The class it is in.
	 * @param __name The name of the member.
	 * @param __type The type of the member.
	 * @since 2016/04/24
	 */
	public NCIFieldReference(ClassNameSymbol __cl, IdentifierSymbol __name,
		FieldSymbol __type)
	{
		super(__cl, __name, __type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public NCIPoolTag tag()
	{
		return NCIPoolTag.FIELDREF;
	}
}

