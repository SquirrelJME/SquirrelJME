// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import net.multiphasicapps.tac.TestSupplier;

/**
 * Tests allocation of a multi-dimensional array.
 *
 * @since 2018/11/03
 */
public class TestMultiANewArray
	extends TestSupplier<String>
{
	/**
	 * {@inheritDoc}
	 * @since 2018/11/03
	 */
	@Override
	public String test()
	{
		// Allocate array
		Integer[][][] array = new Integer[6][7][4];
		
		// Determine the number of elements
		int total = 0;
		for (int i = 0, in = array.length; i < in; i++)
		{
			// Down to next level
			Integer[][] subi = array[i];
			for (int j = 0, jn = subi.length; j < jn; j++)
				total += subi[j].length;
		}
		
		// Is this time
		this.secondary("total", total);
		
		// Use the name of it
		return array.getClass().getName();
	}
}

