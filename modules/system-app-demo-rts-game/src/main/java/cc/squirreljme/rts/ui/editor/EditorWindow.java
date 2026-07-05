// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.ui.editor;

import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.rts.rate.RateController;
import cc.squirreljme.rts.rate.ScreenRunnable;
import cc.squirreljme.rts.ui.TerminateGame;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;

/**
 * This is the main editor window.
 *
 * @since 2026/07/05
 */
public class EditorWindow
	implements ScreenRunnable
{
	/** The rate controller. */
	protected final Reference<RateController> rate;
	
	/** The ScritchUI interface. */
	protected final ScritchInterface scritch;
	
	/** The editor window. */
	protected final ScritchWindowBracket winEditor;
	
	/** Was the window maximized? */
	private volatile boolean _madeMaximized;
	
	/** Was the window made visible? */
	private volatile boolean _madeVisible;
	
	/**
	 * Initializes the editor window.
	 *
	 * @param __scritch The ScritchUI interface.
	 * @param __rate The rate controller.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/07/05
	 */
	public EditorWindow(ScritchInterface __scritch,
		Reference<RateController> __rate)
		throws NullPointerException
	{
		if (__scritch == null || __rate == null)
			throw new NullPointerException("NARG");
		
		this.scritch = __scritch;
		this.rate = __rate;
		
		// Setup editor window
		ScritchWindowBracket winEditor = __scritch.window().windowNew();
		this.winEditor = winEditor;
		
		// Terminate the game if the window is closed
		__scritch.window().windowSetCloseListener(winEditor,
			new TerminateGame());
		
		// Set a proper title
		__scritch.label().labelSetString(winEditor, 
			"Scenario Editor");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/07/05
	 */
	@Override
	public void run()
	{
		// Do we need to make this visible?
		if (this.__latchVisible())
			this.scritch.window().windowSetVisible(this.winEditor,
				true);
	}
	
	/**
	 * Visibility latch.
	 *
	 * @return If the latch should make the window visible.
	 * @since 2026/06/10
	 */
	private boolean __latchVisible()
	{
		// Make the editor visible?
		if (!this._madeVisible && this._madeMaximized)
			synchronized (this)
			{
				// Double check again
				if (!this._madeVisible && this._madeMaximized)
				{
					this._madeVisible = true;
					return true;
				}
			}
		
		return false;
	}
}
