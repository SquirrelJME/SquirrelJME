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
 * This exception is thrown when a task throws an exception, it just points
 * to the exception register pointer itself.
 *
 * @since 2019/10/13
 */
public class TaskThrownException
	extends Exception
{
	/** The thrown exception value. */
	protected final int pointer;
	
	/**
	 * Initializes the exception with the given pointer.
	 *
	 * @param __p The pointer value to set.
	 * @since 2019/10/13
	 */
	public TaskThrownException(int __p)
	{
		this.pointer = __p;
	}
}

