// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.game;

/**
 * This specifies how a game is to be resumed when an input stream is input.
 *
 * @since 2018/03/19
 */
public enum ResumeMode
{
	/** Treat it as a saved game. */
	SAVED_GAME,
	
	/** Treat it as a replay. */
	REPLAY,
	
	/** End. */
	;
}

