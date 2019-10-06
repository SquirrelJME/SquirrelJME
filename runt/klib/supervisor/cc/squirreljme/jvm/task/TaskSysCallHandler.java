// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.task;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.ConfigReader;
import cc.squirreljme.jvm.ConfigRomType;
import cc.squirreljme.jvm.SupervisorPropertyIndex;
import cc.squirreljme.jvm.SystemCallIndex;

/**
 * This is the handler for system calls within tasks.
 *
 * @since 2019/10/06
 */
public final class TaskSysCallHandler
{
	/**
	 * Not used.
	 *
	 * @since 2019/10/06
	 */
	private TaskSysCallHandler()
	{
	}
	
	/**
	 * Initializes and sets the task handler for system calls.
	 *
	 * @param __cr The configuration to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/10/06
	 */
	public static final void initTaskHandler(ConfigReader __cr)
		throws NullPointerException
	{
		if (__cr == null)
			throw new NullPointerException("NARG");
			
		// Message
		todo.DEBUG.note("Setting task system call handler...");
		
		// Set handlers
		Assembly.sysCallP(SystemCallIndex.SUPERVISOR_PROPERTY_SET,
			SupervisorPropertyIndex.TASK_SYSCALL_STATIC_FIELD_POINTER,
			__cr.loadInteger(ConfigRomType.SYSCALL_STATIC_FIELD_POINTER));
		Assembly.sysCallP(SystemCallIndex.SUPERVISOR_PROPERTY_SET,
			SupervisorPropertyIndex.TASK_SYSCALL_METHOD_HANDLER,
			__cr.loadInteger(ConfigRomType.SYSCALL_CODE_POINTER));
		Assembly.sysCallP(SystemCallIndex.SUPERVISOR_PROPERTY_SET,
			SupervisorPropertyIndex.TASK_SYSCALL_METHOD_POOL_POINTER,
			__cr.loadInteger(ConfigRomType.SYSCALL_POOL_POINTER));
		
		// Message
		todo.DEBUG.note("Okay.");
	}
	
	/**
	 * Handler for task system calls.
	 *
	 * @param __tid The task ID.
	 * @param __oldsfp The old static field pointer.
	 * @param __si The system call that was made.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @param __h Argument.
	 * @return The result.
	 * @since 2019/10/05
	 */
	public static final int taskSysCall(int __tid, int __oldsfp, short __si,
		int __a, int __b, int __c, int __d, int __e, int __f, int __g, int __h)
	{
		todo.DEBUG.code('H', 't', __tid);
		todo.DEBUG.code('H', 's', __oldsfp);
		todo.DEBUG.code('H', 'd', __si);
		
		return Assembly.sysCallPV(__si, __a, __b, __c, __d, __e, __f, __g,
			__h);
	}
}

