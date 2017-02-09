// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.DisplayCapabilityException;

/**
 * This is a display engine that is used to determine .
 *
 * @since 2017/02/08
 */
public interface DisplayEngine
{
	/**
	 * Sets the current displayable to show.
	 *
	 * A call to {@link #update()} should be performed following this if
	 * applicable.
	 *
	 * @param __d The display to show.
	 * @param __c The connector which allows the engine to interact with the
	 * {@link Displayable} as needed.
	 * @return The display instance for the given displayable.
	 * @throws DisplayCapabilityException If the engine is not capable of
	 * using the given displayable.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/08
	 */
	public abstract DisplayInstance setDisplayable(Displayable __d,
		DisplayConnector __c)
		throws DisplayCapabilityException, NullPointerException;
}

