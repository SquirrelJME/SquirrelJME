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

import net.multiphasicapps.squirreljme.kernel.KernelProcess;
import net.multiphasicapps.squirreljme.kernel.KernelThread;

/**
 * This is the base class for threads which are implemented in the kernel.
 *
 * @since 2017/01/16
 */
public abstract class InterpreterKernelThread
	extends KernelThread
{
	/**
	 * Initializes the basic kernel thread.
	 *
	 * @param __kp The kernel process.
	 * @param __mc The main class.
	 * @param __m The main method.
	 * @param __args Method arguments.
	 * @since 2017/01/16
	 */
	public InterpreterKernelThread(KernelProcess __kp, String __mc,
		String __m, Object... __args)
	{
		super(__kp, __mc, __m, __args);
	}
}

