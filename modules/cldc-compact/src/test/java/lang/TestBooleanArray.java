// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import net.multiphasicapps.tac.TestSupplier;

/**
 * Checks that boolean arrays work.
 *
 * @since 2020/07/11
 */
public class TestBooleanArray
	extends TestSupplier<boolean[]>
{
	/** The size of the array. */
	public static final int SIZE =
		37;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/11
	 */
	@Override
	public boolean[] test()
	{
		boolean[] values = new boolean[TestBooleanArray.SIZE];
		
		// Check read
		for (int i = 0; i < TestBooleanArray.SIZE; i++)
			values[i] = ((i & 1) == 0);
		
		// Check write/copy
		boolean[] copy = new boolean[values.length];
		for (int i = 0, n = values.length; i < n; i++)
			copy[i] = values[i];
		
		// Copied values
		this.secondary("copy", copy);
		
		return values;
	}
}
