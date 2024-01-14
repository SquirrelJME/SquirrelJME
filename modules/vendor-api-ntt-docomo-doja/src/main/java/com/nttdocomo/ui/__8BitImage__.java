// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.image.ImageReaderDispatcher;
import cc.squirreljme.runtime.nttdocomo.ui.DoJa8BitImageLoader;
import cc.squirreljme.runtime.nttdocomo.ui.EightBitImageStore;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Support for 8-bit image.
 *
 * @since 2024/01/14
 */
class __8BitImage__
	extends PalettedImage
{
	/** The image that is currently stored here. */
	volatile EightBitImageStore _store;
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public void changeData(byte[] __data)
		throws NullPointerException, UIException
	{
		// Forward to stream based handler
		try (InputStream in = new ByteArrayInputStream(__data))
		{
			this.changeData(in);
		}
		catch (IOException __e)
		{
			UIException toss = new UIException(UIException.ILLEGAL_STATE);
			toss.initCause(__e);
			throw toss;
		}
	}
	
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
		this._store = (EightBitImageStore)loader.finish();
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
}
