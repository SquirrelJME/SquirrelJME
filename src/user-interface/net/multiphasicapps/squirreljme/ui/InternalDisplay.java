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
 * This is the internal display which in essence acts like a standard window,
 * tab, or single screen to provide an interactive environment for the user.
 *
 * Internal classes are not meant to be used by the end user, but only by
 * the implementation of a display manager.
 *
 * @see UIDisplay
 * @since 2016/05/21
 */
public abstract class InternalDisplay
{
	/**
	 * Initializes the internal display.
	 *
	 * @since 2016/05/21
	 */
	public InternalDisplay()
	{
		throw new Error("TODO");
	}
}

