// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm;

import java.util.ServiceLoader;

/**
 * This class is used to initialize virtual machines based on a set of factory
 * classes.
 *
 * This was added because there were many cases where tons of code was
 * duplicated to initialize the virtual machine, which were effectively
 * copy and pasted to each other so this will remove that.
 *
 * This uses the service loader to locate factories which are available.
 *
 * @since 2018/11/17
 */
public abstract class VMFactory
{
	/** The name of the VM implementation. */
	protected final String name;
	
	/**
	 * Initializes the factory.
	 *
	 * @param __name The name of the virtual machine.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/17
	 */
	public VMFactory(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
	}
	
	/**
	 * Shaded main entry point.
	 *
	 * @param __args Arguments to the program.
	 * @since 2018/11/17
	 */
	public static final void shadedMain(String... __args)
	{
		// Force to exist
		if (__args == null)
			__args = new String[0];
		
		throw new todo.TODO();
	}
}

