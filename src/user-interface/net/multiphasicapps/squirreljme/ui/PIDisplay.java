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

import java.lang.ref.Reference;

/**
 * This is the internal display which in essence acts like a standard window,
 * tab, or single screen to provide an interactive environment for the user.
 *
 * Internal classes are not meant to be used by the end user, but only by
 * the implementation of a display manager.
 *
 * @see UIDisplay
 * @since 2016/05/21
 */
public interface PIDisplay
	extends PIBase, PIContainer, PIIconAndText
{
	/**
	 * Returns the visibility state of the internal display.
	 *
	 * @return {@code true} if the display is visible.
	 * @throws UIException If the visibility state could not be determined.
	 * @since 2016/05/22
	 */
	public abstract boolean isVisible()
		throws UIException;
	
	/**
	 * Sets the menu to be used for the display.
	 *
	 * @param __menu The menu to use, if {@code null} then it is removed.
	 * @throws UIException If the menu could not be set.
	 * @since 2016/05/23
	 */
	public abstract void setMenu(UIMenu __menu)
		throws UIException;
	
	/**
	 * Sets the visibility state of the internal display, it is possible for
	 * this operation to not change the visibility of the display.
	 *
	 * @param __vis If {@code true} then the display should be made visible.
	 * @throws UIException If the visibility could not be changed because of
	 * an error, not if it could not be changed for a normal reason.
	 * @since 2016/05/22
	 */
	public abstract void setVisible(boolean __vis)
		throws UIException;
}

