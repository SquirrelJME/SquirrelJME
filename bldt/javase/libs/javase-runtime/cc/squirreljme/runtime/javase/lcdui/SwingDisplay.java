// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.javase.lcdui;

import cc.squirreljme.runtime.lcdui.server.LcdDisplay;

/**
 * This represents a display which utilizes Java's Swing.
 *
 * @since 2018/03/18
 */
public class SwingDisplay
	extends LcdDisplay
{
	/**
	 * Initializes the display.
	 *
	 * @param __dx The display index.
	 * @since 2018/03/17
	 */
	public SwingDisplay(int __dx)
	{
		super(__dx);
	}
}

