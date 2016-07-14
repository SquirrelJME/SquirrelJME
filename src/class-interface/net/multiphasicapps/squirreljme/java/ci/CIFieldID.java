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

import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;

/**
 * This identifies a field by its name and type.
 *
 * @since 2016/04/22
 */
public final class CIFieldID
	extends CIMemberID<FieldSymbol>
{
	/**
	 * Initializes the field identifer.
	 *
	 * @param __n The name of this field.
	 * @param __t The type that the field is.
	 * @since 2016/04/22
	 */
	public CIFieldID(IdentifierSymbol __n, FieldSymbol __t)
	{
		super(__n, __t);
	}
}

