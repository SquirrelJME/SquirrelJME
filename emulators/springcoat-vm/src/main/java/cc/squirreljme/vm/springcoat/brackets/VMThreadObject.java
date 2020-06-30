// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.vm.springcoat.AbstractGhostObject;
import cc.squirreljme.vm.springcoat.SpringThread;

/**
 * This represents the "thread" object.
 *
 * @since 2020/06/17
 */
public final class VMThreadObject
	extends AbstractGhostObject
{
	/** The thread to target. */
	protected final SpringThread thread;
	
	/**
	 * Initializes the thread.
	 *
	 * @param __thread The thread to target.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/17
	 */
	public VMThreadObject(SpringThread __thread)
		throws NullPointerException
	{
		if (__thread == null)
			throw new NullPointerException("NARG");
		
		this.thread = __thread;
	}
	
	/**
	 * Returns the thread this is bound to.
	 *
	 * @return The thread this is bound to.
	 * @since 2020/06/17
	 */
	public SpringThread getThread()
	{
		return this.thread;
	}
}
