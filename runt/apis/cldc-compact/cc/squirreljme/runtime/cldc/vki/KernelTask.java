// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.vki;

/**
 * This class manages the tasks for the kernel and such.
 *
 * @since 2019/05/04
 */
public final class KernelTask
{
	/**
	 * Creates a new task.
	 *
	 * @param __classpath The class path.
	 * @param __sysprops System properties.
	 * @param __mainclass Main class.
	 * @param __mainargs Main arguments.
	 * @param __ismidlet Is this a MIDlet?
	 * @param __gd The current guest depth.
	 * @return The task ID, a pointer to the task identifier.
	 * @since 2019/05/04
	 */
	public static final int createTask(byte[][] __classpath,
		byte[][] __sysprops, byte[] __mainclass, byte[][] __mainargs,
		boolean __ismidlet, int __gd)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
}

