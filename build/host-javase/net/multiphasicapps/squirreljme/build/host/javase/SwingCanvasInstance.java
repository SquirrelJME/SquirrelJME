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

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.microedition.lcdui.Canvas;
import javax.swing.JPanel;
import net.multiphasicapps.squirreljme.lcdui.DisplayCanvasConnector;

/**
 * This provides an interface for the standard canvas.
 *
 * The drawing operations are adapted directly onto the Swing drawing
 * operations.
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
	
	/** The adapter for drawing. */
	protected final AWTGraphicsAdapter adapter =
		new AWTGraphicsAdapter();
	
	/** The drawing panel. */
	private final __DrawPane__ _panel;
	
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
		
		// Set
		this.canvas = __d;
		this.canvasconnector = __c;
		
		// It is easier to draw directly on a panel
		__DrawPane__ panel = new __DrawPane__();
		this.frame.add(panel);
		this._panel = panel;
	}
	
	/**
	 * This is the drawing panel.
	 *
	 * @since 2017/02/08
	 */
	private final class __DrawPane__
		extends JPanel
	{
		/**
		 * {@inheritDoc}
		 * @since 2017/02/08
		 */
		@Override
		protected void paintComponent(Graphics __g)
		{
			// Must draw the panel itself
			super.paintComponent(__g);
			
			// Cast and adapt
			Graphics2D gfx = (Graphics2D)__g;
			AWTGraphicsAdapter adapter = SwingCanvasInstance.this.adapter;
			adapter._awtgfx = gfx;
			
			// Make it draw into it
			SwingCanvasInstance.this.canvasconnector.paint(adapter);
		}
	}
}

