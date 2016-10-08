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

import net.multiphasicapps.squirreljme.bui.framebuffer.Framebuffer;
import net.multiphasicapps.squirreljme.midp.lcdui.LCDUIDisplay;
import net.multiphasicapps.squirreljme.midp.lcdui.LCDUIDisplayInstance;

/**
 * This is a display which accesses a framebuffer.
 *
 * @since 2016/10/08
 */
public class LCDFramebufferDisplay
	extends LCDUIDisplay
{
	/** The framebuffer this wraps. */
	protected final Framebuffer framebuffer;
	
	/**
	 * Initializes the framebuffer wrapper.
	 *
	 * @param __f The framebuffer to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/08
	 */
	public LCDFramebufferDisplay(Framebuffer __f)
		throws NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.framebuffer = __f;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public LCDUIDisplayInstance createInstance()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public boolean hasCapabilities(int __caps)
	{
		throw new Error("TODO");
	}
}

