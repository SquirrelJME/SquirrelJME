// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.lcdui.scritchui.DisplayableState;

/**
 * Repainter for canvases.
 *
 * @since 2024/04/25
 */
class __ExecCanvasRepainter__
	extends __ExecCanvas__
	implements Runnable
{
	/**
	 * Initializes the repainter.
	 *
	 * @param __canvas The canvas to repaint.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/04/25
	 */
	__ExecCanvasRepainter__(Canvas __canvas)
		throws NullPointerException
	{
		super(__canvas);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/25
	 */
	@Override
	public void run()
	{
		// Do we have the canvas still?
		Canvas canvas = this._canvas.get();
		if (canvas == null)
			return;
		
		// Forward repaint call
		DisplayableState state = canvas._state;
		state.scritchApi().paintable().repaint(state.scritchPanel());
	}
}
