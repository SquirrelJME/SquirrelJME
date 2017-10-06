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
import net.multiphasicapps.squirreljme.jit.cff.ClassName;

/**
 * This represents a thread within the virtual machine.
 *
 * @since 2017/10/06
 */
public class VMThread
	implements Runnable
{
	/** The reference to the owning interpreter. */
	protected final Reference<Interpreter> _interpreterref;
	
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
		
		this._interpreterref = __i;
	}
	
	/**
	 * Returns the instance for the specified class.
	 *
	 * @param __cn The name of the class to get the instance for.
	 * @return The instance of the class.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/06
	 */
	public final ClassInstance classInstance(ClassName __cn)
		throws NullPointerException
	{
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/06
	 */
	@Override
	public final void run()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the interpreter which owns this thread.
	 *
	 * @return The interpreter.
	 * @throws IllegalStateException If the interpreter was garbage collected.
	 * @since 2017/10/06
	 */
	private Interpreter __interpreter()
		throws IllegalStateException
	{
		// {@squirreljme.error AH01 The interpreter has been garbage
		// collected.}
		Interpreter rv = this._interpreterref.get();
		if (rv == null)
			throw new IllegalStateException("AH01");
		return rv;
	}
}

