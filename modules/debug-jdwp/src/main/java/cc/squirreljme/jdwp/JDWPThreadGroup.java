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
 * Represents a thread group.
 *
 * @since 2021/03/13
 */
public final class JDWPThreadGroup
	implements JDWPId
{
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@Override
	public int id()
	{
		throw Debugging.todo();
	}
}
