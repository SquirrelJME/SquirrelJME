// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.image.ImageReaderDispatcher;
import cc.squirreljme.runtime.nttdocomo.ui.DoJa8BitImageLoader;
import cc.squirreljme.runtime.nttdocomo.ui.EightBitImageStore;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Support for generic paletted 8-bit images.
 *
 * @since 2024/01/14
 */
@KeepWhenCompacting
class __8BitImage__
	extends PalettedImage
{
	/** The image that is currently stored here. */
	@SquirrelJMEVendorApi
	volatile EightBitImageStore _store;
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public void changeData(InputStream __in)
		throws NullPointerException, UIException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Parse the given image
		DoJa8BitImageLoader loader = new DoJa8BitImageLoader();
		try
		{
			ImageReaderDispatcher<EightBitImageStore> reader =
				new ImageReaderDispatcher<>(loader);
			
			reader.parse(__in);
		}
		catch (IOException __e)
		{
			UIException toss = new UIException(UIException.UNSUPPORTED_FORMAT);
			toss.initCause(__e);
			throw toss;
		}
		
		// Finish loading in the image to the store
		EightBitImageStore store = (EightBitImageStore)loader.finish();
		
		// This is a bit of a hack, but we need to tell the image store the
		// modification count tracker that is used
		Palette palette = store.getPalette();
		store.setPalette(palette, palette._modCount);
		
		// Use this given store
		this._store = store;
		this._paletteOverride = palette;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public void dispose()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/11
	 */
	@Override
	public int getHeight()
		throws UIException
	{
		EightBitImageStore store = this._store;
		if (store == null)
			throw new UIException(UIException.ILLEGAL_STATE);
		
		return store.getHeight();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/15
	 */
	@Override
	public int getTransparentIndex()
		throws UIException
	{
		EightBitImageStore store = this._store;
		if (store == null)
			throw new UIException(UIException.ILLEGAL_STATE);
		
		// Try the super method first
		try
		{
			return super.getTransparentIndex();
		}
		catch (UIException ignored)
		{
			// Use the color from the store
			return store.getTransparentIndex();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/11
	 */
	@Override
	public int getWidth()
		throws UIException
	{
		EightBitImageStore store = this._store;
		if (store == null)
			throw new UIException(UIException.ILLEGAL_STATE);
		
		return store.getWidth();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/04/10
	 */
	@Override
	public void setPalette(Palette __palette)
		throws IllegalArgumentException, NullPointerException, UIException
	{
		// Let the super call run first
		super.setPalette(__palette);
		
		// Update the palette of the 8-bit image storage
		EightBitImageStore store = this._store;
		if (store != null && __palette != null)
			store.setPalette(__palette, __palette._modCount);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/04/10
	 */
	@Override
	public void setTransparentIndex(int __index)
		throws ArrayIndexOutOfBoundsException, UIException
	{
		// Let the super call run first
		super.setTransparentIndex(__index);
		
		// Update the transparent index of the 8-bit image storage
		EightBitImageStore store = this._store;
		if (store != null)
			store.setTransparentIndex(__index);
	}
}
