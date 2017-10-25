// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui.widget;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * This is the default canvas object which contains a picture and a default
 * implementation.
 *
 * @since 2017/10/25
 */
public class DefaultEmbeddedCanvas
	extends EmbeddedCanvas
{
	/** The backing image. */
	private volatile Image _image =
		Image.createImage(1, 1);
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/25
	 */
	@Override
	public Graphics getGraphics()
	{
		Image image = this._image;
		return image.getGraphics();
	}
}

