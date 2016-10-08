// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bui.framebuffer.javase;

import net.multiphasicapps.squirreljme.bui.framebuffer.Framebuffer;

/**
 * This class implements the framebuffer interface and provides the view and
 * the handling of input controls via the standard Swing interface.
 *
 * @since 2016/10/08
 */
public class JavaSEFramebuffer
	implements Framebuffer
{
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public boolean isInActiveMode()
	{
		// Not supported
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public void setActiveMode(boolean __active)
	{
		// Display sleeping and/or screensaver inhibition is not supported
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public boolean supportsInputEvents()
	{
		// Always supports input
		return true;
	}
}

