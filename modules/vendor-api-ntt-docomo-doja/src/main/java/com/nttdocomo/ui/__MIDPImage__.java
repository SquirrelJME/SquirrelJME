// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.nttdocomo.media.AbstractMediaImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import javax.microedition.io.InputConnection;

/**
 * This wraps a MIDP {@link javax.microedition.lcdui.Image} so it can be
 * used for i-mode applications.
 *
 * @see javax.microedition.lcdui.Image
 * @since 2021/12/01
 */
final class __MIDPImage__
	extends AbstractMediaImage
{
	/** DoJa image reference handle. */
	volatile Reference<Image> _dojaImage;
	
	/** The actual loaded image. */
	volatile javax.microedition.lcdui.Image _image;
	
	/**
	 * Initializes the source image.
	 *
	 * @param __source The image source.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/05
	 */
	__MIDPImage__(InputConnection __source)
		throws NullPointerException
	{
		super(__source);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	protected void becomingRealized(InputStream __in, MediaResource __copy)
		throws NullPointerException, UIException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			try
			{
				this._image = javax.microedition.lcdui.Image.createImage(__in);
			}
			catch (IOException __e)
			{
				// Debug, as DoJa applications will drop exceptions
				if (Debugging.ENABLED)
					__e.printStackTrace();
				
				UIException toss = new UIException(
					UIException.UNSUPPORTED_FORMAT);
				
				toss.initCause(__e);
				
				throw toss;
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	protected void becomingDeallocated()
	{
		synchronized (this)
		{
			this._image = null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/12/01
	 */
	@Override
	public int getHeight()
	{
		return this.__midpImage().getHeight();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/12/01
	 */
	@Override
	public Image getImage()
	{
		synchronized (this)
		{
			Reference<Image> ref = this._dojaImage;
			Image result = null;
			
			// Need to recreate the cache?
			if (ref == null || (result = ref.get()) == null)
			{
				result = new __DoJaImage__(this.__midpImage());
				this._dojaImage = new WeakReference<>(result);
			}
			
			// Use the cached image
			return result;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/12/01
	 */
	@Override
	public int getWidth()
	{
		return this.__midpImage().getWidth();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	protected boolean validKey(String __key)
		throws NullPointerException
	{
		return false;
	}
	
	/**
	 * Returns the used MIDP {@link javax.microedition.lcdui.Image}.
	 *
	 * @return The MIDP {@link javax.microedition.lcdui.Image}.
	 * @throws UIException If the image is not in use.
	 * @since 2022/02/14
	 */
	javax.microedition.lcdui.Image __midpImage()
		throws UIException
	{
		synchronized (this)
		{
			// Is the image not loaded?
			javax.microedition.lcdui.Image image = this._image;
			if (image == null)
				throw new UIException(UIException.ILLEGAL_STATE);
			
			return image;
		}
	}
}
