// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

/**
 * DESCRIBE THIS.
 *
 * @since 2016/05/21
 */
public abstract class KernelTrace
{
	/**
	 * Initializes the base tracer.
	 *
	 * @since 2016/05/21
	 */
	KernelTrace()
	{
	}
	
	/**
	 * Trace that a new process with the given ID was created.
	 *
	 * @param __kp The process which was created.
	 * @since 2016/05/21
	 */
	public abstract void createdProcess(KernelProcess __kp);
	
	/**
	 * The specified process no longer has any thread remaining.
	 *
	 * @param __kp The process which died.
	 * @since 2016/05/21
	 */
	public abstract void deadProcess(KernelProcess __kp);
	
	/**
	 * All processes within the kernel have been terminated.
	 *
	 * @since 2016/05/21
	 */
	public abstract void noMoreProcesses();
}

