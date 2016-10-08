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

import javax.microedition.lcdui.Display;
import net.multiphasicapps.squirreljme.bui.framebuffer.Framebuffer;
import net.multiphasicapps.squirreljme.midp.lcdui.LCDUIDisplayInstance;

/**
 * This performs the drawing that is needed to bridge the LCDUI interface
 * with the framebuffer.
 *
 * @since 2016/10/08
 */
public class LCDFramebufferDisplayInstance
	implements LCDUIDisplayInstance
{
	/** The framebuffer to use. */
	protected final Framebuffer framebuffer;
	
	/**
	 * Initializes the display instance for the framebuffer.
	 *
	 * @param __fb The framebuffer to draw on.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/08
	 */
	public LCDFramebufferDisplayInstance(Framebuffer __fb)
		throws NullPointerException
	{
		// Check
		if (__fb == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.framebuffer = __fb;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public int getCapabilities()
	{
		// If it supports input, say as such
		if (this.framebuffer.supportsInputEvents())
			return Display.SUPPORTS_INPUT_EVENTS;
		
		// Otherwise only canvases are supported, Forms will be virtualized
		// by the very upper classes
		return 0;
	}
}

