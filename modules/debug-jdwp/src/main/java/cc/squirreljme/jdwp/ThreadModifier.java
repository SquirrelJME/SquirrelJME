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
 * Modifier to match on a given thread.
 *
 * @since 2021/03/14
 */
public final class ThreadModifier
	implements EventModifier
{
	/** The thread to match. */
	protected final JDWPThread thread;
	
	/**
	 * Initializes the modifier.
	 * 
	 * @param __thread The thread to match on.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/14
	 */
	public ThreadModifier(JDWPThread __thread)
		throws NullPointerException
	{
		if (__thread == null)
			throw new NullPointerException("NARG");
		
		this.thread = __thread;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/16
	 */
	@Override
	public String toString()
	{
		return "Thread(" + this.thread.debuggerId() + ")";
	}
}
