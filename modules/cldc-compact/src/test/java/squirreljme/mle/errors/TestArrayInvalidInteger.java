// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle.errors;

import cc.squirreljme.jvm.mle.ObjectShelf;

/**
 * Test int array operations.
 *
 * @since 2020/06/22
 */
public class TestArrayInvalidInteger
	extends __BaseArrayInvalidTest__<int[]>
{
	/**
	 * {@inheritDoc}
	 * @since 2020/06/22
	 */
	@Override
	protected int[] arrayNew(int __len)
	{
		return new int[__len];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/22
	 */
	@Override
	protected void arrayOperation(int[] __src, int __srcOff,
		int[] __dest, int __destOff, int __len)
	{
		ObjectShelf.arrayCopy(__src, __srcOff, __dest, __destOff, __len);
	}
}
