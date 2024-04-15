// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This indicates an index for a supervisor register which is controlled and
 * only settable by the supervisor. It is used in system call handling and
 * otherwise.
 *
 * @since 2019/10/04
 */
public interface SupervisorPropertyIndex
{
	/** The static field register of the task syscall handler. */
	byte TASK_SYSCALL_STATIC_FIELD_POINTER =
		1;
	
	/** The method pointer of the task syscall method. */
	byte TASK_SYSCALL_METHOD_HANDLER =
		2;
	
	/** The pool pointer of the task syscall method. */
	byte TASK_SYSCALL_METHOD_POOL_POINTER =
		3;
	
	/** The number of available properties. */
	byte NUM_PROPERTIES =
		4;
}

