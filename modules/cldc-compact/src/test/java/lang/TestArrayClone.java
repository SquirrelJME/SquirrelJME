// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import java.util.Arrays;
import net.multiphasicapps.tac.TestBoolean;

/**
 * This tests that array cloning is correct.
 *
 * @since 2019/12/25
 */
public class TestArrayClone
	extends TestBoolean
{
	/**
	 * {@inheritDoc}
	 * @since 2019/12/25
	 */
	@Override
	public final boolean test()
	{
		// Setup new array
		int[] array = new int[12];
		for (int i = 0; i < 12; i++)
			array[i] = i;
		
		int[] cloned = array.clone();
		return Arrays.equals(array, cloned) && array != cloned;
	}
}

