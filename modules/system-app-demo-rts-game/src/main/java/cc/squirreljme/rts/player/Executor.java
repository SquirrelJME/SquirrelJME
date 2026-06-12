// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.player;

/**
 * This is the base class for all player types.
 *
 * @since 2026/06/12
 */
public abstract class Executor
{
	/** The maximum number of executors permitted within a single game. */
	public static final byte MAX_EXECUTORS =
		12;
}
