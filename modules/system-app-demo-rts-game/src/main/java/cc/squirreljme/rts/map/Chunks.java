// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.map;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Manages multiple {@link Chunks} to create a uniform and optimal map.
 *
 * @see Chunk
 * @since 2026/06/10
 */
public class Chunks
	extends ChunkAddressable
{
	/**
	 * {@inheritDoc}
	 * @since 2026/06/10
	 */
	@Override
	public final long getTx(ChunkLayer __layer, int __tx, int __ty)
		throws IndexOutOfBoundsException, NullPointerException
	{
		throw Debugging.todo();
	}
}
