// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This interface is used to provide an interface that the translation engine
 * can use to interact directly with the JIT translator for the current method.
 *
 * @since 2017/02/16
 */
public interface JITStateAccessor
{
	/**
	 * Returns the currently active cache state.
	 *
	 * @return The currently active cache state.
	 * @since 2017/02/19
	 */
	public abstract ActiveCacheState activeCacheState();
	
	/**
	 * Returns the state of cached variables across the entire program.
	 *
	 * @return The cache states, or {@code null} if they are not yet available.
	 * @since 2017/02/16
	 */
	public abstract CacheStates cacheStates();
}

