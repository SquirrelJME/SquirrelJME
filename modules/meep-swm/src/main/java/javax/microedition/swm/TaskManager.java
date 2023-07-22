// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.util.List;

/**
 * This is an interface which is used to manage tasks. Tasks allow multiple
 * suites to be ran at the same time.
 *
 * @see ManagerFactory
 * @since 2016/06/24
 */
@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
@Api
public interface TaskManager
{
	/**
	 * Adds the given task listener so that if the state of any task changes
	 * then the provided methods are called.
	 *
	 * @param __tl The listener for events.
	 * @since 2016/06/24
	 */
	@Api
	void addTaskListener(TaskListener __tl);
	
	/**
	 * Returns the task which belongs to the caller of this method.
	 *
	 * @return The task of the current caller.
	 * @since 2016/06/24
	 */
	@Api
	Task getCurrentTask();
	
	/**
	 * This returns the list of all running tasks on the system.
	 *
	 * @param __incsys If {@code true} then any tasks which are owned by the
	 * system are also returned.
	 * @return The list of tasks which currently exist within the virtual
	 * machine.
	 * @since 2016/06/24
	 */
	@Api
	List<Task> getTaskList(boolean __incsys);
	
	/**
	 * Removes the given task listener so that it is no longer notified when
	 * the state of a task has changed.
	 *
	 * @param __tl The task listener to remove.
	 * @since 2016/06/24
	 */
	@Api
	void removeTaskListener(TaskListener __tl);
	
	/**
	 * Sets the given task so that it appears at the foreground task which
	 * is running, this operation may fail.
	 *
	 * A task that is in the foreground is one which is visible and has
	 * input focus. It is possible that a call of this method will have no
	 * effect.
	 *
	 * @param __t The task to bring to the foreground.
	 * @return If {@code true} then the task was brought to the foreground.
	 * @throws IllegalArgumentException If the task is a system task.
	 * @since 2016/06/24
	 */
	@Api
	boolean setForeground(Task __t)
		throws IllegalArgumentException;
	
	/**
	 * Attempts to set the priority of a given task. A call of this method
	 * may affect the amount of time slices which are available to a process
	 * or its resume priority in the event of cooperative multi-tasking.
	 *
	 * @param __t The task to change the priority of.
	 * @param __p The new priority of the given task.
	 * @return {@code true} if the priority of the given task has been
	 * changed.
	 * @throws IllegalArgumentException if the task is a system task.
	 * @since 2016/06/24
	 */
	@Api
	boolean setPriority(Task __t, TaskPriority __p)
		throws IllegalArgumentException;
	
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
	 * @param __s The suite to start, must be an application.
	 * @param __cn The class which extends
	 * {@link javax.microedition.midlet.MIDlet} and acts as the main entry
	 * point for the program.
	 * @return The task which was created.
	 * @throws IllegalArgumentException If the suite is a library, the given
	 * class does not exist, or the given class does not extend
	 * {@link javax.microedition.midlet.MIDlet}.
	 * @throws IllegalStateException If the suite has been removed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/24
	 */
	@Api
	Task startTask(Suite __s, String __cn)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException;
	
	/**
	 * Attempts to stop the given task and destroy it so that it does not
	 * consume memory or execution cycles.
	 *
	 * @param __t The task to be stopped.
	 * @return {@code true} if the task has been stopped and destroyed.
	 * @throws IllegalArgumentException If the task has already stopped, has
	 * not yet been started, or has finished execution.
	 * @throws IllegalStateException If the task is a system task.
	 * @since 2016/06/24
	 */
	@Api
	boolean stopTask(Task __t)
		throws IllegalArgumentException, IllegalStateException;
}

