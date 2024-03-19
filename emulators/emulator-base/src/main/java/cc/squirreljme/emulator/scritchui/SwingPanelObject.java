// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui;

import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchPaintListener;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Object for Swing panels.
 *
 * @since 2024/03/16
 */
public class SwingPanelObject
	extends SwingComponentObject
	implements ScritchPanelBracket
{
	/** The backing panel. */
	protected final JPanel panel =
		new __InternalPanel__(this);
	
	/** The listener to use for paint events. */
	volatile Reference<ScritchPaintListener> _listener;
	
	/** The image to draw onto. */
	volatile BufferedImage _pixelImage;
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/17
	 */
	@Override
	public JComponent component()
	{
		return this.panel;
	}
	
	/**
	 * Sets the paint listener for the panel.
	 *
	 * @param __listener The listener to use, or {@code null} to clear.
	 * @since 2024/03/19
	 */
	public void setPaintListener(ScritchPaintListener __listener)
	{
		if (__listener != null)
		{
			this._listener = new WeakReference<>(__listener);
			
			// We control all the drawn pixels here
			JPanel panel = this.panel;
			panel.setOpaque(true);
			
			// Allow this to be focused, so it can have key events within
			panel.setFocusable(true);
			panel.setRequestFocusEnabled(true);
			panel.setFocusTraversalKeysEnabled(true);
			
			// Redraw it
			panel.repaint();
		}
		
		// Clear listener
		else
		{
			this._listener = null;
			
			// Give up control of drawing
			JPanel panel = this.panel;
			panel.setOpaque(false);
			
			// Also do not permit the panel take keys and be focused
			panel.setFocusable(false);
			panel.setRequestFocusEnabled(false);
			panel.setFocusTraversalKeysEnabled(false);
			
			// Redraw it
			panel.repaint();
		}
	}
	
	/**
	 * The internal draw panel.
	 *
	 * @since 2024/03/19
	 */
	private static class __InternalPanel__
		extends JPanel
	{
		/** The panel to draw on. */
		protected final WeakReference<SwingPanelObject> panel;
		
		/**
		 * Initializes the base panel.
		 * 
		 * @param __panel The panel to draw on.
		 * @throws NullPointerException On null arguments.
		 * @since 2024/03/19
		 */
		__InternalPanel__(SwingPanelObject __panel)
			throws NullPointerException
		{
			if (__panel == null)
				throw new NullPointerException("NARG");
			
			this.panel = new WeakReference<>(__panel);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2024/03/19
		 */
		@Override
		protected void paintComponent(Graphics __g)
		{
			// Must always be called to perform other operations
			super.paintComponent(__g);
			
			// Debug
			Debugging.debugNote("paintComponent() A");
			
			// Do nothing if the panel was GCed
			SwingPanelObject panel = this.panel.get();
			if (panel == null)
				return;
			
			// Debug
			Debugging.debugNote("paintComponent() B");
			
			// Do nothing if there is no listener
			Reference<ScritchPaintListener> listenerRef = panel._listener;
			if (listenerRef == null)
				return;
			
			// Debug
			Debugging.debugNote("paintComponent() C");
			
			// Also do nothing if it was GCed
			ScritchPaintListener listener = listenerRef.get();
			if (listener == null)
				return;
			
			// Debug
			Debugging.debugNote("paintComponent() D");
			
			// We always want to overwrite the alpha values and do no blending
			Graphics2D gt = ((__g instanceof Graphics2D) ?
				(Graphics2D)__g : null);
			if (gt != null)
				gt.setComposite(AlphaComposite.SrcOver);
			
			// Get the panel size, used for drawing and such
			int pW = this.getWidth();
			int pH = this.getHeight();
			
			// Did the framebuffer need to be recreated?
			BufferedImage pixelImage = panel._pixelImage;
			if (pixelImage == null || pW != pixelImage.getWidth() ||
				pH != pixelImage.getHeight())
			{
				// Set base image
				panel._pixelImage = (pixelImage = new BufferedImage(pW, pH,
					BufferedImage.TYPE_INT_RGB));
				
				// Fill the entire buffer with nothing
				int[] buffer = ((DataBufferInt)pixelImage.getRaster()
					.getDataBuffer()).getData();
				
				Arrays.fill(buffer, 0xFF_000000);
			}
			
			// Send to callback
			listener.paint(panel, UIPixelFormat.INT_RGB888,
				pW, pH, ((DataBufferInt)pixelImage.getRaster()
					.getDataBuffer()).getData(), 0,
					null, 0, 0, pW, pH, 0);
			
			// Draw the buffer directly onto the panel
			__g.drawImage(pixelImage, 0, 0, pW, pH,
				0, 0, pW, pH, null);
		}
	}
}
