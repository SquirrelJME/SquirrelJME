// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.interpreter;

import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelProcess;
import net.multiphasicapps.squirreljme.kernel.SuiteDataAccessor;

/**
 * This provides the base process representation for the interpreter.
 *
 * @since 2017/01/03
 */
public abstract class AbstractKernelProcess
	extends KernelProcess
{
	/**
	 * Initializes the abstract process.
	 *
	 * @param __k The owning kernel.
	 * @param __cp The class path.
	 * @since 2017/01/03
	 */
	public AbstractKernelProcess(Kernel __k, SuiteDataAccessor[] __cp)
	{
		super(__k, __cp);
	}
}

