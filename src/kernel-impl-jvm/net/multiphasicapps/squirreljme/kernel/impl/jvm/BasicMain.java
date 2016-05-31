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

import net.multiphasicapps.squirreljme.terp.Interpreter;

/**
 * This provides the basic initialize of a JVM based kernel.
 *
 * @since 2016/05/30
 */
public class BasicMain
	implements Runnable
{
	/** The kernel being used. */
	protected final JVMKernel kernel;
	
	/**
	 * This initializes the kernel to use for execution along with the
	 * interpreter.
	 *
	 * @param __args The arguments to the kernel.
	 * @since 2016/05/30
	 */
	public BasicMain(String... __args)
	{
		// Create the kernel to use
		this.kernel = createKernel(null, __args);
		
		// {@squirreljme.error BC01 The kernel was never created.}
		if (this.kernel == null)
			throw new NullPointerException("BC01");
	}
	
	/**
	 * This creates an instance of the kernel to run on the JVM using the
	 * specified interpreter for execution.
	 *
	 * @param __terp The interpreter to use for execution.
	 * @param __args The arguments to the kernel.
	 * @throws NullPointerException If no interpreter was specified.
	 * @since 2016/05/30
	 */
	protected JVMKernel createKernel(Interpreter __terp, String... __args)
		throws NullPointerException
	{
		// Check
		if (__terp == null)
			throw new NullPointerException("NARG");
		
		// Create it
		return new JVMKernel(__terp, __args);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/30
	 */
	@Override
	public void run()
	{
		// Run kernel cycles
		JVMKernel kernel = this.kernel;
		for (;; Thread.yield())
			kernel.runCycle();
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Kernel arguments.
	 * @since 2016/05/30
	 */
	public static void main(String... __args)
	{
		// Setup basic main and run it
		new BasicMain(__args).run();
	}
}

