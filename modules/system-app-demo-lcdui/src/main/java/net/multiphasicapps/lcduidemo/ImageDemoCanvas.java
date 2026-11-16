// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.lcduidemo;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * The demo canvas which does the drawing.
 *
 * @since 2019/04/15
 */
public final class ImageDemoCanvas
	extends Canvas
{
	/** The image to draw. */
	protected final Image[] images;
	
	/** Which image is being drawn? */
	private volatile int _index;
	
	/**
	 * Initializes the demo canvas.
	 *
	 * @param __i The images to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/15
	 */
	public ImageDemoCanvas(Image[] __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		this.images = __i;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/11/15
	 */
	@Override
	protected void keyPressed(int __code)
	{
		// Which direction is the index moving?
		int index = this._index;
		switch (this.getGameAction(__code))
		{
			case Canvas.GAME_A:
			case Canvas.GAME_C:
			case Canvas.LEFT:
				index -= 1;
				break;
				
			case Canvas.GAME_B:
			case Canvas.GAME_D:
			case Canvas.RIGHT:
				index += 1;
				break;
		}
		
		// Clamp the index to the number of available images
		this._index = Math.max(0, 
			Math.min(index, this.images.length - 1));
		
		// Always repaint the canvas to get the next image
		this.repaint();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/11/15
	 */
	@Override
	protected void keyRepeated(int __code)
	{
		// Same as pressing a key
		this.keyPressed(__code);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2018/11/22
	 */
	@Override
	public void paint(Graphics __g)
	{
		// Get image
		Image image = this.images[this._index];
		int w = image.getWidth(), h = image.getHeight();
		
		// Base Y position
		int basey = 0;
		
		// Draw portions of the image
		for (int i = 0; i < 4; i++)
		{
			// Determine x and y offset
			int xoff = (((i & 0b01) == 0) ? 0 : w >>> 1), yoff = (
				((i & 0b10) == 0) ? 0 : h >>> 1);
			
			// Source X and Y dimensions
			int sx = xoff, sy = yoff, sw = w - xoff, sh = h - yoff;
			
			// Non-mirrored rotations
			for (int j = 0; j < AbstractImageDemo._NO_MIRROR.length; j++)
				__g.drawRegion(image, sx, sy, sw, sh,
					AbstractImageDemo._NO_MIRROR[j], (w * j), basey, 0);
			basey += h;
			
			// Mirrored rotations
			for (int j = 0; j < AbstractImageDemo._MIRROR.length; j++)
				__g.drawRegion(image, sx, sy, sw, sh,
					AbstractImageDemo._MIRROR[j], (w * j), basey, 0);
			basey += h;
		}
		
		// Plain image drawing
		__g.drawImage(image, 0, basey, Graphics.TOP | Graphics.LEFT);
		basey += h;
		
		// Big Lex
		__g.drawRegion(image, 0, 0, w, h, 0, 0, basey, 0, w * 2, h * 2);
		basey += (h * 2);
		
		// Little Lex
		__g.drawRegion(image, 0, 0, w, h, 0, 0, basey, 0, w / 2, h / 2);
		basey += h / 2;
		
		// Thicc Lex
		__g.drawRegion(image, 0, 0, w, h, 0, 0, basey, 0, w * 4, h / 2);
		basey += h / 2;
		
		// Back to top
		int basex = (w * 4) + (w / 2);
		basey = 0;
		
		// Half of a lex, width
		__g.drawRegion(image, w / 2, 0, w / 2, h, 0, basex, basey, 0);
		basey += h;
		
		// Half of a lex, Height
		__g.drawRegion(image, 0, h / 2, w, h / 2, 0, basex, basey, 0);
		basey += h;
	}
}
