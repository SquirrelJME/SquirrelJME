// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm.test;

import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.terp.TerpInterpreter;

/**
 * This is the Java virtual machine test kernel.
 *
 * @since 2016/05/27
 */
public class JVMTestKernel
	extends Kernel
{
	/** The interpreter to use when executing user processes. */
	protected final TerpInterpreter interpreter;
	
	/**
	 * Initializes the test kernel.
	 *
	 * @param __terp The interpreter backend to use for execution.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/27
	 */
	public JVMTestKernel(TerpInterpreter __terp)
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
	 * @since 2016/05/27
	 */
	@Override
	public void quitKernel()
	{
		System.exit(0);
	}
}

