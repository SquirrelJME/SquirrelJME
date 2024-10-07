// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.ScritchChoiceInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchChoiceBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.lcdui.Image;

/**
 * Executes an upsert into a choice.
 *
 * @since 2024/07/25
 */
@SquirrelJMEVendorApi
final class __ExecChoiceUpsert__
	implements Runnable
{
	/** The index to add at. */
	private final int _atIndex;
	
	/** Is this an insertion operation? */
	private final boolean _insert;
	
	/** The ScritchAPI for calling. */
	private final ScritchInterface _scritchApi;
	
	/** The string to set. */
	private final String _string;
	
	/** The widget to modify. */
	private final ScritchChoiceBracket _widget;
	
	/** The image data. */
	private final int[] _imageData;
	
	/** The image width. */
	private final int _imageWidth;
	
	/** The image height. */
	private final int _imageHeight;
	
	/** The return value from insert. */
	volatile int _result;
	
	/** If there was an exception. */
	@SquirrelJMEVendorApi
	volatile MLECallError _error;
	
	/**
	 * Initializes the choice upsertion executor.
	 *
	 * @param __scritchApi The API interface.
	 * @param __widget The choice to modify.
	 * @param __insert Is this being inserted?
	 * @param __atIndex The index to insert at.
	 * @param __str The string to use.
	 * @param __img The image to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/25
	 */
	@SquirrelJMEVendorApi
	__ExecChoiceUpsert__(ScritchInterface __scritchApi,
		ScritchChoiceBracket __widget, boolean __insert,
		int __atIndex, String __str, Image __img)
		throws NullPointerException
	{
		if (__scritchApi == null || __widget == null)
			throw new NullPointerException("NARG");
		
		// Store for later run
		this._scritchApi = __scritchApi;
		this._widget = __widget;
		this._insert = __insert;
		this._atIndex = __atIndex;
		this._string = __str;
		
		// Image being set?
		if (__img != null)
		{
			// Get image details
			int w = __img.getWidth();
			int h = __img.getHeight();
			
			// Initialize target buffer
			int[] data = new int[w * h];
			
			// Read in RGB data
			__img.getRGB(data, 0, w, 0, 0, w, h);
			
			// Store everything
			this._imageData = data;
			this._imageWidth = w;
			this._imageHeight = h;
		}
		
		// No image used
		else
		{
			this._imageData = null;
			this._imageWidth = 0;
			this._imageHeight = 0;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public void run()
	{
		ScritchChoiceInterface choiceApi = this._scritchApi.choice();
		ScritchChoiceBracket widget = this._widget;
		
		try
		{
			// Do we need to insert the item?
			int result = this._atIndex;
			if (this._insert)
				result = choiceApi.choiceInsert(widget, result);
			
			// Set string data
			choiceApi.choiceSetString(widget, result, this._string);
			
			// Set image data?
			if (this._imageData != null)
				choiceApi.choiceSetImage(widget, result, this._imageData, 0,
					this._imageWidth, this._imageWidth, this._imageHeight);
				
			// Make sure result is set 
			this._result = result;
		}
		catch (MLECallError __e)
		{
			__e.printStackTrace(System.err);
			
			this._result = -1;
			this._error = __e;
		}
	}
}
