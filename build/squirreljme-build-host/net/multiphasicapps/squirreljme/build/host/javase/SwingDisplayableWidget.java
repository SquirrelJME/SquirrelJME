// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.host.javase;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.Rectangle;
import java.lang.ref.Reference;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.swing.JPanel;
import net.multiphasicapps.squirreljme.lcdui.gfx.PixelArrayGraphics;
import net.multiphasicapps.squirreljme.lcdui.widget.DisplayableWidget;

/**
 * This represents widgtes that swing uses to display embedded objects.
 *
 * @since 2017/10/25
 */
public class SwingDisplayableWidget
	extends DisplayableWidget
	implements ComponentListener
{
	/** The panel where widgets are displayed in. */
	final __DrawPane__ _panel =
		new __DrawPane__();
	
	/** The image to display in the panel. */
	private volatile BufferedImage _image =
		new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
	
	/**
	 * Initializes the widget.
	 *
	 * @param __rd The reference to the displayable.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/25
	 */
	public SwingDisplayableWidget(Reference<Displayable> __rd)
		throws NullPointerException
	{
		super(__rd);
		
		// Force these properties on panels
		__DrawPane__ panel = this._panel;
		panel.setMinimumSize(new Dimension(160, 160));
		
		// The panel needs to be made focusable in order to get key events
		panel.setFocusable(true);
		panel.requestFocusInWindow();
		
		// Add needed events
		panel.addComponentListener(this);
	}
	
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
		BufferedImage image = this._image;
		int oldw = image.getWidth(),
			oldh = image.getHeight(),
			neww = this._panel.getWidth(),
			newh = this._panel.getHeight();
		
		// Recreate the image if it is larger
		if (neww > oldw || newh > oldh)
			this._image = new BufferedImage(Math.max(oldw, neww),
				Math.max(oldh, newh), BufferedImage.TYPE_INT_RGB);
		
		// Send repaint event
		eventQueue().repaintCanvas(
			this.<Canvas>displayable(Canvas.class), 0, 0, neww, newh);
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
	 * @since 2017/10/25
	 */
	@Override
	public Graphics getGraphics()
	{
		return new PixelArrayGraphics(
			((DataBufferInt)this._image.getRaster().getDataBuffer()).
			getData(), getWidth(), getHeight(), false);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/25
	 */
	@Override
	public int getHeight()
	{
		return this._panel.getHeight();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/25
	 */
	@Override
	public int getWidth()
	{
		return this._panel.getWidth();
	}
	
	/**
	 * This is the drawing panel.
	 *
	 * @since 2017/02/08
	 */
	private final class __DrawPane__
		extends JPanel
	{
		/** The wrapped buffered image
		private volatile BufferedImage _bi;
		
		/**
		 * {@inheritDoc}
		 * @since 2017/10/25
		 */
		@Override
		protected void paintComponent(java.awt.Graphics __g)
		{
			// This must always be called
			super.paintComponent(__g);
			
			// Draw the backed buffered image
			int xw = getWidth(),
				xh = getHeight();
			__g.drawImage(SwingDisplayableWidget.this._image, 0, 0, xw, xh,
				0, 0, xw, xh, null);
		}
	}
}

