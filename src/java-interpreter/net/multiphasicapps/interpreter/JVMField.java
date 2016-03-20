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

import java.util.Set;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;

/**
 * This represents a field which is defined in a class.
 *
 * @since 2016/03/17
 */
public class JVMField
	extends JVMMember<FieldSymbol, JVMFieldFlags>
{
	/**
	 * Initializes the interpreted method.
	 *
	 * @param __owner The class which owns this method.
	 * @param __name The name of the field.
	 * @param __type The type of the field.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	public JVMField(JVMClass __owner, IdentifierSymbol __name,
		FieldSymbol __type)
		throws NullPointerException
	{
		super(__owner, FieldSymbol.class, __name, __type, JVMFieldFlags.class);
	}
}

