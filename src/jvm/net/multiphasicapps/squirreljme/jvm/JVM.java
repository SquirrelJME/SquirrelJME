// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jvm;

/**
 * This class provides the basic control for the Java Virtual Machine.
 *
 * @since 2016/06/23
 */
public abstract class JVM
{
	/**
	 * Initializes the base virtual machine.
	 *
	 * @param __args Arguments to be passed to the launcher.
	 * @since 2016/06/23
	 */
	public JVM(String... __args)
	{
		throw new Error("TODO");
	}
	
	/**
	 * This returns an array of modules which are available for usage by the
	 * JVM.
	 *
	 * @return The array of modules which are available.
	 * @since 2016/06/23
	 */
	protected abstract JVMModule[] modules();
}

