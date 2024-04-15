// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle.errors;

import cc.squirreljme.jvm.mle.ObjectShelf;

/**
 * Test short array operations.
 *
 * @since 2020/06/22
 */
public class TestArrayInvalidShort
	extends __BaseArrayInvalidTest__<short[]>
{
	/**
	 * {@inheritDoc}
	 * @since 2020/06/22
	 */
	@Override
	protected short[] arrayNew(int __len)
	{
		return new short[__len];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/22
	 */
	@Override
	protected void arrayOperation(short[] __src, int __srcOff,
		short[] __dest, int __destOff, int __len)
	{
		ObjectShelf.arrayCopy(__src, __srcOff, __dest, __destOff, __len);
	}
}
