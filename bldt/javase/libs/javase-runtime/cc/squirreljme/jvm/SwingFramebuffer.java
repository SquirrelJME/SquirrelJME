// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

import cc.squirreljme.runtime.lcdui.vfb.VirtualFramebuffer;

/**
 * This is a framebuffer that uses Java SE's Swing framework.
 *
 * @since 2019/12/28
 */
public final class SwingFramebuffer
{
	/** The singular created instance. */
	private static SwingFramebuffer _INSTANCE;
	
	/** Virtual framebuffer to use. */
	protected final VirtualFramebuffer vfb =
		new VirtualFramebuffer(new DefaultIPCRouter());
	
	/**
	 * Returns an instance of the framebuffer.
	 *
	 * @return The framebuffer instance.
	 * @since 2019/12/28
	 */
	public static final SwingFramebuffer instance()
	{
		SwingFramebuffer rv = _INSTANCE;
		if (rv == null)
			_INSTANCE = (rv = new SwingFramebuffer());
		return rv;
	}
}

