// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter;

import java.lang.ref.Reference;

/**
 * This represents a thread within the virtual machine.
 *
 * @since 2017/10/06
 */
public class VMThread
	implements Runnable
{
	/** The reference to the owning interpreter. */
	protected final Reference<Interpreter> interpreter;
	
	/**
	 * Initializes the virtual machine thread.
	 *
	 * @param __i The owning interpreter.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/06
	 */
	public VMThread(Reference<Interpreter> __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		this.interpreter = __i;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/06
	 */
	@Override
	public void run()
	{
		throw new todo.TODO();
	}
}

