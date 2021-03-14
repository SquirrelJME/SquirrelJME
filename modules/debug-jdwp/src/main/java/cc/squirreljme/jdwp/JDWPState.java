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
 * This contains the complete state for the debugging instance.
 *
 * @since 2021/03/13
 */
public final class JDWPState
{
	/** Thread groups which are available. */
	public final JDWPLinker<JDWPThreadGroup> threadGroups =
		new JDWPLinker<>(JDWPThreadGroup.class);
	
	/** Threads that are available. */
	public final JDWPLinker<JDWPThread> threads =
		new JDWPLinker<>(JDWPThread.class);
}
