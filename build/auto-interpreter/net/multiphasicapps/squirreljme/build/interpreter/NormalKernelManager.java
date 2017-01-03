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

import net.multiphasicapps.squirreljme.kernel.ProcessCreationException;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelLaunchParameters;
import net.multiphasicapps.squirreljme.kernel.KernelProcess;

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
	
	/** The real launch parameters to use. */
	protected final KernelLaunchParameters launchparms;
	
	/**
	 * Initializes the normal kernel manager.
	 *
	 * @param __ai The interpreter owning this.
	 * @param __lp Kernel launch parameters.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/02
	 */
	public NormalKernelManager(AutoInterpreter __ai,
		KernelLaunchParameters __lp)
		throws NullPointerException
	{
		super(__ai);
		
		// Check
		if (__lp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.launchparms = __lp;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/03
	 */
	@Override
	public KernelProcess createProcess(Kernel __k)
		throws NullPointerException, ProcessCreationException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Create it
		return new NormalKernelProcess(__k);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/07
	 */
	@Override
	protected KernelLaunchParameters internalLaunchParameters()
	{
		return this.launchparms;
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

