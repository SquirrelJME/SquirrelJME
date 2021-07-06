// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.views;

import cc.squirreljme.jdwp.JDWPStepTracker;
import cc.squirreljme.jdwp.JDWPThreadSuspension;
import cc.squirreljme.jvm.mle.constants.ThreadStatusType;

/**
 * A viewer of threads.
 *
 * @since 2021/04/10
 */
public interface JDWPViewThread
	extends JDWPViewValidObject
{
	/**
	 * Returns the stack frame of the given thread.
	 * 
	 * @param __which Get the frames of which thread?
	 * @return The stack frames, the top-most thread is the first index in
	 * the array.
	 * @since 2021/04/11
	 */
	Object[] frames(Object __which);
	
	/**
	 * Gets the instance object of the given thread.
	 * 
	 * @param __which Get the instance object of which thread?
	 * @return The instance object of the given thread.
	 * @since 2021/04/18
	 */
	Object instance(Object __which);
	
	/**
	 * Interrupts the given thread.
	 * 
	 * @param __which Which thread to interrupt?
	 * @since 2021/04/30
	 */
	void interrupt(Object __which);
	
	/**
	 * Returns the termination status of the thread.
	 * 
	 * @param __which Get the termination state of which thread?
	 * @return If the given thread is terminated.
	 * @since 2021/04/19
	 */
	boolean isTerminated(Object __which);
	
	/**
	 * Returns the name of the given thread.
	 * 
	 * @param __which Get the name of which thread?
	 * @return The name of the thread.
	 * @since 2021/04/10
	 */
	String name(Object __which);
	
	/**
	 * Returns the thread parent.
	 * 
	 * @param __which Which thread to get the parent of?
	 * @return The parent of the given thread.
	 * @since 2021/04/10
	 */
	Object parentGroup(Object __which);
	
	/**
	 * Returns and potentially creates the thread's stepping tracker.
	 * 
	 * @param __which Which thread to get the step tracker from?
	 * @return The step tracker for the given thread, creating it if it is
	 * missing.
	 * @since 2021/04/28
	 */
	JDWPStepTracker stepTracker(Object __which);
	
	/**
	 * Returns the thread suspension manager.
	 * 
	 * @param __which Which thread to get the suspension state from.
	 * @return The thread suspension manager.
	 * @since 2021/04/10
	 */
	JDWPThreadSuspension suspension(Object __which);
	
	/**
	 * Returns the status of the given thread.
	 * 
	 * @param __which Get the status of which thread? 
	 * @return The {@link ThreadStatusType}.
	 * @since 2021/04/10
	 */
	int status(Object __which);
}
