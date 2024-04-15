// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import com.nttdocomo.io.ConnectionException;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;

/**
 * This wraps a MIDP {@link javax.microedition.lcdui.Image} so it can be
 * used for i-mode applications.
 *
 * @see javax.microedition.lcdui.Image
 * @since 2021/12/01
 */
final class __MIDPImage__
	extends Image
	implements MediaImage
{
	/** The URI of the image. */
	final String _uri;
	
	/** The actual loaded image. */
	volatile javax.microedition.lcdui.Image _image;
	
	/** The number of times this has been used. */
	volatile int _useCount;
	
	/**
	 * Initializes the wrapped image.
	 *
	 * @param __uri The image to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/01
	 */
	__MIDPImage__(String __uri)
		throws NullPointerException
	{
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		this._uri = __uri;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/12/01
	 */
	@Override
	public void dispose()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/12/01
	 */
	@Override
	public int getHeight()
	{
		return this.__midpImage().getHeight();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/12/01
	 */
	@Override
	public Image getImage()
	{
		// Always return self as this is one
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/12/01
	 */
	@Override
	public int getWidth()
	{
		return this.__midpImage().getWidth();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/12/01
	 */
	@Override
	public void unuse()
		throws UIException
	{
		synchronized (this)
		{
			int useCount = this._useCount;
			if (useCount <= 0)
				throw new UIException(UIException.ILLEGAL_STATE);
			
			// Destroy the image
			this._useCount = (--useCount);
			if (useCount == 0)
				this._image = null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/12/01
	 */
	@Override
	public void use()
		throws ConnectionException, SecurityException, UIException
	{
		synchronized (this)
		{
			// Count up usage?
			int useCount = this._useCount;
			if (useCount > 0)
			{
				this._useCount = useCount + 1;
				return;
			}
			
			// Load in the image
			try (InputStream in = Connector.openInputStream(this._uri))
			{
				this._image = javax.microedition.lcdui.Image.createImage(in);
			}
			catch (IOException __e)
			{
				UIException toss = new UIException(
					UIException.UNSUPPORTED_FORMAT);
				
				toss.initCause(__e);
				
				throw toss;
			}
			
			// Initial count as it is now loaded
			this._useCount = 1;
		}
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
