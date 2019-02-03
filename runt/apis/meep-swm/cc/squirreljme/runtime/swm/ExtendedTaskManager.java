// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.swm;

import java.io.OutputStream;
import java.util.Map;
import javax.microedition.swm.Suite;
import javax.microedition.swm.Task;

/**
 * This interface is used for providing the ability to launch tasks.
 *
 * @since 2019/02/02
 */
public interface ExtendedTaskManager
{
	/**
	 * Attempts to create a task which runs the given suite.
	 *
	 * The task is created and is initially in the {@link TaskStatus#STARTING}
	 * state. If starting fails then it enters the
	 * {@link TaskStatus#START_FAILED} state.
	 *
	 * A suite may only be active once and cannot have multiple copies running
	 * at the same time. In the event that an application is already running it
	 * must be sent an event specifying an application re-launch.
	 *
	 * This is an extended method which allows more advanced control over the
	 * task such as including arguments, system properties, and potential
	 * output redirection.
	 *
	 * @param __s The suite to start, must be an application.
	 * @param __cn The class which extends
	 * {@link javax.microedition.midlet.MIDlet} and acts as the main entry
	 * point for the program.
	 * @param __sprops System properties to pass to the started task.
	 * @param __args Arguments to pass to the called program.
	 * @param __stdout Alternative {@link System#out}.
	 * @param __stderr Alternative {@link System#err}.
	 * @return The task which was created.
	 * @throws IllegalArgumentException If the suite is a library, the given
	 * class does not exist, or the given class does not extend
	 * {@link javax.microedition.midlet.MIDlet}.
	 * @throws IllegalStateException If the suite has been removed.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/02
	 */
	public abstract Task startTask(Suite __s, String __cn,
		Map<String, String> __sprops, String[] __args, OutputStream __stdout,
		OutputStream __stderr)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException;
}
