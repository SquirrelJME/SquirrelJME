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
	 * @since 2016/01/03
	 */
	public NormalKernelProcess(Kernel __k)
	{
		super(__k);
	}
}

