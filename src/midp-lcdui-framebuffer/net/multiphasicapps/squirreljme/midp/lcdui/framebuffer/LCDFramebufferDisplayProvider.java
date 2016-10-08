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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;
import net.multiphasicapps.squirreljme.bui.framebuffer.Framebuffer;
import net.multiphasicapps.squirreljme.bui.framebuffer.FramebufferManager;
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
		// Setup return value
		List<LCDUIDisplay> rv = new ArrayList<>();
		
		// Go through available framebuffers
		for (Framebuffer f : FramebufferManager.instance().uis())
			throw new Error("TODO");
		
		// Return it
		return rv.<LCDUIDisplay>toArray(new LCDUIDisplay[rv.size()]);
	}
}

