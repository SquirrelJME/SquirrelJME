// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter;

import net.multiphasicapps.descriptors.FieldSymbol;

/**
 * This represents a field which is defined in a class.
 *
 * @since 2016/03/17
 */
public class JVMField
	extends JVMMember<FieldSymbol>
{
	/**
	 * Initializes the interpreted method.
	 *
	 * @param __owner The class which owns this method.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	JVMField(JVMClass __owner)
		throws NullPointerException
	{
		super(__owner, FieldSymbol.class);
	}
}

