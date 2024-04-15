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
 * Test byte array operations.
 *
 * @since 2020/06/22
 */
public class TestArrayInvalidByte
	extends __BaseArrayInvalidTest__<byte[]>
{
	/**
	 * {@inheritDoc}
	 * @since 2020/06/22
	 */
	@Override
	protected byte[] arrayNew(int __len)
	{
		return new byte[__len];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/22
	 */
	@Override
	protected void arrayOperation(byte[] __src, int __srcOff, byte[] __dest,
		int __destOff, int __len)
	{
		ObjectShelf.arrayCopy(__src, __srcOff, __dest, __destOff, __len);
	}
}
