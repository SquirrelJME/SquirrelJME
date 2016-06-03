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

import net.multiphasicapps.squirreljme.kernel.KernelProcess;
import net.multiphasicapps.squirreljme.terp.Interpreter;
import net.multiphasicapps.squirreljme.terp.InterpreterProcess;

/**
 * This is the implementation of processes which act as a bridge to the
 * interpreter memory space.
 *
 * @since 2016/05/31
 */
public class JVMKernelProcess
	extends KernelProcess
{
	/** The owning JVM kernel. */
	protected final JVMKernel jvmkernel;
	
	/** The interpreter process. */
	protected final InterpreterProcess iprocess;
	
	/**
	 * Initializes the kernel process.
	 *
	 * @param __k The owning kernel.
	 * @param __cp The classpath for object lookup.
	 * @since 2016/05/31
	 */
	JVMKernelProcess(JVMKernel __k, ClassPath __cp)
	{
		super(__k, __cp);
		
		// Set
		JVMKernel jvmkernel = (JVMKernel)this.kernel;
		this.jvmkernel = jvmkernel;
		
		// Get the used interpreter
		Interpreter terp = jvmkernel.interpreter();
		
		// Setup interpreter process
		this.iprocess = terp.createProcess(this.classpath);
	}
	
	/**
	 * Returns the process which is associated with the interpreter.
	 *
	 * @return The interpreter based process.
	 * @since 2016/06/03
	 */
	public final InterpreterProcess interpreterProcess()
	{
		return this.iprocess;
	}
}

