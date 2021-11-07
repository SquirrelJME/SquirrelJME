// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat.base;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.io.ChunkFuture;

/**
 * The future size for a chunk.
 *
 * @since 2021/09/06
 */
class __TocFutureCount__
	implements ChunkFuture
{
	/** The writer used. */
	private final TableOfContentsWriter writer;
	
	/**
	 * Initializes the future counter.
	 * 
	 * @param __writer The writer user.
	 * @since 2021/11/06
	 */
	__TocFutureCount__(TableOfContentsWriter __writer)
	{
		this.writer = __writer;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/09/06
	 */
	@Override
	public int get()
	{
		Debugging.debugNote("FUTURE Count is %d!",
			this.writer.currentCount());
		return this.writer.currentCount();
	}
}
