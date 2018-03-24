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

import cc.squirreljme.runtime.cldc.system.type.Array;
import cc.squirreljme.runtime.cldc.system.type.IntegerArray;
import cc.squirreljme.runtime.lcdui.gfx.PixelFormat;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
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
	
	/** The buffered image type. */
	
	/** The image to display in the panel. */
	private volatile BufferedImage _image;
	
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
		
		// Setup basic image
		this._image = ColorInfo.create(1, 1);
		
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
		
		BufferedImage image = this._image;
		int oldw = image.getWidth(),
			oldh = image.getHeight(),
			xw = this.getWidth(),
			xh = this.getHeight();
	
		// Recreate the image if the size has changed
		if (xw != oldw || xh != oldh)
			this._image = (image = ColorInfo.create(xw, xh));
		
		// Have the remote end draw into our buffer as needed
		Rectangle rect = __g.getClipBounds();
		try
		{
			this.owner.callbackPaint(PixelFormat.INT_RGB888,
				rect.x, rect.y, rect.width, rect.height,
				ColorInfo.getArray(image), ColorInfo.getPalette(image),
				xw, xh, false, xw, 0);
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
	}
}

