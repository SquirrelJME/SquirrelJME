// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.task;

/**
 * This is the result of a created task
 *
 * @since 2019/12/08
 */
public final class TaskCreateResult
{
	/** The resulting task. */
	public final Task task;
	
	/** The initial thread. */
	public final TaskThread thread;
	
	/** The main entry class. */
	public final String mainclass;
	
	/** The main entry method name. */
	public final String mainmethodname;
	
	/** The main entry method type. */
	public final String mainmethodtype;
	
	/** The arguments to call. */
	public final int[] callargs;
	
	/**
	 * Initializes the task creation result.
	 *
	 * @param __task The created task.
	 * @param __thread The created task.
	 * @param __mcl The main class.
	 * @param __mname The method name.
	 * @param __mtype The method type.
	 * @param __callargs The arguments to the method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/08
	 */
	public TaskCreateResult(Task __task, TaskThread __thread, String __mcl,
		String __mname, String __mtype, int[] __callargs)
		throws NullPointerException
	{
		if (__task == null || __thread == null || __mcl == null ||
			__mname == null || __mtype == null || __callargs == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.thread = __thread;
		this.mainclass = __mcl;
		this.mainmethodname = __mname;
		this.mainmethodtype = __mtype;
		this.callargs = __callargs;
	}
}

