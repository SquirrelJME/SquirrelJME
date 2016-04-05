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

import net.multiphasicapps.classfile.CFMemberKey;
import net.multiphasicapps.classfile.CFMethod;
import net.multiphasicapps.classfile.CFMethodFlags;
import net.multiphasicapps.classfile.CFMethods;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This represents the collection of methods which are available to a class..
 *
 * @since 2016/04/04
 */
public class JVMMethods
	extends JVMMembers<MethodSymbol, CFMethodFlags, CFMethod, JVMMethod>
{
	/**
	 * Initializes the class method mappings.
	 *
	 * @param __cl The class containing the methods.
	 * @since 2016/04/04
	 */
	JVMMethods(JVMClass __cl)
	{
		super(__cl, __cl.base().methods());
	}
}

