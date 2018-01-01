// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase;

import java.lang.ref.Reference;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTaskStatus;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelTask;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelTasks;

/**
 * This represents a task which is provided by a standard process.
 *
 * @since 2017/12/11
 */
public final class JavaProcessKernelTask
	extends KernelTask
{
	/** The process to watch. */
	protected final Process process;
	
	/**
	 * Initializes the process task.
	 *
	 * @param __ref Reference to the kernel tasks.
	 * @param __dx The task index.
	 * @param __perms The permissions for the task.
	 * @param __proc The child process.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/27
	 */
	public JavaProcessKernelTask(Reference<KernelTasks> __ref, int __dx,
		int __perms, Process __proc)
		throws NullPointerException
	{
		super(__ref, __dx, __perms, __proc.getInputStream(),
			__proc.getOutputStream());
		
		if (__proc == null)
			throw new NullPointerException("NARG");
		
		this.process = __proc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	protected int accessFlags()
	{
		int rv = 0;
		
		// Has initialization been completed?
		boolean initcomplete = this.isInitializationComplete();
		
		// If there is an exit value then set status according to failure or
		// success
		try
		{
			int ev = this.process.exitValue();
			if (initcomplete)
				if (ev == 0)
					rv |= SystemTaskStatus.EXITED_REGULAR;
				else
					rv |= SystemTaskStatus.EXITED_FATAL;
			
			// If there is an exit code and the boot flag was never set then
			// start failed
			else
				rv |= SystemTaskStatus.START_FAILED;
		}
		
		// Has not terminated yet, so assume it is running
		catch (IllegalThreadStateException e)
		{
			// This flag is set when the client sends the boot response
			// to the kernel IPC
			if (initcomplete)
				rv |= SystemTaskStatus.RUNNING;
			
			// Otherwise it sits at starting until that happens.
			else
				rv |= SystemTaskStatus.STARTING;
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	protected long accessMetric(int __m)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/01
	 */
	@Override
	protected void accessTerminated()
	{
	}
}

