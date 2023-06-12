// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Token set cache for a {@link CType} using {@link CTokenSet} as the cache
 * for the values.
 *
 * @since 2023/06/06
 */
final class __CTokenSetCache__
{
	/** The internal cache. */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private static final Reference<List<String>>[] _cache =
		(Reference<List<String>>[])
			((Object)new WeakReference[CTokenSet._VALUES.length]);
	
	/**
	 * Gets the tokens for the given set under the given type.
	 * 
	 * @param __set The set to check.
	 * @param __type The type that is being input, used for generation.
	 * @return The cached or generated tokens.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/06
	 */
	List<String> __get(CTokenSet __set,
		__CAbstractType__ __type)
		throws NullPointerException
	{
		if (__set == null || __type == null)
			throw new NullPointerException("NARG");
		
		List<String> rv;
		
		int dx = __set.ordinal();
		Reference<List<String>>[] cache = this._cache;
		synchronized (this)
		{
			Reference<List<String>> ref = cache[dx];
			if (ref == null || (rv = ref.get()) == null)
			{
				// Build and store
				rv = UnmodifiableList.of(__type.__generateTokens(__set));
				cache[dx] = new WeakReference<>(rv);
			}
		}
		
		return rv;
	}
}
