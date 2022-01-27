// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.debug.Debugging;

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
	/** The image to be shown. */
	final javax.microedition.lcdui.Image image;
	
	/**
	 * Initializes the wrapped image.
	 * 
	 * @param __image The image to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/01
	 */
	__MIDPImage__(javax.microedition.lcdui.Image __image)
		throws NullPointerException
	{
		if (__image == null)
			throw new NullPointerException("NARG");
		
		this.image = __image;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/12/01
	 */
	@Override
	public void dispose()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/12/01
	 */
	@Override
	public int getHeight()
	{
		return this.image.getHeight();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/12/01
	 */
	@Override
	public Image getImage()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/12/01
	 */
	@Override
	public int getWidth()
	{
		return this.image.getWidth();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/12/01
	 */
	@Override
	public void unuse()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/12/01
	 */
	@Override
	public void use()
	{
		throw Debugging.todo();
	}
}
