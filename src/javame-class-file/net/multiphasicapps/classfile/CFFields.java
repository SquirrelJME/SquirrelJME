// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;

/**
 * This represents fields which exist in a class.
 *
 * @since 2016/03/19
 */
public class CFFields
	extends CFMembers<FieldSymbol, CFFieldFlags, CFField>
{
	/**
	 * Initializes the field mappings.
	 *
	 * @param __own The owning class.
	 * @since 2016/03/20
	 */
	public CFFields(JVMClass __own)
	{
		super(__own, CFField.class);
	}
}

