// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.ui;

import net.multiphasicapps.squirrelquarrel.MainThread;

/**
 * This is the base implementation of the game user interface.
 *
 * @since 2016/09/07
 */
public abstract class GameUI
{
	/**
	 * Renders the game.
	 *
	 * @param __mt The main game thread.
	 * @since 2016/09/11
	 */
	public abstract void render(MainThread __mt);
}

