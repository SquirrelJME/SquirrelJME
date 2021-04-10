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
 * This is a viewpoint of a single object, thread, class, method, or otherwise
 * within JDWP.
 *
 * @param <V> The view used to do interpret a given object or otherwise
 * @deprecated Very likely not needed at all.
 * @since 2021/04/10
 */
@Deprecated
public final class JDWPViewer<V extends JDWPView>
	implements JDWPId
{
	/**
	 * {@inheritDoc}
	 * @since 2021/04/10
	 */
	@Override
	public int debuggerId()
	{
		throw Debugging.todo();
	}
}
