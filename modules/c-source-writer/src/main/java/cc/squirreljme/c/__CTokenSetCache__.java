// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.util.List;

/**
 * Token set cache for a {@link CType} using {@link CTokenSet} as the cache
 * for the values.
 *
 * @since 2023/06/06
 */
final class __CTokenSetCache__
{
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
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
}
