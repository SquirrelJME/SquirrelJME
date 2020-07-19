// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.fbui;

import javax.microedition.lcdui.Canvas;

/**
 * This is a drawing of a canvas.
 *
 * @since 2020/01/18
 */
@Deprecated
public class DrawingCanvas
	extends UIDrawerState
{
	/** The canvas to draw. */
	@Deprecated
	protected final Canvas canvas;
	
	/**
	 * Initializes the drawer.
	 *
	 * @param __c The canvas to draw.
	 * @since 2020/01/18
	 */
	@Deprecated
	public DrawingCanvas(Canvas __c)
	{
		super(__c);
		
		this.canvas = __c;
	}
}

