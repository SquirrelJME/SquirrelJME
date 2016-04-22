// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.library;

import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This identifies a method by its name and type.
 *
 * @since 2016/04/22
 */
public class NLMethodID
	extends NLMemberID<MethodSymbol>
{
	/**
	 * Initializes the method identifier.
	 *
	 * @param __n The name of this method.
	 * @param __t The type of this member.
	 * @since 2016/04/22
	 */
	public NLMethodID(IdentifierSymbol __n, MethodSymbol __t)
	{
		super(__n, __t);
	}
}

