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
 * Represents a thread group which may contain sub-threads and otherwise.
 * 
 * Thread groups have the limitation that they are always top level and they
 * have no child {@link JDWPThreadGroup}s.
 *
 * @since 2021/03/13
 */
@Deprecated
public interface JDWPThreadGroup
	extends JDWPObjectLike
{
	/**
	 * Returns all of the child threads.
	 * 
	 * @return The threads which are the child of this thread group.
	 * @since 2021/03/13
	 */
	JDWPThread[] debuggerThreads();
}
