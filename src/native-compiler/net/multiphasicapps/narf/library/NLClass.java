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

import net.multiphasicapps.descriptors.BinaryNameSymbol;

/**
 * This represents information about a class such as its access flags and any
 * fields or methods it contains.
 *
 * @since 2016/04/21
 */
public abstract class NLClass
{
	/**
	 * Returns the super class of this class.
	 *
	 * @return The class super name.
	 * @since 2016/04/22
	 */
	public abstract BinaryNameSymbol superClass();
}

