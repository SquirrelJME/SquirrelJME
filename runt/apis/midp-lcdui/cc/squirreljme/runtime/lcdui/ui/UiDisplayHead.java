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
 * This represents the single display head that is provided for clients to
 * use and share with each other when displaying graphics. Since only a single
 * application may show something at a time, there is {@link UiDisplay} which
 * provides a constant widget that represents the display used
 *
 * @since 2018/04/04
 */
public interface UiDisplayHead
	extends UiWidget
{
	/**
	 * Returns the client display which is currently being shown.
	 *
	 * @return The client display being shown or {@code null} if there is none.
	 * @since 2018/04/04
	 */
	public abstract UiDisplay getCurrentDisplay();
	
	/**
	 * Sets the current display to be shown.
	 *
	 * @param __d The display to show, {@code null} will clear it.
	 * @since 2018/04/04
	 */
	public abstract void setCurrentDisplay(UiDisplay __d);
}

