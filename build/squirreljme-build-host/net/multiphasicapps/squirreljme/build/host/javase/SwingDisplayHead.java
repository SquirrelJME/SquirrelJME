// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.host.javase;

import net.multiphasicapps.squirreljme.lcdui.DisplayHardwareState;
import net.multiphasicapps.squirreljme.lcdui.DisplayHead;
import net.multiphasicapps.squirreljme.lcdui.DisplayState;

/**
 * This is a display head which outputs to Swing.
 *
 * @since 2017/08/19
 */
public class SwingDisplayHead
	extends DisplayHead
{
	/**
	 * Initializes the display head.
	 *
	 * @since 2017/10/01
	 */
	public SwingDisplayHead()
	{
		// Make the display hardware enabled always
		setHardwareState(DisplayHardwareState.ENABLED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/01
	 */
	@Override
	protected void displayStateChanged(DisplayState __old,
		DisplayState __new)
		throws NullPointerException
	{
		if (__old == null || __new == null)
			throw new NullPointerException("NARG");
		
		// The swing interface has no notion of foreground and background
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/01
	 */
	@Override
	protected void hardwareStateChanged(DisplayHardwareState __old,
		DisplayHardwareState __new)
		throws NullPointerException
	{
		if (__new == null)
			throw new NullPointerException("NARG");
		
		// The swing interface does not really care about this, so do
		// nothing at all
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public boolean isColor()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public int numColors()
	{
		return 256 * 256 * 256;
	}
}

