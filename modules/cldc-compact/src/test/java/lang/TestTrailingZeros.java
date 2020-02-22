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
 * Tests trailing zero count.
 *
 * @since 2018/11/11
 */
public class TestTrailingZeros
	extends TestSupplier<Integer>
{
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public Integer test()
	{
		int rv = Integer.numberOfTrailingZeros(0);
		this.secondary("zero", rv);
		
		for (int i = 0; i < 31; i++)
		{
			int v = Integer.numberOfTrailingZeros(1 << i);
			
			this.secondary("b" + i, v);
			
			rv += v;
		}
		
		for (int i = 0; i < 31; i++)
		{
			int v = Integer.numberOfTrailingZeros(0xDEADBEEF << i);
			
			this.secondary("d" + i, v);
			
			rv += v;
		}
		
		return rv;
	}
}

