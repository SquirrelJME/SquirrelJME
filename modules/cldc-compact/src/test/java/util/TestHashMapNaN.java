// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util;

import net.multiphasicapps.tac.TestSupplier;
import java.util.HashMap;

/**
 * {@link HashMap}s can contain NaN.
 *
 * @since 2018/10/10
 */
public class TestHashMapNaN
	extends TestSupplier<Boolean>
{
	/**
	 * {@inheritDoc}
	 * @since 2018/10/10
	 */
	@Override
	public Boolean test()
	{
		// Initialize a map to work with
		HashMap<Float, String> map = new HashMap<>();
		
		// Store a NaN
		map.put(Float.NaN, "Squirrels are adorable!");
		
		return map.containsKey(Float.NaN);
	}
}

