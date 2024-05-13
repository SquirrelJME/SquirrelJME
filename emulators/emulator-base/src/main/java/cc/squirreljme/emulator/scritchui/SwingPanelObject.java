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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

/**
 * Object for Swing panels.
 *
 * @since 2024/03/16
 */
@Deprecated
public class SwingPanelObject
	extends SwingComponentObject
	implements ScritchPanelBracket
{
	/** The backing panel. */
	protected final __InternalPanel__ panel =
		new __InternalPanel__(this);
	
	/** The image to draw onto. */
	volatile BufferedImage _pixelImage;
	
	/**
	 * Initializes the base panel.
	 *
	 * @since 2024/03/19
	 */
	public SwingPanelObject()
	{
		this.panel.setLayout(new BorderLayout());
	}
	
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
	 * Enables or disables focus for this panel.
	 *
	 * @param __enabled If focus should be enabled.
	 * @since 2024/03/24
	 */
	protected void enableFocus(boolean __enabled)
	{
		__InternalPanel__ panel = this.panel;
		
		// Allow this to be focused, so it can have key events within
		// Or do not permit the panel take keys and be focused
		panel.setFocusable(__enabled);
		panel.setRequestFocusEnabled(__enabled);
		panel.setFocusTraversalKeysEnabled(__enabled);
	}
	
	/**
	 * Repaints the panel.
	 *
	 * @since 2024/03/19
	 */
	protected void repaint()
	{
		this.panel.repaint();
	}
	
	/**
	 * Sets the paint listener for the panel.
	 *
	 * @param __listener The listener to use, or {@code null} to clear.
	 * @since 2024/03/19
	 */
	public void setPaintListener(ScritchPaintListener __listener)
	{
		__InternalPanel__ panel = this.panel;
		
		// Debug
		Debugging.debugNote("setPaintListener(%p) <- parent: %p",
			__listener, panel.getParent());
		
		// Store listener for later calls
		panel._listener = __listener;
		
		// Are we setting everything?
		boolean set = (__listener != null);
		
		// We control all the drawn pixels here
		// Or give up control of drawing
		panel.setOpaque(set);
		
		// Redraw it
		panel.repaint();
	}
	
	/**
	 * The internal draw panel.
	 *
	 * @since 2024/03/19
	 */
	private static class __InternalPanel__
		extends JPanel
	{
		/** The owning panel. */
		protected final Reference<SwingPanelObject> panel;
	
		/** The listener to use for paint events. */
		volatile ScritchPaintListener _listener;
		
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
			
			// Do nothing if the panel was GCed
			SwingPanelObject panel = this.panel.get();
			if (panel == null)
				return;
			
			// Do nothing if there is no listener
			ScritchPaintListener listener = this._listener;
			if (listener == null)
				return;
			
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
			throw Debugging.todo();
			/*
			listener.paint(panel, UIPixelFormat.INT_RGB888,
				pW, pH, ((DataBufferInt)pixelImage.getRaster()
					.getDataBuffer()).getData(), 0,
					null, 0, 0, pW, pH, 0);
			
			// Draw the buffer directly onto the panel
			__g.drawImage(pixelImage, 0, 0, pW, pH,
				0, 0, pW, pH, null);
			 */
		}
	}
}
