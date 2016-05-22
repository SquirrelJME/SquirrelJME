// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm.javase;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.ui.UIDisplay;
import net.multiphasicapps.squirreljme.ui.UIDisplayManager;

/**
 * This is a display manager which interfaces with Java's Swing and uses it
 * to interact with the user.
 *
 * @since 2016/05/20
 */
public class SwingDisplayManager
	extends UIDisplayManager
{
	/** The kernel which created this. */
	protected final JVMJavaSEKernel kernel;
	
	/**
	 * Initializes the swing based display manager.
	 *
	 * @param The kernel which created this display manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/21
	 */
	public SwingDisplayManager(JVMJavaSEKernel __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.kernel = __k;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/21
	 */
	@Override
	public UIDisplay createDisplay()
	{
		throw new Error("TODO");
	}
}

