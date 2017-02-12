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

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.microedition.lcdui.Canvas;
import javax.swing.JPanel;
import net.multiphasicapps.squirreljme.lcdui.DisplayCanvasConnector;
import net.multiphasicapps.squirreljme.lcdui.PointerEventType;

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
	
	/** Key press engine. */
	protected final KeyPressEngine keyengine;
	
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
		
		// The panel needs to be made focusable in order to get key events
		panel.setFocusable(true);
		panel.requestFocusInWindow();
		
		// Handle resize events
		panel.addComponentListener(panel);
		panel.addMouseListener(panel);
		panel.addMouseMotionListener(panel);
		
		// Setup key press engine
		KeyPressEngine keyengine = new KeyPressEngine(__c);
		panel.addKeyListener(keyengine);
		this.keyengine = keyengine;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public int getHeight()
	{
		return this._panel.getHeight();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public int getWidth()
	{
		return this._panel.getWidth();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public void repaint(int __x, int __y, int __w, int __h)
	{
		this._panel.repaint(__x, __y, __w, __h);
	}
	
	/**
	 * This is the drawing panel.
	 *
	 * @since 2017/02/08
	 */
	private final class __DrawPane__
		extends JPanel
		implements ComponentListener, MouseListener, MouseMotionListener
	{
		/** Key handler engine. */
		
		
		/** Is the mouse down? */
		private volatile boolean _mousedown;
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/10
		 */
		@Override
		public void componentHidden(ComponentEvent __e)
		{
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2017/02/10
		 */
		@Override
		public void componentMoved(ComponentEvent __e)
		{
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2017/02/10
		 */
		@Override
		public void componentResized(ComponentEvent __e)
		{
			// Report new panel size
			SwingCanvasInstance.this.canvasconnector.
				sizeChanged(getWidth(), getHeight());
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2017/02/10
		 */
		@Override
		public void componentShown(ComponentEvent __e)
		{
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2017/02/12
		 */
		@Override
		public void mouseClicked(MouseEvent __e)
		{
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2017/02/12
		 */
		@Override
		public void mouseDragged(MouseEvent __e)
		{
			// Do not generate drag events if the mouse is not down because
			// another button other than the primary one could be used
			if (!this._mousedown)
				return;
			
			// Report event
			SwingCanvasInstance.this.canvasconnector.pointerEvent(
				PointerEventType.DRAGGED, __e.getX(), __e.getY());
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2017/02/12
		 */
		@Override
		public void mouseEntered(MouseEvent __e)
		{
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2017/02/12
		 */
		@Override
		public void mouseExited(MouseEvent __e)
		{
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2017/02/12
		 */
		@Override
		public void mouseMoved(MouseEvent __e)
		{
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2017/02/12
		 */
		@Override
		public void mousePressed(MouseEvent __e)
		{
			// Only use the primary click
			if (__e.getButton() != MouseEvent.BUTTON1)
				return;
			
			// Report event
			this._mousedown = true;
			SwingCanvasInstance.this.canvasconnector.pointerEvent(
				PointerEventType.PRESSED, __e.getX(), __e.getY());
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2017/02/12
		 */
		@Override
		public void mouseReleased(MouseEvent __e)
		{
			// Only use the primary click
			if (__e.getButton() != MouseEvent.BUTTON1)
				return;
			
			// Report event
			this._mousedown = false;
			SwingCanvasInstance.this.canvasconnector.pointerEvent(
				PointerEventType.RELEASED, __e.getX(), __e.getY());
		}
		
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
			adapter._awt = gfx;
			adapter._width = getWidth();
			adapter._height = getHeight();
			
			// Reset translation
			adapter.translate(-adapter.getTranslateX(),
				-adapter.getTranslateY());
			
			// Translate clipping bounds so the destination shares the clip
			Rectangle rect = __g.getClipBounds();
			if (rect == null)
				adapter.setClip(0, 0, getWidth(), getHeight());
			else
				adapter.setClip(rect.x, rect.y, rect.width, rect.height);
			
			// Make it draw into it
			SwingCanvasInstance.this.canvasconnector.paint(adapter);
		}
	}
}

