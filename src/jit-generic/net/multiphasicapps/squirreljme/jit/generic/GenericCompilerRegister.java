// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import net.multiphasicapps.squirreljme.jit.JITVariableType;

/**
 * This interface is used to describe generic compiler registers to be used
 * for data storage.
 *
 * @since 2016/08/30
 */
public interface GenericCompilerRegister
{
	/**
	 * Returns the type of values which may be stored in this register.
	 *
	 * @return An array of variable types that can be stored in a given
	 * register.
	 * @since 2016/08/30
	 */
	public abstract JITVariableType[] types();
}

