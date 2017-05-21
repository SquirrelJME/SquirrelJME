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

import javax.microedition.lcdui.Graphics;

/**
 * This is a connector which is used to provide access to the canvas.
 *
 * @since 2017/02/08
 */
@Deprecated
public interface DisplayCanvasConnector
	extends DisplayConnector, KeyEventCapableConnector,
		PointerEventCapableConnector
{
	/**
	 * Tells the canvas to perform its paint events.
	 *
	 * @param __g The graphics to paint to.
	 * @since 2017/02/08
	 */
	public abstract void paint(Graphics __g);
}

