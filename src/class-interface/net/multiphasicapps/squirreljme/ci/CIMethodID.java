// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ci;

import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This identifies a method by its name and type.
 *
 * @since 2016/04/22
 */
public class CIMethodID
	extends CIMemberID<MethodSymbol>
{
	/**
	 * Initializes the method identifier.
	 *
	 * @param __n The name of this method.
	 * @param __t The type of this member.
	 * @throws CIException If the method name is not valid.
	 * @since 2016/04/22
	 */
	public CIMethodID(IdentifierSymbol __n, MethodSymbol __t)
		throws CIException
	{
		super(__n, __t);
		
		// {@squirreljme.error AO20 The specified name is not a valid name for
		// a method. (The name of the method)}
		if (!name.isValidMethod())
			throw new CIException(CIException.Issue.INVALID_METHOD_NAME,
				String.format("AO20", __n));
	}
}

