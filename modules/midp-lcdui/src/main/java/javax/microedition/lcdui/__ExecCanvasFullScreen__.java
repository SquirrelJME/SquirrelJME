// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.annotation.ScritchEventLoop;
import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayState;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayableState;

/**
 * Callback for making a Canvas full-screen or not.
 *
 * @since 2025/12/23
 */
@KeepWhenCompacting
final class __ExecCanvasFullScreen__
	implements Runnable
{
	/** The canvas to full screen. */
	private final Canvas _canvas;
	
	/** Is full-screen being set? */
	private final boolean _isFull;
	
	@KeepWhenCompacting
	__ExecCanvasFullScreen__(Canvas __canvas, boolean __isFull)
	{
		this._canvas = __canvas;
		this._isFull = __isFull;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/23
	 */
	@Override
	@ScritchEventLoop
	public void run()
	{
		Canvas canvas = this._canvas;
		
		// Tell the display that we desire to be full-screen
		DisplayableState state = canvas.__state();
		state.desireFullScreen(this._isFull);
		
		// If we have a display, we need to tell it to check the new state
		// However, this can really only be done when the window is hidden
		// and then shown again... we want to skip all other exit/alert logic
		DisplayState current = state.currentDisplay();
		if (current != null)
			new __ExecDisplaySetCurrent__(state.scritchApi(),
					current.display(), canvas, null)
				.__refresh(true, true);
	}
}
