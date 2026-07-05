// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.ui;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPencilBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchInputListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchPaintListener;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchInputMethodType;
import cc.squirreljme.rts.map.WorldMap;
import cc.squirreljme.rts.rate.RateController;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.mle.PencilGraphics;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Performs the actual main game drawing.
 *
 * @since 2026/06/10
 */
public class FullScreenDrawer
	implements ScritchPaintListener, ScritchInputListener
{
	/** The maximum number of local viewports. */
	public static final int MAX_LOCAL_VIEWS =
		4;
	
	/** The frame rate controller. */
	protected final Reference<RateController> rate;
	
	/** Local viewports. */
	private final Viewport[] _views;
	
	/** How many views are viewing locally? */
	private volatile int _numViewers;
	
	/**
	 * Initializes the drawer.
	 *
	 * @param __rate The frame rate controller.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/10
	 */
	public FullScreenDrawer(Reference<RateController> __rate)
		throws NullPointerException
	{
		if (__rate == null)
			throw new NullPointerException("NARG");
		
		// These are important
		this.rate = __rate;
		
		// Setup local viewports
		Viewport[] views = new Viewport[FullScreenDrawer.MAX_LOCAL_VIEWS];
		this._views = views;
		for (int n = views.length, i = 0; i < n; i++)
			views[i] = new Viewport(i);
		this._numViewers = views.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/07/04
	 */
	@Override
	public void inputEvent(@NotNull ScritchComponentBracket __component,
		int __type, long __time, int __a, int __b, int __c, int __d, int __e,
		int __f, int __g, int __h, int __i, int __j, int __k, int __l)
	{
		// Update local cursor position?
		switch (__type)
		{
			case ScritchInputMethodType.MOUSE_MOTION:
			case ScritchInputMethodType.MOUSE_BUTTON_PRESSED:
			case ScritchInputMethodType.TOUCH_FINGER_PRESSED:
			case ScritchInputMethodType.STYLUS_PEN_PRESSED:
			case ScritchInputMethodType.STYLUS_DRAG_MOTION:
			case ScritchInputMethodType.STYLUS_HOVER_MOTION:
				this.localCursor(__c, __d);
				break;
		}
	}
	
	/**
	 * Updates the local cursor.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @since 2026/07/05
	 */
	public void localCursor(int __x, int __y)
	{
		for (Viewport view : this._views)
			if (view.isActive() &&
				__x >= view.screenX && __y >= view.screenY)
				view.localCursor(__x - view.screenX,
					__y - view.screenY);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/06/10
	 */
	@Override
	public void paint(@NotNull ScritchComponentBracket __component,
		@NotNull ScritchPencilBracket __g,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sw,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sh, int __special)
	{
		// Is there an actual rate controller?
		RateController rate = this.rate.get();
		if (rate == null)
			return;
		
		// Get all viewports and map them to the screen if needed
		Viewport[] views = this._views;
		for (int n = views.length, i = 0; i < n; i++)
			views[i].splitIfDirty(__sw, __sh, this._numViewers);
		
		// Pencil graphics is a bit easier to use here
		try (PencilGraphics g = PencilGraphics.of(__g, __sw, __sh))
		{
			// Get the current world map, if there is one
			WorldMap map = rate.worldMap();
			if (map == null)
			{
				// Loading...
				g.setColor(0xFF00FF);
				g.drawString("Loading...", 0, 0, 0);
				
				// Do nothing more
				return;
			}
			
			// Draw each viewport that is valid
			for (int n = views.length, i = 0; i < n; i++)
				views[i].paint(g, __sw, __sh, map);
		}
	}
	
	/**
	 * Returns the viewports.
	 *
	 * @return The viewports.
	 * @since 2026/06/13
	 */
	public Viewport[] views()
	{
		return this._views;
	}
}
