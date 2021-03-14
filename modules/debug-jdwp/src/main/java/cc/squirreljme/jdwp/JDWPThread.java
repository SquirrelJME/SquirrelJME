// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Represents a debugger thread.
 *
 * @since 2021/03/13
 */
public interface JDWPThread
	extends JDWPId
{
	/**
	 * Returns all of the thread frames.
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
	 * Returns the owning thread group.
	 * 
	 * @return The thread group this is in.
	 * @since 2021/03/13
	 */
	JDWPThreadGroup debuggerThreadGroup();
}
