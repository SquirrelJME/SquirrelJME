// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Thread groups for JDWP access.
 *
 * @since 2021/03/13
 */
public final class JDWPThreadGroups
{
	/**
	 * Returns the current thread groups.
	 * 
	 * @return The current thread groups.
	 * @since 2021/03/13
	 */
	public JDWPThreadGroup[] current()
	{
		throw Debugging.todo();
	}
}
