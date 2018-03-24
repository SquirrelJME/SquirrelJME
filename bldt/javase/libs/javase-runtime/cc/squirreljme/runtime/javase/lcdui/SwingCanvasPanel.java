// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.javase.lcdui;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.Rectangle;
import javax.swing.JPanel;

/**
 * This class provides the implementation for canvases.
 *
 * @since 2018/03/24
 */
public class SwingCanvasPanel
	extends JPanel
{
	/** The owning widget. */
	protected final SwingWidget owner;
	
	/** The image to display in the panel. */
	private volatile BufferedImage _image =
		new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
	
	/** First frame being drawn? */
	private boolean _firstframe =
		true;
	
	/**
	 * Initializes the canvas.
	 *
	 * @param __o The owning widget.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/24
	 */
	public SwingCanvasPanel(SwingWidget __o)
		throws NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		this.owner = __o;
		
		// It is rather annoying when canvases are really tiny
		this.setMinimumSize(new Dimension(160, 160));
		this.setPreferredSize(new Dimension(640, 480));
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
		
		throw new todo.TODO();
		/*
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
		this.repaint(0, 0, neww, newh);
		
		// If this is the first frame make sure when stuff is drawn that
		// the image is initialized
		if (this._firstframe)
		{
			this.componentResized(null);
			this._firstframe = false;
		}
		
		// Get image now
		BufferedImage image = this._image;
		int xw = image.getWidth(),
			xh = image.getHeight();
		
		// Have the remote end draw into our buffer as needed
		Rectangle rect = __g.getClipBounds();
		try
		{
			SwingWidget.this.callbacks.displayablePaint(
				SwingWidget.this,
				rect.x, rect.y, rect.width, rect.height,
				new LocalIntegerArray(((DataBufferInt)image.getRaster().
				getDataBuffer()).getData()), null, xw, xh, false, xw, 0);
		}
		
		// Remote end threw some exception, ignore it so that execution
		// can continue
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		
		// Draw the backed buffered image
		__g.drawImage(image, 0, 0, xw, xh,
			0, 0, xw, xh, null);
		*/
	}
}

