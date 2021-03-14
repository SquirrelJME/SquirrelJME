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
	 * Returns the frame count.
	 * 
	 * @since 2021/03/13
	 */
	int debuggerFrameCount();
	
	/**
	 * Queries, suspends, or otherwise resumes the thread, after the operation
	 * completes the suspension count will be returned.
	 * 
	 * @param __type The type of suspend.
	 * @return The resultant suspension count.
	 * @since 2021/03/13
	 */
	int debuggerSuspend(JDWPSuspend __type);
}
