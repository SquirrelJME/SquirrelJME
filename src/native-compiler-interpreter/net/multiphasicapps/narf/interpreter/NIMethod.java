// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.interpreter;

import net.multiphasicapps.descriptors.MethodSymbol;
import net.multiphasicapps.narf.classinterface.NCIMethod;

/**
 * This represents a method which exists within a class.
 *
 * @since 2016/04/22
 */
public class NIMethod
	extends NIMember<NCIMethod>
{
	/**
	 * Initializes the method.
	 *
	 * @param __oc The owning class.
	 * @param __m The base method.
	 * @since 2016/04/22
	 */
	public NIMethod(NIClass __oc, NCIMethod __m)
	{
		super(__oc, __m);
	}
}

