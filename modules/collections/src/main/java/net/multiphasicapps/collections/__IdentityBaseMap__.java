// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.util.AbstractMap;
import java.util.Map;

/**
 * This is the base class for identify hash maps.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 * @since 2021/11/28
 */
abstract class __IdentityBaseMap__<K, V>
	extends AbstractMap<K, V>
{
	/** The backing map. */
	private final Map<Identity<K>, V> _backing;
	
	/**
	 * Initializes the base map.
	 * 
	 * @param __backing The backing map.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/11/28
	 */
	protected __IdentityBaseMap__(Map<Identity<K>, V> __backing)
		throws NullPointerException
	{
		if (__backing == null)
			throw new NullPointerException("NARG");
		
		this._backing = __backing;
	}
}
