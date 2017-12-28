// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.kernel;

/**
 * This class contains bitfields which represent simple permissions.
 *
 * @since 2017/12/12
 */
public interface KernelSimplePermission
{
	/** Can simple permissions be set? */
	public static final int SET_SIMPLE_PERMISSION =
		0x0000_0001;
	
	/** Can read simple permissions? */
	public static final int GET_SIMPLE_PERMISSION =
		0x0000_0002;
	
	/** Can obtain the program manager. */
	public static final int GET_PROGRAMS_INSTANCE =
		0x0000_0004;
	
	/** Can list programs which are installed. */
	public static final int LIST_PROGRAMS =
		0x0000_0008;
	
	/** Can get properties about a program. */
	public static final int GET_PROGRAM_PROPERTY =
		0x0000_0010;
	
	/** Return the instance of the task manager. */
	public static final int GET_TASKS_INSTANCE =
		0x0000_0020;
	
	/** Return the list of available tasks. */
	public static final int LIST_TASKS =
		0x0000_0040;
	
	/** Can get a property about a task. */
	public static final int GET_TASK_PROPERTY =
		0x0000_0080;
}

