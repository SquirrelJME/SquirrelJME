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

import net.multiphasicapps.squirreljme.executable.ExecutableClass;
import net.multiphasicapps.squirreljme.kernel.ContextClass;
import net.multiphasicapps.squirreljme.kernel.ContextLoadException;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.SuiteDataAccessor;

/**
 * This represents a normal kernel process which has no deterministic factors.
 *
 * @since 2017/01/03
 */
public class NormalKernelProcess
	extends AbstractKernelProcess
{
	/**
	 * Initializes the normal process.
	 *
	 * @param __k The owning kernel.
	 * @param __cp Class path.
	 * @since 2017/01/03
	 */
	public NormalKernelProcess(Kernel __k, SuiteDataAccessor[] __cp)
	{
		super(__k, __cp);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/16
	 */
	@Override
	protected ContextClass initializeClassContext(ExecutableClass __e)
		throws ContextLoadException, NullPointerException
	{
		// Check
		if (__e == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

