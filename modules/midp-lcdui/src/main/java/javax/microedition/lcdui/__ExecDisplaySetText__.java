// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayState;
import cc.squirreljme.runtime.lcdui.scritchui.TextTrackerListener;

/**
 * Handles title changes for {@code Display}.
 *
 * @since 2024/07/18
 */
final class __ExecDisplaySetText__
	implements TextTrackerListener
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
	__ExecDisplaySetText__(DisplayState __display)
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
	public void textUpdated(String __t)
	{
		throw Debugging.todo();
	}
}
