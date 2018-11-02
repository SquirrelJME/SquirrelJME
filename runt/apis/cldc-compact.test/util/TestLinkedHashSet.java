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
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

/**
 * This tests that linked hash set works properly.
 *
 * @since 2018/11/02
 */
public class TestLinkedHashSet
	extends TestSupplier<Integer>
{
	/** Random key used. */
	public static final long KEY =
		0x537175697272656CL;
	
	/** Number of entries to test. */
	public static final int COUNT =
		128;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/02
	 */
	@Override
	public Integer test()
	{
		// The set to test
		Set<Integer> links = new LinkedHashSet<>();
		
		// Add entries to the set
		Random rand = new Random(KEY);
		int intotal = 0;
		for (int i = 0; i < COUNT; i++)
		{
			// Generate and store total value
			int v = rand.nextInt();
			intotal += v;
			
			// Store in the map
			if (!links.add(v))
				this.secondary("duplicatekey", true);
		}
		
		// Mark expected value
		this.secondary("intotal", intotal);
		
		// Go through iterator and match
		rand = new Random(KEY);
		int ittotal = 0;
		for (int i : links)
		{
			if (i != rand.nextInt())
				this.secondary("unmatched", true);
			
			ittotal += i;
		}
		
		return ittotal;
	}
}

