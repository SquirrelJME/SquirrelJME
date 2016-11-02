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

import net.multiphasicapps.squirreljme.kernel.KernelInterface;
import net.multiphasicapps.squirreljme.kernel.KernelSuiteInterface;
import net.multiphasicapps.squirreljme.kernel.ThreadingExecutionModel;

/**
 * This bridges the kernel interface to the one used by the interpreter.
 *
 * @since 2016/10/31
 */
public class InterpreterInterface
	implements KernelInterface
{
	/** The owning interpreter. */
	protected final AutoInterpreter interpreter;
	
	/**
	 * Initializes the interface.
	 *
	 * @param __ai The interface to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/31
	 */
	InterpreterInterface(AutoInterpreter __ai)
		throws NullPointerException
	{
		// Check
		if (__ai == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.interpreter = __ai;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/31
	 */
	@Override
	public void cooperativeHostYield()
	{
		// It is really unknown if the host system is cooperatively tasked or
		// not, so always yield
		Thread.yield();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/31
	 */
	@Override
	public boolean isKernelInterrupted()
	{
		return Thread.interrupted();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/31
	 */
	@Override
	public int runCycleCount()
	{
		return 1024;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/11/01
	 */
	@Override
	public KernelSuiteInterface suiteInterface()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/31
	 */
	@Override
	public ThreadingExecutionModel threadingExecutionModel()
	{
		// The JVM running the interpreter handles the threading
		return ThreadingExecutionModel.EXTERNAL_THREADING;
	}
}

