// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.host.javase;

import javax.microedition.lcdui.Canvas;
import net.multiphasicapps.squirreljme.lcdui.DisplayCanvasConnector;

/**
 * This provides an interface for the standard canvas.
 *
 * @since 2017/02/08
 */
public class SwingCanvasInstance
	extends SwingInstance
{
	/** The canvas to use. */
	protected final Canvas canvas;
	
	/** The connector to the canvas. */
	protected final DisplayCanvasConnector canvasconnector;
	
	/**
	 * Initializes the swing canvas instance.
	 *
	 * @param __d The canvas to use.
	 * @param __c The connector to that canvas.
	 * @since 2017/02/098
	 */
	public SwingCanvasInstance(Canvas __d, DisplayCanvasConnector __c)
	{
		super(__d, __c);
		
		this.canvas = __d;
		this.canvasconnector = __c;
	}
}

