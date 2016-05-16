// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.jvm.javase;

import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.AbstractConsoleView;

/**
 * This contains the launcher used by the host Java SE system.
 *
 * @since 2016/05/14
 */
public class JVMJavaSEKernel
	extends Kernel
{
	/**
	 * This initializes the launcher which uses an existing full Java SE JVM.
	 *
	 * @param __args The arguments to the launcher.
	 * @since 2016/05/14
	 */
	public JVMJavaSEKernel(String... __args)
	{
		// Must always exist
		if (__args == null)
			__args = new String[0];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/14
	 */
	@Override
	public AbstractConsoleView createConsoleView()
	{
		// Setup a new console which uses a Swing based interface
		SwingConsoleView scv = new SwingConsoleView(eventQueue());
		
		// Show it
		scv.setVisible();
		
		// Return it
		return scv;
	}
}

