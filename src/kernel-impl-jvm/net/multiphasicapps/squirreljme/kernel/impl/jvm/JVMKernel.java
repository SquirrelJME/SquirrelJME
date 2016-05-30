// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm;

import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelProcess;
import net.multiphasicapps.squirreljme.kernel.KernelThread;
import net.multiphasicapps.squirreljme.terp.Interpreter;

/**
 * This is the kernel which runs on an existing JVM.
 *
 * @since 2016/05/27
 */
public class JVMKernel
	extends Kernel
{
	/** The interpreter to use when executing user processes. */
	protected final Interpreter interpreter;
	
	/**
	 * Initializes the kernel.
	 *
	 * @param __terp The interpreter backend to use for execution.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/27
	 */
	public JVMKernel(Interpreter __terp)
		throws NullPointerException
	{
		// Check
		if (__terp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.interpreter = __terp;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	protected KernelProcess internalCreateProcess()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	protected KernelThread internalCreateThread()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	protected void internalRunCycle()
	{
		// Run a single interpreter cycle
		interpreter.runCycle();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	protected KernelThread internalCurrentThread()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/27
	 */
	@Override
	public void quitKernel()
	{
		System.exit(0);
	}
}

