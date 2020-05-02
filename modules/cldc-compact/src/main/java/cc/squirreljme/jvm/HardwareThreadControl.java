// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * System call controls for hardware threads.
 *
 * @since 2020/05/01
 */
public interface HardwareThreadControl
{
	/**
	 * Creates a new hardware thread, the task ID of the thread will be the
	 * same as that of the creator of the hardware thread.
	 *
	 * @squirreljme.syscallreturn The hardware thread ID, this may be a system
	 * specific thread ID or another kind of value. This will be {@code 0}
	 * on errors.
	 * @since 2020/05/01
	 */
	byte CONTROL_CREATE_THREAD =
		1;
	
	/**
	 * Sets the task ID of the specified hardware thread.
	 *
	 * @squirreljme.syscallparam 1 The hardware thread ID.
	 * @squirreljme.syscallparam 2 The task ID to use.
	 * @since 2020/05/01
	 */
	byte CONTROL_THREAD_SET_TASKID =
		2;
	
	/** The number of hardware controls. */
	byte NUM_CONTROLS =
		3;
}
