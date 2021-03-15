// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jvm.mle.constants.ThreadStatusType;

/**
 * Represents a debugger thread.
 *
 * @since 2021/03/13
 */
public interface JDWPThread
	extends JDWPObjectLike
{
	/**
	 * Returns all of the thread frames, it will be noted that this is in
	 * the same format as {@link Throwable#printStackTrace()} in that the
	 * top-most frames are lower. This means that the most recently called
	 * method should be at index zero.
	 * 
	 * @return The thread frames.
	 * @since 2021/03/13
	 */
	JDWPThreadFrame[] debuggerFrames();
	
	/**
	 * Returns the suspension tracker.
	 * 
	 * @return The suspension tracker.
	 * @since 2021/03/13
	 */
	JDWPThreadSuspension debuggerSuspend();
	
	/**
	 * Returns the thread object used for this thread.
	 * 
	 * @return The object used for the thread.
	 * @since 2021/03/14
	 */
	JDWPObject debuggerThreadObject();
	
	/**
	 * Returns the owning thread group.
	 * 
	 * @return The thread group this is in.
	 * @since 2021/03/13
	 */
	JDWPThreadGroup debuggerThreadGroup();
	
	/**
	 * Returns the {@link ThreadStatusType} of a thread.
	 * 
	 * @return The {@link ThreadStatusType}.
	 * @since 2021/03/15
	 */
	int debuggerThreadStatus();
}
