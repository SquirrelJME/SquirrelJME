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

import net.multiphasicapps.squirreljme.ci.CIMethod;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelProcess;
import net.multiphasicapps.squirreljme.kernel.KernelThread;
import net.multiphasicapps.squirreljme.terp.Interpreter;
import net.multiphasicapps.squirreljme.terp.InterpreterProcess;
import net.multiphasicapps.squirreljme.terp.InterpreterThread;

/**
 * This represents a single thread which runs in the kernel, this is tied
 * into the interpreter for execution of code.
 *
 * @since 2016/06/01
 */
public class JVMKernelThread
	extends KernelThread
{
	/** The owning JVM kernel. */
	protected final JVMKernel jvmkernel;
	
	/** The owning process. */
	protected final JVMKernelProcess jvmprocess;
	
	/** The interpreter based thread. */
	protected final InterpreterThread ithread;
	
	/**
	 * Initializes the JVM interpreter based thread.
	 *
	 * @param __k The owning kernel.
	 * @param __proc The owning process.
	 * @param __mm The main method.
	 * @param __args The method arguments.
	 * @since 2016/06/01
	 */
	public JVMKernelThread(JVMKernel __k, JVMKernelProcess __proc,
		CIMethod __mm, Object... __args)
	{
		super(__k, __proc);
		
		// Check
		if (__mm == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.jvmkernel = (JVMKernel)this.kernel;
		this.jvmprocess = (JVMKernelProcess)this.process;
		
		// Get the used interpreter
		Interpreter terp = this.jvmkernel.interpreter();
		
		// Get the process
		InterpreterProcess iproc = this.jvmprocess.interpreterProcess();
		
		// Setup new thread
		this.ithread = terp.createThread(iproc, __mm, __args);
	}
}

