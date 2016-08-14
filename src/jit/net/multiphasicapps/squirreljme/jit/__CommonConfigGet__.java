// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;

/**
 * Common getters for the mutable and immutable configuration.
 *
 * @since 2016/07/06
 */
interface __CommonConfigGet__
{
	/**
	 * Returns the cache creator to use when writing to the disk cache.
	 *
	 * @return The cache creator, may return {@code null} if one is not set.
	 * @since 2016/07/06
	 */
	public abstract JITCacheCreator cacheCreator();
	
	/**
	 * Returns all of the static call rewrites that exist within the JIT.
	 *
	 * @return The rewrites used in the configuration.
	 * @since 2016/08/07
	 */
	public abstract JITClassNameRewrite[] classNameRewrites();
	
	/**
	 * Returns the triplet to target.
	 *
	 * @return The target triplet.
	 * @since 2016/07/06
	 */
	public abstract JITTriplet triplet();
}

