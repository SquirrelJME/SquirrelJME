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
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.lang.ref.Reference;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.swing.JFrame;
import javax.swing.JPanel;
import net.multiphasicapps.squirreljme.lcdui.DisplayHardwareState;
import net.multiphasicapps.squirreljme.lcdui.DisplayHead;
import net.multiphasicapps.squirreljme.lcdui.DisplayOrientation;
import net.multiphasicapps.squirreljme.lcdui.DisplayState;
import net.multiphasicapps.squirreljme.lcdui.gfx.IntArrayGraphics;

/**
 * This is a display head which outputs to Swing.
 *
 * @since 2017/08/19
 */
public class SwingDisplayHead
	extends DisplayHead
{
	/** The frame this uses. */
	protected final JFrame frame =
		new JFrame("SquirrelJME");
	
	/** The client screen panel. */
	protected final __Screen__ screen =
		new __Screen__();
	
	/**
	 * Initializes the display head.
	 *
	 * @since 2017/10/01
	 */
	public SwingDisplayHead()
	{
		// Make the display hardware enabled always
		setHardwareState(DisplayHardwareState.ENABLED);
		
		// Set some default display properties
		JFrame frame = this.frame;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Force minimum size to something more friendly
		frame.setMinimumSize(new Dimension(160, 160));
		frame.setPreferredSize(new Dimension(640, 480));
		
		// Setup screen
		__Screen__ screen = this.screen;
		screen.setMinimumSize(new Dimension(160, 160));
		screen.setPreferredSize(new Dimension(640, 480));
		
		// Screen belongs in the frame
		frame.add(screen);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public int displayPhysicalHeightMillimeters()
	{
		return (int)((displayPhysicalHeightPixels() * 25.4) /
			(double)Toolkit.getDefaultToolkit().getScreenResolution());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public int displayPhysicalHeightPixels()
	{
		return GraphicsEnvironment.getLocalGraphicsEnvironment().
			getDefaultScreenDevice().getDisplayMode().getHeight();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public int displayPhysicalWidthMillimeters()
	{
		return (int)((displayPhysicalWidthPixels() * 25.4) /
			(double)Toolkit.getDefaultToolkit().getScreenResolution());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public int displayPhysicalWidthPixels()
	{
		return GraphicsEnvironment.getLocalGraphicsEnvironment().
			getDefaultScreenDevice().getDisplayMode().getWidth();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/27
	 */
	@Override
	public int displayVirtualHeightPixels()
	{
		return this.screen.getHeight();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/27
	 */
	@Override
	public int displayVirtualWidthPixels()
	{
		return this.screen.getWidth();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/01
	 */
	@Override
	protected void displayStateChanged(DisplayState __old,
		DisplayState __new)
		throws NullPointerException
	{
		if (__old == null || __new == null)
			throw new NullPointerException("NARG");
		
		JFrame frame = this.frame;
		
		// If the new state is in the foreground make it visible
		if (__new == DisplayState.FOREGROUND)
		{
			// Clean it up
			frame.pack();
			frame.setLocationRelativeTo(null);
			
			// Make it visible
			frame.setVisible(true);
		}
		
		// Otherwise hide it
		else
			frame.setVisible(false);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public int fontSizeToPixelSize(int __s)
	{
		// There is really no real known way to get the pixel font size in
		// Swing and it is really really generic.
		// So the pixel sizes are calibrated for my display with the DPI
		// scaled accordingly
		// The pixel sizes of the font are determined to be the distance
		// between the lowest part of the o character in my word processor
		// in pixels, but regardless it should still work for the most
		// part.
		double dpimul = ((double)displayDpi() / 120.0);
		switch (__s)
		{
				// Small, 8 pt
			case Font.SIZE_SMALL:
				return (int)(15.0 * dpimul);
			
				// Large, 16 pt
			case Font.SIZE_LARGE:
				return (int)(23.0 * dpimul);
			
				// Default, 12 pt
			case Font.SIZE_MEDIUM:
			default:
				return (int)(30.0 * dpimul);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/24
	 */
	@Override
	public Graphics graphics()
	{
		return this.screen.lcduiGraphics();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/27
	 */
	@Override
	public void graphicsPainted()
	{
		// Just force a complete repaint of the frame
		this.frame.repaint();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/01
	 */
	@Override
	protected void hardwareStateChanged(DisplayHardwareState __old,
		DisplayHardwareState __new)
		throws NullPointerException
	{
		if (__new == null)
			throw new NullPointerException("NARG");
		
		// The swing interface does not really care about this, so do
		// nothing at all
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/27
	 */
	@Override
	public int headId()
	{
		// Always zero
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public boolean isColor()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/27
	 */
	@Override
	public boolean isNaturalOrientation(DisplayOrientation __o)
		throws NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Is natural if it matches the current one
		return __o == orientation();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public int numColors()
	{
		return 256 * 256 * 256;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/27
	 */
	@Override
	public DisplayOrientation orientation()
	{
		if (displayVirtualWidthPixels() > displayVirtualHeightPixels())
			return DisplayOrientation.LANDSCAPE;
		return DisplayOrientation.PORTRAIT;
	}
	
	/**
	 * This class represents the screen that is currently being displayed to
	 * the user.
	 *
	 * @since 2017/10/27
	 */
	private final class __Screen__
		extends JPanel
		implements ComponentListener
	{
		/** The image to display in the panel. */
		private volatile BufferedImage _image =
			new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		
		/**
		 * Initialize the screen.
		 *
		 * @since 2017/10/27
		 */
		private __Screen__()
		{
			addComponentListener(this);
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
				neww = this.getWidth(),
				newh = this.getHeight();
		
			// Recreate the image if it is larger
			if (neww != oldw || newh != oldh)
				this._image = new BufferedImage(neww, newh,
					BufferedImage.TYPE_INT_RGB);
		
			// Send repaint event
			eventQueue().repaint(headId(), 0, 0, neww, newh);
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
		 * Returns the graphics to draw on this screen.
		 *
		 * @return The graphics to draw on this screen.
		 * @since 2017/10/27
		 */
		public Graphics lcduiGraphics()
		{
			BufferedImage image = this._image;
			return new IntArrayGraphics(
				((DataBufferInt)image.getRaster().getDataBuffer()).
				getData(), image.getWidth(), image.getHeight(), false,
				image.getWidth(), 0);
		}
		
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
			__g.drawImage(this._image, 0, 0, xw, xh,
				0, 0, xw, xh, null);
		}
	}
}

