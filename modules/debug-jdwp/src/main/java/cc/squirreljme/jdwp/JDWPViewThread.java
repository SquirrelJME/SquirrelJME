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
 * A viewer of threads.
 *
 * @since 2021/04/10
 */
public interface JDWPViewThread
	extends JDWPView
{	
	/**
	 * Returns the name of the given thread.
	 * 
	 * @param __what Get the name of which thread?
	 * @return The name of the thread.
	 * @since 2021/04/10
	 */
	String name(Object __what);
	
	/**
	 * Returns the thread parent.
	 * 
	 * @param __what Which thread to get the parent of?
	 * @return The parent of the given thread.
	 * @since 2021/04/10
	 */
	Object parentGroup(Object __what);
	
	/**
	 * Returns the thread suspension manager.
	 * 
	 * @param __what Which thread to get the suspension state from.
	 * @return The thread suspension manager.
	 * @since 2021/04/10
	 */
	JDWPThreadSuspension suspension(Object __what);
	
	/**
	 * Returns the status of the given thread.
	 * 
	 * @param __what Get the status of which thread? 
	 * @return The {@link ThreadStatusType}.
	 * @since 2021/04/10
	 */
	int status(Object __what);
}
