// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.lis.realm;

/**
 * An object which is cached in memory.
 *
 * @param <T> The type of object to cache.
 * @since 2025/12/21
 */
public interface MemoryObject<T extends MemoryObject<T>>
	extends AutoCloseable
{
	/**
	 * {@inheritDoc}
	 * @since 2025/12/21
	 */
	@Override
	void close();
	
	/**
	 * Resets this memory object to a zero state.
	 *
	 * @since 2025/12/21
	 */
	void reset();
}
