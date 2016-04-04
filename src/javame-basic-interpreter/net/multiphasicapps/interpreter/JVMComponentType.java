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

import net.multiphasicapps.descriptors.ClassNameSymbol;

/**
 * This interface is used to describe classes and primitive which may be used
 * as the component in an array.
 *
 * @since 2016/03/31
 */
public interface JVMComponentType
{
	/**
	 * Returns the name of the current class.
	 *
	 * @return The current class name.
	 * @since 2016/03/19
	 */
	public abstract ClassNameSymbol thisName();
}

