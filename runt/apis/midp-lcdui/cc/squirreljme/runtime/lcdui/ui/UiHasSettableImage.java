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
 * This represents something that is able to have an image set for it so that
 * it may be displayed.
 *
 * @since 2018/04/06
 */
public interface UiHasSettableImage
	extends UiHasViewableImage, UiInterface
{
	/**
	 * Sets or clears the image to be displayed.
	 *
	 * @param __i The image to display, {@code null} clears it.
	 * @since 2018/04/04
	 */
	public abstract void setImage(UiImage __i);
}

