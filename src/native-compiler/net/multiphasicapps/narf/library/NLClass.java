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

import java.util.Set;
import net.multiphasicapps.descriptors.ClassNameSymbol;

/**
 * This represents information about a class such as its access flags and any
 * fields or methods it contains.
 *
 * @since 2016/04/21
 */
public abstract class NLClass
{
	/**
	 * Returns the name of the implemented interfaces.
	 *
	 * @return The set of implemented interfaces.
	 * @since 2016/04/22
	 */
	public abstract Set<ClassNameSymbol> interfaceNames();
	
	/**
	 * Returns the name of the super class.
	 *
	 * @return The class super name.
	 * @since 2016/04/22
	 */
	public abstract ClassNameSymbol superName();
	
	/**
	 * Returns the name of this class.
	 *
	 * @return The name of this class.
	 * @since 2016/04/22
	 */
	public abstract ClassNameSymbol thisName();
}

