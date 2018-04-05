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
 * This represents a client local display which gets displayables shown on it
 * accordingly.
 *
 * @since 2018/04/04
 */
public interface UiDisplay
	extends UiInterface, UiWidget
{
	/**
	 * Returns the current displayable being shown on the display.
	 *
	 * @return The current displayable being shown, returns {@code null} if
	 * there is none.
	 * @since 2018/04/04
	 */
	public abstract UiDisplayable getCurrent();
	
	/**
	 * Gets the display head this is associated with.
	 *
	 * @return The display head this is associated with.
	 * @since 2018/04/05
	 */
	public abstract UiDisplayHead getDisplayHead();
	
	/**
	 * Sets the current displayable to be shown on the display.
	 *
	 * @param __d The displayable to show, {@code null} will clear it.
	 * @since 2018/04/04
	 */
	public abstract void setCurrent(UiDisplayable __d);
}

