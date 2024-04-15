// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.jvm.mle.brackets.VMThreadBracket;
import cc.squirreljme.vm.springcoat.AbstractGhostObject;
import cc.squirreljme.vm.springcoat.SpringMachine;
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
	 * @param __machine The machine used.
	 * @param __thread The thread to target.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/17
	 */
	public VMThreadObject(SpringMachine __machine, SpringThread __thread)
		throws NullPointerException
	{
		super(__machine, VMThreadBracket.class);
		
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
