// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.descriptors;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This represents a cache for strings to symbol types.
 *
 * This is used by all sumbol types so that they share a single cache of
 * symbols at a cost of speed for less memory consumption.
 *
 * @param <S> The symbol type.
 * @since 2016/05/18
 */
final class __Cache__<S extends __BaseSymbol__>
{
	/** The class type used. */
	protected final Class<S> type;
	
	/** The creator for making new types. */
	protected final __Create__<S> create;
	
	/** The cache. */
	protected final Map<String, Reference<S>> cache =
		new WeakHashMap<>();
	
	/**
	 * Initializes the cache.
	 *
	 * @param __cl The class type to return.
	 * @param __cr The creator for the symbols.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/18
	 */
	__Cache__(Class<S> __cl, __Create__<S> __cr)
		throws NullPointerException
	{
		// Check
		if (__cl == null || __cr == null)
			throw new NullPointerException("NARG");
		
		// Set
		type = __cl;
		create = __cr;
	}
	
	/**
	 * Locates the symbol associated with the given string.
	 *
	 * @param __s The string to locate in the cache.
	 * @return The found symbol.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/18
	 */
	final S __of(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * This interface is used by the static symbol data for creating symbols
	 * of the given symbol.
	 *
	 * @param <S> The symbol type.
	 * @since 2016/05/18
	 */
	static interface __Create__<S extends __BaseSymbol__>
	{
		public abstract S create(String __s);
	}
}

