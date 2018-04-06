// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.ui;

/**
 * This represents something which has an image associated with it and it can
 * be read.
 *
 * @since 2018/04/06
 */
public interface UiHasViewableImage
	extends UiInterface
{
	/**
	 * Gets the image to be displayed for this item.
	 *
	 * @return The image to be displayed or {@code null} if there is no image.
	 * @since 2018/04/04
	 */
	public abstract UiImage getImage();
}

