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
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
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
	 * Returns the blending mode string.
	 *
	 * @param __blendMode The blending mode.
	 * @return The resultant blending mode string.
	 * @since 2025/12/22
	 */
	private static String blendString(int __blendMode)
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
		
		/**
		 * Initializes the viewport.
		 *
		 * @since 2025/12/22
		 */
		public Viewport()
		{
			this.setTitle("Blending Demo");
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2025/12/22
		 */
		@Override
		protected void paint(Graphics __g)
		{
			// Info on the blending mode and such
			__g.drawString(String.format("%d -> %d (Mode %s)",
				this.alphaSrc, this.alphaDst,
					Blending.blendString(this.blendMode)),
				0, 0, 0);
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
