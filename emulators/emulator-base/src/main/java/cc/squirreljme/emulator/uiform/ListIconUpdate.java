// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 * This is used as a callback to update the icon on the list accordingly.
 *
 * @since 2020/11/17
 */
public class ListIconUpdate
	implements Runnable
{
	/** The callback used. */
	protected final UIFormCallback callback;
	
	/** The form to call on. */
	protected final SwingForm form;
	
	/** The item being called on. */
	protected final SwingItemList list;
	
	/** The entry being called on. */
	protected final ListEntry entry;
	
	/** The sub-index to call on. */
	protected final int subIndex;
	
	/** The image dimension. */
	protected final int dimension;
	
	/**
	 * Used to update the icon accordingly.
	 * 
	 * @param __callback The callback to call.
	 * @param __form The form being called on.
	 * @param __swingItemList The
	 * @param __entry The entry of the list.
	 * @param __sub The sub-index.
	 * @param __dim The dimension of the icon.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/17
	 */
	public ListIconUpdate(UIFormCallback __callback, SwingForm __form,
		SwingItemList __swingItemList, ListEntry __entry, int __sub, int __dim)
		throws NullPointerException
	{
		if (__callback == null || __form == null || __swingItemList == null ||
			__entry == null)
			throw new NullPointerException("NARG");
		
		this.callback = __callback;
		this.form = __form;
		this.list = __swingItemList;
		this.entry = __entry;
		this.subIndex = __sub;
		this.dimension = __dim;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/17
	 */
	@Override
	public void run()
	{
		SwingItemList list = this.list;
		int dimension = this.dimension;
		
		// Draw icon into the given pixel buffer
		int[] pixelBuffer = new int[dimension * dimension];
		this.callback.paint(list,
			UIPixelFormat.INT_RGB888, dimension, dimension,
			pixelBuffer,0, null,
			0, 0, dimension, dimension, this.subIndex);
		
		// Load image from the pixels
		BufferedImage bi = new BufferedImage(dimension, dimension,
			BufferedImage.TYPE_INT_RGB);
		bi.setRGB(0, 0, dimension, dimension,
			pixelBuffer, 0, dimension);
		
		// Use this icon for the list, indicate the icon is no longer dirty
		this.entry._icon = new ImageIcon(bi);
		this.entry._dirtyIcon = false;
	}
}
