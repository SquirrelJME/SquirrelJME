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

import net.multiphasicapps.classfile.CFField;
import net.multiphasicapps.classfile.CFFieldFlags;
import net.multiphasicapps.descriptors.FieldSymbol;

/**
 * This represents a virtual machine field.
 *
 * @since 2016/04/04
 */
public class JVMField
	extends JVMMember<FieldSymbol, CFFieldFlags, CFField, JVMField>
{
	/**
	 * Initializes the field.
	 *
	 * @param __o The owning group.
	 * @param __b The base for it.
	 * @since 2016/04/05
	 */
	JVMField(JVMFields __o, CFField __b)
	{
		super(__o, __b);
	}
}

