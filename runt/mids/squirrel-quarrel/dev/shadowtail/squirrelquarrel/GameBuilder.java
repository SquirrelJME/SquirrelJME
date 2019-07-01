// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.squirrelquarrel;

import java.io.InputStream;
import java.io.IOException;

/**
 * This is used to build games which can then be run and such.
 *
 * @since 2019/07/01
 */
public final class GameBuilder
{
	/**
	 * Builds the actual game.
	 *
	 * @return The resulting game.
	 * @since 2019/07/01
	 */
	public final Game build()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Builds and plays back a game from a replay.
	 *
	 * @param __in The input stream.
	 * @return The resulting game.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/01
	 */
	public static final Game fromReplay(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

