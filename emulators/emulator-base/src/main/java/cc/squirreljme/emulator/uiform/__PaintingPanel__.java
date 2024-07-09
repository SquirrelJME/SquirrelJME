// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.brackets.UIDrawableBracket;
import cc.squirreljme.jvm.mle.callbacks.UIDrawableCallback;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import javax.swing.JPanel;

/**
 * Panel used for painting graphics.
 *
 * @since 2020/09/20
 */
@Deprecated
class __PaintingPanel__
	extends JPanel
{
	/** The display to draw on. */
	protected final Reference<SwingDisplay> displayRef;
	
	/** The item to check callbacks on. */
	protected final Reference<SwingItem> itemRef;
	
	/** The pixel image for drawing. */
	private BufferedImage _pixelImage;
	
	/**
	 * Initializes the painting panel, with no linked item.
	 * 
	 * @param __display The display to paint on.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/01/14
	 */
	__PaintingPanel__(SwingDisplay __display)
		throws NullPointerException
	{
		if (__display == null)
			throw new NullPointerException("NARG");
		
		this.displayRef = new WeakReference<>(__display);
		this.itemRef = null;
	}
	
	/**
	 * Initializes the painting panel.
	 * 
	 * @param __item The item owning this.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/20
	 */
	__PaintingPanel__(SwingItem __item)
		throws NullPointerException
	{
		if (__item == null)
			throw new NullPointerException("NARG");
		
		this.displayRef = null;
		this.itemRef = new WeakReference<>(__item);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/20
	 */
	@Override
	protected void paintComponent(Graphics __g)
	{
		// Must always be called to perform other operations
		super.paintComponent(__g);
		
		// We always want to overwrite the alpha values and do no blending
		Graphics2D gt = ((__g instanceof Graphics2D) ? (Graphics2D)__g : null);
		if (gt != null)
			gt.setComposite(AlphaComposite.SrcOver);
		
		// Get the panel size, used for drawing and such
		int pW = this.getWidth();
		int pH = this.getHeight();
		
		// Did the framebuffer need to be recreated?
		BufferedImage pixelImage = this._pixelImage;
		if (pixelImage == null || pW != pixelImage.getWidth() ||
			pH != pixelImage.getHeight())
		{
			// Set base image
			this._pixelImage = (pixelImage = new BufferedImage(pW, pH,
				BufferedImage.TYPE_INT_RGB));
			
			// Fill the entire buffer with nothing
			int[] buffer = ((DataBufferInt)pixelImage.getRaster()
				.getDataBuffer()).getData();
			
			Arrays.fill(buffer, 0xFF_000000);
		}
		
		// Determine which callback is to be called
		UIDrawableCallback callback = null;
		UIDrawableBracket callbackItem = null;
		if (this.itemRef != null)
		{
			SwingItem item = this.itemRef.get();
			if (item != null)
			{
				SwingForm form = item._form;
				if (form != null)
					callback = form.callback();
				
				// Use for the callback
				callbackItem = item;
			}
		}
		
		// Is a display?
		else if (this.displayRef != null)
		{
			SwingDisplay display = this.displayRef.get();
			if (display != null)
			{
				callback = display.callback();
				callbackItem = display;
			}	
		}
		
		// Send to callback
		if (callback != null)
			callback.paint(callbackItem, UIPixelFormat.INT_RGB888,
				pW, pH, ((DataBufferInt)pixelImage.getRaster()
					.getDataBuffer()).getData(), null, 0, 0, pW, pH, 0);
		
		// Draw the buffer directly onto the panel
		__g.drawImage(pixelImage, 0, 0, pW, pH,
			0, 0, pW, pH, null);
	}
}
