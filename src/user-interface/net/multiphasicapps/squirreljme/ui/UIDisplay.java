// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ui;

/**
 * This is the base class for displays. Displays contain canvases, widgets,
 * and may provide input events.
 *
 * This is equivalent to the LCD UI {@code Display} class.
 *
 * @since 2016/05/20
 */
public abstract class UIDisplay
{
	/**
	 * Returns {@code true} if this display is visible to the user, but not
	 * necessarily seen by the user.
	 *
	 * In the event that the display is fully obscured by an overlay or perhaps
	 * another running application, the display will still be visible to the
	 * virtual machine itself. If the UI manager does not support multiple
	 * displays visible at the same time, then only a single display will
	 * ever be visible.
	 *
	 * @return {@code true} if the display is visible to the user.
	 * @since 2016/05/20
	 */
	public abstract boolean isVisible();
	
	/**
	 * Sets whether or not this display is visible on the screen.
	 *
	 * In the event that the display manager can only display a single display
	 * at a time and this is the currently visible display, this will have no
	 * effect if visibility is being cleared.
	 *
	 * @param __vis If {@code true} then the display should be made visible to
	 * the user, otherwise it should be hidden.
	 * @return {@code true} if the visibility change was accepted and took
	 * effect or if the display was already in the given visibility state.
	 * @since 2016/05/21
	 */
	public abstract boolean setVisible(boolean __vis);
}

