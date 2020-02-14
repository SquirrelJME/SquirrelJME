// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.ui;

/**
 * This is implemented by classes that need to perform some actions after
 * a game frame has run.
 *
 * @since 2018/03/19
 */
public interface FrameSync
{
	/**
	 * This is called when the game has finished running the game logic
	 * and is requesting that the game be repainted accordingly.
	 *
	 * @param __framenum The frame which has just finished.
	 * @since 2018/03/19
	 */
	public abstract void frameRepaintRequest(int __framenum);
}

