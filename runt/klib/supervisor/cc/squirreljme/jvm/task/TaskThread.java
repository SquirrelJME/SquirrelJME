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
 * This represents a single thread within a task.
 *
 * @since 2019/10/13
 */
public final class TaskThread
{
	private int _staticfieldptr;
	
	/**
	 * Enters the given frame on the thread.
	 *
	 * @param __cl The class to execute.
	 * @param __mn The method name.
	 * @param __mt The method type.
	 * @param __args The arguments to the thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/10/13
	 */
	public final void enterFrame(String __cl, String __mn, String __mt,
		int[] __args)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Performs inline execution of this thread, entering it and executing it
	 * on the same thread line as the current thread.
	 *
	 * @return The return value from execution, this will be the value of both
	 * return registers.
	 * @throws TaskException If this thread threw an exception.
	 * @since 2019/10/13
	 */
	public final long inlineExecute()
		throws TaskException
	{
		throw new todo.TODO();
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

