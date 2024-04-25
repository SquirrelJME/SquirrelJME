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
import java.lang.ref.Reference;

/**
 * Repainter for canvases.
 *
 * @since 2024/04/25
 */
class __ExecCanvasRepainter__
	implements Runnable
{
	/** The canvas to paint. */
	private final Reference<Canvas> _canvas;
	
	/**
	 * Initializes the repainter.
	 *
	 * @param __canvas The canvas to repaint.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/04/25
	 */
	__ExecCanvasRepainter__(Reference<Canvas> __canvas)
		throws NullPointerException
	{
		if (__canvas == null)
			throw new NullPointerException("NARG");
		
		this._canvas = __canvas;
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
		state.scritchApi().panel().repaint(state.scritchPanel());
	}
}
