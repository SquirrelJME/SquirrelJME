// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classfile;

import net.multiphasicapps.narf.classinterface.NCIMethod;
import net.multiphasicapps.narf.classinterface.NCIMethodFlags;
import net.multiphasicapps.narf.classinterface.NCIMethodID;

/**
 * This represents a method which is contained within a class file.
 *
 * @since 2016/04/26
 */
public final class NCFMethod
	extends NCFMember<NCIMethodID, NCIMethodFlags>
	implements NCIMethod
{
	/**
	 * Initializes the class method.
	 *
	 * @param __oc The outer class.
	 * @param __id The identifier of the method.
	 * @param __fl The method flags.
	 * @param __ca The code attribute of this method.
	 * @since 2016/04/26
	 */
	NCFMethod(NCFClass __oc, NCIMethodID __id, NCIMethodFlags __fl,
		byte[] __ca)
	{
		super(__oc, __id, __fl);
		
		throw new Error("TODO");
	}
}

