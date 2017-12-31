// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.kernel.syscall;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemCaller;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemProgram;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemProgramInstallReport;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTask;
import net.multiphasicapps.squirreljme.runtime.kernel.Kernel;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelProgram;
import net.multiphasicapps.squirreljme.runtime.kernel.
	KernelProgramInstallReport;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelTask;

/**
 * This is a system caller which directly calls the kernel and uses the
 * provided task as the context for permissions and the source performing any
 * given action.
 *
 * This class must never expose the {@link Kernel} or {@link KernelTask}
 * instances.
 *
 * @since 2017/12/10
 */
public final class DirectCaller
	extends SystemCaller
{
	/** The kernel to call. */
	protected final Kernel kernel;
	
	/** The task to call the kernel as. */
	protected final KernelTask task;
	
	/** The mapping of kernel tasks to system tasks. */
	private static final Map<KernelTask, Reference<SystemTask>> _taskmap =
		new WeakHashMap<>();
	
	/** Mapping of kernel programs to system programs. */
	private static final Map<KernelProgram, Reference<SystemProgram>> _promap =
		new WeakHashMap<>();
	
	/**
	 * Initializes the in-kernel system caller.
	 *
	 * @param __k The kernel to call.
	 * @param __t The task of the process to call.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/10
	 */
	public DirectCaller(Kernel __k, KernelTask __t)
		throws NullPointerException
	{
		if (__k == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.kernel = __k;
		this.task = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/28
	 */
	@Override
	public final SystemProgramInstallReport installProgram(
		byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		KernelTask task = this.task;
		KernelProgramInstallReport report = this.kernel.programs(task).install(
			task, __b, __o, __l);
		
		int error = report.error();
		if (error != 0)
			return new SystemProgramInstallReport(error, report.message());
		return new SystemProgramInstallReport(__wrapProgram(report.program()));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/11
	 */
	@Override
	public final SystemProgram[] listPrograms(int __typemask)
	{
		KernelTask task = this.task;
		KernelProgram[] programs = this.kernel.programs(task).
			list(task, __typemask);
		
		// The programs returned by the kernel are wrapped internally
		int n = programs.length;
		SystemProgram[] rv = new SystemProgram[n];
		for (int i = 0; i < n; i++)
			throw new todo.TODO();
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	public SystemTask[] listTasks(boolean __incsys)
	{
		KernelTask task = this.task;
		KernelTask[] tasks = this.kernel.tasks(task).list(task, __incsys);
		
		// The returned tasks must be wrapped
		int n = tasks.length;
		SystemTask[] rv = new SystemTask[n];
		Map<KernelTask, Reference<SystemTask>> taskmap = this._taskmap;
		synchronized (taskmap)
		{
			for (int i = 0; i < n; i++)
			{
				KernelTask kt = tasks[i];
				Reference<SystemTask> ref = taskmap.get(kt);
				SystemTask st;
				
				// Need to wrap the task?
				if (ref == null || null == (st = ref.get()))
					taskmap.put(kt, new WeakReference<>((st =
						new DirectTask(task, kt))));
				
				rv[i] = st;
			}
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/28
	 */
	@Override
	public void setDaemonThread(Thread __t)
		throws IllegalThreadStateException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		this.kernel.setDaemonThread(__t);
	}
	
	/**
	 * Wraps the specified program.
	 *
	 * @param __p The program to wrap.
	 * @return The wrapped program.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/28
	 */
	private SystemProgram __wrapProgram(KernelProgram __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

