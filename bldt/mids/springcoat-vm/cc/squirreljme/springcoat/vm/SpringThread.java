// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

/**
 * This class contains information about a thread within the virtual machine.
 *
 * @since 2018/09/01
 */
public final class SpringThread
{
	/** The thread ID. */
	protected final int id;
	
	/** The name of this thread. */
	protected final String name;
	
	/**
	 * Initializes the thread.
	 *
	 * @param __id The thread ID.
	 * @param __n The name of the thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/01
	 */
	SpringThread(int __id, String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		this.id = __id;
		this.name = __n;
	}
}

