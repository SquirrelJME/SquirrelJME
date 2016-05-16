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

import java.util.HashSet;
import java.util.Set;

/**
 * This represents a process within the kernel. A process owns a number of
 * {@link Thread}s and also is used for when the system is in kernel code that
 * permissions are checked and such for the current process before an
 * operation is performed.
 *
 * This class acts as a {@link SecurityManager} to running threads.
 *
 * @since 2016/05/16
 */
public final class KernelProcess
{
	/**
	 * Initializes the kernel process.
	 *
	 * @since 2016/05/16
	 */
	KernelProcess()
	{
	}
	
	/**
	 * Adds a thread to the current process.
	 *
	 * @param __t The thread to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/16
	 */
	void __addThread(Thread __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

