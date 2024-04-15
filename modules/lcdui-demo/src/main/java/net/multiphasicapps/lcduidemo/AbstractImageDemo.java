// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.lcduidemo;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * This is the base for image demos.
 *
 * @since 2019/04/15
 */
public abstract class AbstractImageDemo
	extends MIDlet
{
	/** No mirrors. */
	static final int[] _NO_MIRROR =
		new int[]{Sprite.TRANS_NONE, Sprite.TRANS_ROT90,
			Sprite.TRANS_ROT180, Sprite.TRANS_ROT270};
			
	/** Mirrors. */
	static final int[] _MIRROR =
		new int[]{Sprite.TRANS_MIRROR, Sprite.TRANS_MIRROR_ROT90,
			Sprite.TRANS_MIRROR_ROT180, Sprite.TRANS_MIRROR_ROT270};
	
	/** The image to use. */
	protected final Image image;
	
	/**
	 * Initializes the image demo.
	 *
	 * @param __in Input stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/15
	 */
	public AbstractImageDemo(InputStream __in)
		throws NullPointerException
	{
		// Load image data
		try
		{
			this.image = Image.createImage(__in);
		}
		
		// Fail
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	protected void destroyApp(boolean __uc)
		throws MIDletStateChangeException
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	protected void startApp()
		throws MIDletStateChangeException
	{
		// Setup canvas
		DemoCanvas cv = new DemoCanvas(this.image);
		cv.setTitle("Image Demo");
		
		// Exit command
		cv.addCommand(Exit.command);
		cv.setCommandListener(new Exit());
		
		// We do not draw every pixel
		cv.setPaintMode(false);
		
		// Set display to the canvas
		Display.getDisplay(this).setCurrent(cv);
	}
	
	/**
	 * The demo canvas which does the drawing.
	 *
	 * @since 2019/04/15
	 */
	public static final class DemoCanvas
		extends Canvas
	{
		/** The image to draw. */
		protected final Image image;
		
		/**
		 * Initializes the demo canvas.
		 *
		 * @param __i The image to use.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/04/15
		 */
		public DemoCanvas(Image __i)
			throws NullPointerException
		{
			if (__i == null)
				throw new NullPointerException("NARG");
			
			this.image = __i;
			
			// We do not draw over every pixel
			this.setPaintMode(false);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/22
		 */
		@Override
		public void paint(Graphics __g)
		{
			// Get image
			Image image = this.image;
			int w = image.getWidth(),
				h = image.getHeight();
			
			// Base Y position
			int basey = 0;
			
			// Draw portions of the image
			for (int i = 0; i < 4; i++)
			{
				// Determine x and y offset
				int xoff = (((i & 0b01) == 0) ? 0 : w >>> 1),
					yoff = (((i & 0b10) == 0) ? 0 : h >>> 1);
				
				// Source X and Y dimensions
				int sx = xoff,
					sy = yoff,
					sw = w - xoff,
					sh = h - yoff;
				
				// Non-mirrored rotations
				for (int j = 0; j < AbstractImageDemo._NO_MIRROR.length; j++)
					__g.drawRegion(image, sx, sy, sw, sh,
						AbstractImageDemo._NO_MIRROR[j],
						(w * j), basey, 0);
				basey += h;
					
				// Mirrored rotations
				for (int j = 0; j < AbstractImageDemo._MIRROR.length; j++)
					__g.drawRegion(image, sx, sy, sw, sh,
						AbstractImageDemo._MIRROR[j],
						(w * j), basey, 0);
				basey += h;
			}
			
			// Plain image drawing
			__g.drawImage(image, 0, basey,
				Graphics.TOP | Graphics.LEFT);
		}
	}
}

