// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bootstrap.javase.lcdui;

import javax.microedition.lcdui.Display;
import net.multiphasicapps.squirreljme.midp.lcdui.DisplayProtocol;
import net.multiphasicapps.squirreljme.midp.lcdui.VirtualDisplay;

/**
 * This is a virtual display used by the Swing display manager.
 *
 * @since 2016/10/14
 */
public class SwingVirtualDisplay
	extends VirtualDisplay
{
	/**
	 * Initilaizes the virtual display.
	 *
	 * @param __ds The owning display server.
	 * @param __id The id of the display.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/14
	 */
	public SwingVirtualDisplay(SwingDisplayServer __ds, byte __id)
	{
		super(__ds, __id);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/14
	 */
	@Override
	protected int capabilities()
	{
		// For now, just supports input events
		return Display.SUPPORTS_INPUT_EVENTS;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/14
	 */
	@Override
	protected int capabilitiesExtended()
	{
		return DisplayProtocol.EXTENDED_CAPABILITY_POINTER_EVENTS |
			DisplayProtocol.EXTENDED_CAPABILITY_POINTER_MOTION_EVENTS |
			DisplayProtocol.EXTENDED_CAPABILITY_COLOR;
	}
}

