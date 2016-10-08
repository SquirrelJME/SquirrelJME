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
import java.util.Map;
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
	/** This acts as a cache for framebuffers to displays. */
	protected final Map<Framebuffer, LCDFramebufferDisplay> cache =
		new WeakHashMap<>();
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public LCDUIDisplay[] displays()
	{
		// Setup return value
		List<LCDUIDisplay> rv = new ArrayList<>();
		
		// Lock on the cache map
		Map<Framebuffer, LCDFramebufferDisplay> cache = this.cache;
		synchronized (cache)
		{
			// Go through available framebuffers
			for (Framebuffer f : FramebufferManager.instance().uis())
			{
				// If the display has not been cached, it must be cached
				LCDFramebufferDisplay d = cache.get(f);
				if (d == null)
					cache.put(f, (d = new LCDFramebufferDisplay(f)));
				
				// Will need to return it
				rv.add(d);
			}
		}
		
		// Return it
		return rv.<LCDUIDisplay>toArray(new LCDUIDisplay[rv.size()]);
	}
}

