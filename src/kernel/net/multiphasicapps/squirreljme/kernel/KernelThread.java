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
 * This is an abstract representation of a thread which runs within the kernel
 * that is implemented using native means.
 *
 * @since 2016/05/28
 */
public abstract class KernelThread
	implements __Identifiable__
{
	/** The kernel which owns this thread. */
	protected final Kernel kernel;
	
	/** The thread ID. */
	protected final int id;
	
	/**
	 * Initializes the base kernel thread.
	 *
	 * @param __k The owning kernel.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/28
	 */
	public KernelThread(Kernel __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.kernel = __k;
		this.id = __k._threadidgen.__next();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	public final int id()
	{
		return this.id;
	}
}

