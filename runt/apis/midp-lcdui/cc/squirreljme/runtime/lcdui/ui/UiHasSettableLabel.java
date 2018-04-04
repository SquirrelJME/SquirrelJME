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
 * This class represents anything which has a label associated with it, it
 * must contain a standard label which represents the short label but can
 * additionally have a long label or image.
 *
 * @since 2018/04/04
 */
public interface UiHasSettableLabel
	extends UiHasViewableLabel
{
	/**
	 * Sets or clears the image to be displayed.
	 *
	 * @param __i The image to display, {@code null} clears it.
	 * @since 2018/04/04
	 */
	public abstract void setImage(UiImage __i);
	
	/**
	 * Set the label to be displayed.
	 *
	 * @param __s The text to use for the label.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/04
	 */
	public abstract void setLabel(String __s)
		throws NullPointerException;
	
	/**
	 * Sets or clears the long label for an item.
	 *
	 * @param __s The long label to display, {@code null} clears it.
	 * @since 2018/04/04
	 */
	public abstract void setLongLabel(String __s);
}

