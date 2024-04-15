// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.util;

import net.multiphasicapps.squirrelquarrel.game.EventSource;

/**
 * This is an event source which reads actions from replay data.
 *
 * @since 2019/03/24
 */
public final class ReplayEventSource
	implements EventSource
{
	/** The input replay stream. */
	protected final ReplayInputStream in;
	
	/**
	 * Initializes the event source.
	 *
	 * @param __in The input stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/24
	 */
	public ReplayEventSource(ReplayInputStream __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this.in = __in;
	}
}

