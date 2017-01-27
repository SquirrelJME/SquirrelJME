// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import net.multiphasicapps.squirreljme.executable.ExecutableClass;
import net.multiphasicapps.squirreljme.executable.ExecutableLoadException;

/**
 * This is a class which has been loaded by a process which means it will have
 * statics allocated and be available for usage by executing code.
 *
 * @since 2017/01/16
 */
public interface ContextClass
{
	/**
	 * Return the executable class that this provides a context for.
	 *
	 * @return The excutable class for this context.
	 * @since 2017/01/16
	 */
	public abstract ExecutableClass executable();
	
	/**
	 * Return the binary name of the class.
	 *
	 * @return The class binary name.
	 * @since 2017/01/16
	 */
	public abstract String name();
}

