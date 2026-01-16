// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.lcduidemo;

import cc.squirreljme.jvm.mle.constants.PencilBlendingMode;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.lcdui.gfx.ExtraGraphics;
import cc.squirreljme.runtime.lcdui.mle.PencilGraphics;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Shows off the blending modes supported by SquirrelJME.
 *
 * @since 2025/12/22
 */
@SquirrelJMEVendorApi
public class Blending
	extends MIDlet
{
	/** The number of levels to skip at once. */
	public static final int LEVEL_SKIP =
		16;
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/22
	 */
	@Override
	protected void destroyApp(boolean __uc)
		throws MIDletStateChangeException
	{
		// Do nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/22
	 */
	@Override
	protected void startApp()
		throws MIDletStateChangeException
	{
		// Just make a viewport and use it
		Display.getDisplay(this).setCurrent(new Viewport());
	}
	
	/**
	 * Returns the blending mode string.
	 *
	 * @param __blendMode The blending mode.
	 * @return The resultant blending mode string.
	 * @since 2025/12/22
	 */
	public static String blendString(int __blendMode)
	{
		switch (__blendMode)
		{
			case PencilBlendingMode.SRC_OVER:	return "SRC_OVER";
			case PencilBlendingMode.SRC:		return "SRC";
			case PencilBlendingMode.SRC_ATOP:	return "SRC_ATOP";
			case PencilBlendingMode.SRC_IN:		return "SRC_IN";
			case PencilBlendingMode.SRC_OUT:	return "SRC_OUT";
			case PencilBlendingMode.DEST_OVER:	return "DEST_OVER";
			case PencilBlendingMode.DEST:		return "DEST";
			case PencilBlendingMode.DEST_ATOP:	return "DEST_ATOP";
			case PencilBlendingMode.DEST_IN:	return "DEST_IN";
			case PencilBlendingMode.DEST_OUT:	return "DEST_OUT";
			case PencilBlendingMode.CLEAR:		return "CLEAR";
			case PencilBlendingMode.XOR:		return "XOR";
			default:
				return String.format("Unknown %d?", __blendMode);
		}
	}
	
	/**
	 * Clips the value to a pixel color.
	 *
	 * @param __v The value to clip.
	 * @return The clipped value.
	 * @since 2025/12/22
	 */
	public static final int clipPixel(int __v)
	{
		if (__v < 0)
			return 0;
		else if (__v >= 255)
			return 255;
		return __v;
	}
	
	/**
	 * Clips the value to a blending mode.
	 *
	 * @param __v The value to clip.
	 * @return The clipped value.
	 * @since 2025/12/22
	 */
	public static final int clipMode(int __v)
	{
		if (__v < 0 || __v >= PencilBlendingMode.NUM_BLENDS)
			return 0;
		return __v;
	}
	
	/**
	 * The viewport canvas.
	 *
	 * @since 2025/12/22
	 */
	public static final class Viewport
		extends Canvas
	{
		/** Source alpha level. */
		protected volatile int alphaSrc =
			255;
		
		/** Destination alpha level. */
		protected volatile int alphaDst =
			255;
		
		/** The blending mode. */
		protected volatile int blendMode =
			0;
		
		/** The image of Lex. */
		protected final Image lexImage;
		
		/** The base relaxed Lex. */
		protected final int[] lex;
		
		/** The first relaxed Lex. */
		protected final int[] lexSrc;
		
		/** The second relaxed Lex. */
		protected final int[] lexDst;
		
		/**
		 * Initializes the viewport.
		 *
		 * @since 2025/12/22
		 */
		public Viewport()
		{
			this.setTitle("Blending Demo");
			
			// Exit command
			this.addCommand(Exit.command);
			this.setCommandListener(new Exit());
			
			// Load in Lex
			Image lex;
			try (InputStream in = this.getClass().getResourceAsStream(
				"relaxedpixel.xpm"))
			{
				if (in == null)
					throw new NullPointerException("NARG");
				
				// Load him in!
				lex = Image.createImage(in);
			}
			catch (IOException __e)
			{
				throw new RuntimeException(__e);
			}
			
			// Keep the base Lex around, for safekeeping since he is a good boy
			int w = lex.getWidth();
			int h = lex.getHeight();
			int[] base = new int[w * h];
			this.lexImage = lex;
			this.lex = base;
			lex.getRGB(base, 0, w, 0, 0, w, h);
			
			// Make a mutable copy of him, for alpha adjusting
			this.lexSrc = base.clone();
			this.lexDst = base.clone();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2025/12/22
		 */
		@Override
		protected void paint(Graphics __g)
		{
			// Read in state
			int alphaSrc = this.alphaSrc;
			int alphaDst = this.alphaDst;
			int blendMode = this.blendMode;
			
			// Get image data
			Image lexImage = this.lexImage;
			int iw = lexImage.getWidth();
			int ih = lexImage.getHeight();
			int iz = iw * ih;
			
			// Get array basis
			int[] lexBase = this.lex;
			int[] lexSrc = this.lexSrc;
			int[] lexDst = this.lexDst;
			
			// Setup images depending on the state
			for (int i = 0; i < iz; i++)
			{
				// No alpha here?
				int c = lexBase[i];
				if ((c & 0xFF000000) == 0)
				{
					lexSrc[i] = 0;
					lexDst[i] = 0;
				}
				
				// Otherwise, use the given color with specific alpha
				else
				{
					lexSrc[i] = (c & 0xFFFFFF) | (alphaSrc << 24);
					lexDst[i] = (c & 0xFFFFFF) | (alphaDst << 24);
				}
			}
			
			// Draw background
			__g.setBlendingMode(Graphics.SRC_OVER);
			__g.setAlphaColor(0xFF5BCFFB);
			__g.fillRect(0, 0,
				this.getWidth(), this.getHeight());
			
			// Open hardware graphics to draw on top of
			try (PencilGraphics pg = PencilGraphics.hardwareGraphics(
				UIPixelFormat.INT_ARGB8888, iw, ih,
				lexSrc, null, 0, 0, iw, ih))
			{
				// Draw destination image with the given blend mode
				pg.setBlendingModeEx(blendMode);
				pg.drawRGB(lexDst, 0, iw,
					iw / 4, ih / 4, iw, ih, true);
			}
			
			// Draw source image (which is the resultant image)
			__g.drawRGB(lexSrc, 0, iw,
				0, 0, iw, ih, true);
			
			// Generate blending mode string
			String info = String.format("%d -> %d (Mode %s)", alphaSrc,
				alphaDst, Blending.blendString(blendMode));
			
			// Draw background to make it easier to see
			__g.setBlendingMode(0);
			for (int x = 0; x < 2; x++)
				for (int y = 0; y < 2; y++)
				{
					__g.setAlphaColor(0xFF000000);
					__g.drawString(info, x, y, 0);
				}
			
			// Draw info again, in clear colors
			__g.setAlphaColor(0xFFFFFFFF);
			__g.drawString(info, 1, 1, 0);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2025/12/22
		 */
		@SuppressWarnings("NonAtomicOperationOnVolatileField")
		@Override
		protected void keyPressed(int __code)
		{
			switch (this.getGameAction(__code))
			{
				case Canvas.UP:
					this.alphaSrc = Blending.clipPixel(this.alphaSrc +
						Blending.LEVEL_SKIP);
					break;
					
				case Canvas.DOWN:
					this.alphaSrc = Blending.clipPixel(this.alphaSrc -
						Blending.LEVEL_SKIP);
					break;
					
				case Canvas.RIGHT:
					this.alphaDst = Blending.clipPixel(this.alphaDst +
						Blending.LEVEL_SKIP);
					break;
					
				case Canvas.LEFT:
					this.alphaDst = Blending.clipPixel(this.alphaDst -
						Blending.LEVEL_SKIP);
					break;
					
				case Canvas.GAME_A:
					this.blendMode = Blending.clipMode(this.blendMode + 1);
					break;
					
				case Canvas.GAME_B:
					this.blendMode = Blending.clipMode(this.blendMode - 1);
					break;
			}
			
			// Request redraw
			this.repaint();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2025/12/22
		 */
		@Override
		protected void keyRepeated(int __code)
		{
			// Just treat as a press
			this.keyPressed(__code);
		}
	}
}
