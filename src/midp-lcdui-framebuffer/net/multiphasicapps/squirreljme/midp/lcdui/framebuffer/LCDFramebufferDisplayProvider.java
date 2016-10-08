// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.midp.lcdui.framebuffer;

import net.multiphasicapps.squirreljme.midp.lcdui.LCDUIDisplay;
import net.multiphasicapps.squirreljme.midp.lcdui.LCDUIDisplayProvider;

/**
 * This provides access to the framebuffer based display driver.
 *
 * @since 2016/10/08
 */
public class LCDFramebufferDisplayProvider
	implements LCDUIDisplayProvider
{
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public LCDUIDisplay[] displays()
	{
		throw new Error("TODO");
	}
}

