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
 * This represents something which has a label, long label, or image which can
 * be viewed. This interface does not represent one that can be set unless the
 * interface {@link UiHasSettableLabel} is implemented.
 *
 * @since 2018/04/04
 */
public interface UiHasViewableLabel
	extends UiInterface
{
	/**
	 * Gets the image to be displayed for this item.
	 *
	 * @return The image to be displayed or {@code null} if there is no image.
	 * @since 2018/04/04
	 */
	public abstract UiImage getImage();
	
	/**
	 * Gets the label for this item.
	 *
	 * @return The label to be displayed.
	 * @since 2018/04/04
	 */
	public abstract String getLabel();
	
	/**
	 * Gets the long label for this item, this is one that may be displayed
	 * when there is sufficient room available.
	 *
	 * @return The long label to be displayed, will be {@code null} if none
	 * is set.
	 * @since 2018/04/04
	 */
	public abstract String getLongLabel();
}

