// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.lcdui.scritchui.DisplayManager;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayState;
import cc.squirreljme.runtime.lcdui.scritchui.StringTrackerListener;

/**
 * Handles title changes for {@code Display}.
 *
 * @since 2024/07/18
 */
final class __ExecDisplaySetTitle__
	implements StringTrackerListener
{
	/** The display to update for. */
	final DisplayState _display;
	
	/**
	 * Updates the text for a display.
	 *
	 * @param __display The display to update for.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/18
	 */
	__ExecDisplaySetTitle__(DisplayState __display)
		throws NullPointerException
	{
		if (__display == null)
			throw new NullPointerException("NARG");
		
		this._display = __display;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/18
	 */
	@Override
	public void stringUpdated(String __s)
	{
		DisplayState display = this._display;
		
		DisplayManager.instance().scritch().label()
			.labelSetString(display.scritchWindow(), __s);
	}
}
