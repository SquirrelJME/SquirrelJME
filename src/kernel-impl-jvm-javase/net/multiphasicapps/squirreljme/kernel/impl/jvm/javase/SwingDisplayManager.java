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

import net.multiphasicapps.squirreljme.ui.UIDisplayManager;

/**
 * This is a display manager which interfaces with Java's Swing and uses it
 * to interact with the user.
 *
 * @since 2016/05/20
 */
public class SwingDisplayManager
	extends UIDisplayManager
	implements Runnable
{
	/**
	 * Runs the display manager code in a loop.
	 *
	 * @since 2016/05/20
	 */
	@Override
	public void run()
	{
	}
}

