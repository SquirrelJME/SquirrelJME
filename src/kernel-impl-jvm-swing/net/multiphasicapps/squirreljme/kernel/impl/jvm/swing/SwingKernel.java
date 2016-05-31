// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm.swing;

import net.multiphasicapps.squirreljme.kernel.impl.jvm.JVMKernel;
import net.multiphasicapps.squirreljme.terp.Interpreter;

/**
 * This is the swing based kernel which essentially just provides the display
 * server so that the user can interact with the running virtual machine.
 *
 * @since 2016/05/30
 */
public class SwingKernel
	extends JVMKernel
{
	/**
	 * Initializes the swing based kernel.
	 *
	 * @param __terp The interpreter to use.
	 * @param __args The kernel arguments.
	 * @since 2016/05/30
	 */
	public SwingKernel(Interpreter __terp, String... __args)
	{
		super(__terp, __args);
	}
}

