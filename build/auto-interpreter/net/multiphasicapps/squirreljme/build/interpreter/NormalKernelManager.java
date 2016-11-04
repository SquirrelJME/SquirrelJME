// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.interpreter;

/**
 * This is the normal kernel manager which runs code as fast as possible.
 *
 * @since 2016/11/02
 */
public class NormalKernelManager
	extends AbstractKernelManager
{
	/** Interrupt trigger. */
	protected final Object interrupt =
		new Object();
	
	/**
	 * Initializes the normal kernel manager.
	 *
	 * @param __ai The interpreter owning this.
	 * @since 2016/11/02
	 */
	public NormalKernelManager(AutoInterpreter __ai)
	{
		super(__ai);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/11/03
	 */
	@Override
	public void runThreads()
		throws InterruptedException
	{
		// Wait for interrupt requests
		Object interrupt = this.interrupt;
		synchronized (interrupt)
		{
			interrupt.wait();
		}
	}
}

