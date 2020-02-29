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
import cc.squirreljme.jvm.Globals;
import cc.squirreljme.jvm.io.MemoryBlob;
import cc.squirreljme.jvm.lib.ClassNameUtils;
import cc.squirreljme.jvm.SystemCallIndex;

/**
 * This represents a single thread, which is associated with a task.
 *
 * Every thread has a controller thread, this is the thread which is
 * actually executing the given thread even if the IDs are different. This
 * allows other threads to execute within the contexts of other threads
 * accordingly.
 *
 * @since 2019/10/13
 */
public final class TaskThread
{
	/** Maximum number of arguments to methods. */
	public static final byte MAX_CALL_ARGUMENTS =
		8;
	
	/** The owning process ID. */
	protected final int pid;
	
	/** The thread ID. */
	protected final int tid;
	
	/** The logical ID of this thread. */
	protected final int lid;
	
	/** The static field pointer for this thread. */
	private int _staticfieldptr;
	
	/**
	 * Initializes the thread.
	 *
	 * @param __pid The owning process ID.
	 * @param __tid The task ID.
	 * @param __lid The logical thread ID.
	 * @since 2019/10/19
	 */
	public TaskThread(int __pid, int __tid, int __lid)
	{
		this.pid = __pid;
		this.tid = __tid;
		this.lid = __lid;
	}
	
	/**
	 * Enters the given frame on the thread. Note that this can only be
	 * done from the current thread where it will be executed.
	 *
	 * This searches for the method and loads any classes as needed.
	 *
	 * @param __cl The class to execute.
	 * @param __mn The method name.
	 * @param __mt The method type.
	 * @param __args The arguments to the thread.
	 * @return The return values of the method call.
	 * @throws IllegalArgumentException If the argument count is too high or
	 * the requested class is an array or primitive type.
	 * @throws IllegalStateException If the current thread is being executed
	 * and the current controller thread is not the current thread of
	 * execution.
	 * @throws NullPointerException On null arguments.
	 * @throws TaskThrownException If the task threw an exception.
	 * @since 2019/10/13
	 */
	public final long execute(String __cl, String __mn, String __mt,
		int... __args)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException, TaskThrownException
	{
		if (__cl == null || __mn == null || __mt == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error SV12 Cannot execute into a special class type
		// such as an array or primitive type.}
		if (ClassNameUtils.isSpecial(__cl))
			throw new IllegalArgumentException("SV12");
		
		// Get the owning task
		Task task = Globals.getTaskManager().getTask(this.pid);
		
		// Load our class
		TaskClass eclass = task.loadClass(__cl);
		
		// Execute the resultant method (use the pool of the target class)
		return this.execute(((MemoryBlob)task.classpath.classParser(eclass.
				resourceindex).methodCodeBytes(__mn, __mt)).baseAddress(),
			task.classInfoUtility().poolPointer(eclass), __args);
	}
	
	/**
	 * Enters the given frame on the thread. Note that this can only be
	 * done from the current thread where it will be executed.
	 *
	 * @param __methpool The combined method pointer to invoke and the
	 * constant pool pointer to load, the method pointer is in the low word
	 * while the pool is in the high word.
	 * @param __args The arguments to the thread.
	 * @return The return values of the method call
	 * @throws IllegalStateException If the current thread is being executed
	 * and the current controller thread is not the current thread of
	 * execution.
	 * @throws NullPointerException On null arguments.
	 * @throws TaskThrownException If the task threw an exception.
	 * @since 2019/12/08
	 */
	public final long execute(long __methpool, int... __args)
		throws IllegalStateException, NullPointerException, TaskThrownException
	{
		return this.execute(Assembly.longUnpackLow(__methpool),
			Assembly.longUnpackHigh(__methpool), __args);
	}
	
	/**
	 * Enters the given frame on the thread. Note that this can only be
	 * done from the current thread where it will be executed.
	 *
	 * @param __meth The method pointer to invoke.
	 * @param __pool The constant pool pointer to load.
	 * @param __args The arguments to the thread.
	 * @return The return values of the method call
	 * @throws IllegalArgumentException If too many method arguments were
	 * passed.
	 * @throws IllegalStateException If the current thread is being executed
	 * and the current controller thread is not the current thread of
	 * execution.
	 * @throws NullPointerException On null arguments.
	 * @throws TaskThrownException If the task threw an exception.
	 * @since 2019/12/08
	 */
	public final long execute(int __meth, int __pool, int... __args)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException, TaskThrownException
	{
		if (__args == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error SV11 Cannot execute method with the given
		// number of arguments.}
		if (__args.length > TaskThread.MAX_CALL_ARGUMENTS)
			throw new IllegalArgumentException("SV11");
		
		// Get the owning task
		Task task = Globals.getTaskManager().getTask(this.pid);
		
		// Set the task and enter user mode now
		Assembly.sysCallP(SystemCallIndex.FRAME_TASK_ID_SET, task.lid);
		
		// Set new static field register
		int oldsfp = Assembly.specialGetStaticFieldRegister();
		Assembly.specialSetStaticFieldRegister(this._staticfieldptr);
		
		// The number of pass arguments varies!
		int exception = 0;
		long rv = 0;
		try
		{
			switch (__args.length)
			{
				case 0:
				default:
					rv = Assembly.invokeVL(__meth, __pool);
					break;
				
				case 1:
					rv = Assembly.invokeVL(__meth, __pool, __args[0]);
					break;
				
				case 2:
					rv = Assembly.invokeVL(__meth, __pool, __args[0],
						__args[1]);
					break;
				
				case 3:
					rv = Assembly.invokeVL(__meth, __pool, __args[0],
						__args[1], __args[2]);
					break;
				
				case 4:
					rv = Assembly.invokeVL(__meth, __pool, __args[0],
						__args[1], __args[2], __args[3]);
					break;
				
				case 5:
					rv = Assembly.invokeVL(__meth, __pool, __args[0],
						__args[1], __args[2], __args[3], __args[4]);
					break;
				
				case 6:
					rv = Assembly.invokeVL(__meth, __pool, __args[0],
						__args[1], __args[2], __args[3], __args[4], __args[5]);
					break;
				
				case 7:
					rv = Assembly.invokeVL(__meth, __pool, __args[0],
						__args[1], __args[2], __args[3], __args[4], __args[5],
						__args[6]);
					break;
				
				case 8:
					rv = Assembly.invokeVL(__meth, __pool, __args[0],
						__args[1], __args[2], __args[3], __args[4], __args[5],
						__args[6], __args[7]);
					break;
			}
		}
		
		// Wrap the exception value
		catch (Throwable t)
		{
			exception = Assembly.objectToPointer(t);
		}
		
		// Restore some of our state
		finally
		{
			// The static field register space
			Assembly.specialSetStaticFieldRegister(oldsfp);
		}
		
		// Return the result or throw exception
		if (exception != 0)
			throw new TaskThrownException(exception);
		return rv;
	}
	
	/**
	 * Returns the process ID of this task.
	 *
	 * @return The process ID.
	 * @since 2019/12/14
	 */
	public final int processId()
	{
		return this.pid;
	}
	
	/**
	 * Sets the static field pointer for this thread.
	 *
	 * @param __d The static field pointer.
	 * @since 2019/10/13
	 */
	public final void setStaticFieldPointer(int __d)
	{
		this._staticfieldptr = __d;
	}
}

