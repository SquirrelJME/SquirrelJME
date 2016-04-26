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

import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This identifies a method by its name and type.
 *
 * @since 2016/04/22
 */
public class NCIMethodID
	extends NCIMemberID<MethodSymbol>
{
	/**
	 * Initializes the method identifier.
	 *
	 * @param __n The name of this method.
	 * @param __t The type of this member.
	 * @throws NCIException If the method name is not valid.
	 * @since 2016/04/22
	 */
	public NCIMethodID(IdentifierSymbol __n, MethodSymbol __t)
		throws NCIException
	{
		super(__n, __t);
		
		// {@squirreljme.error NC20 The specified name is not a valid name for
		// a method. (The name of the method)}
		if (!name.isValidMethod())
			throw new NCIException(NCIException.Issue.INVALID_METHOD_NAME,
				String.format("NC20", __n));
	}
}

