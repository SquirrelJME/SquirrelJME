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
 * Test char array operations.
 *
 * @since 2020/06/22
 */
public class TestArrayInvalidCharacter
	extends __BaseArrayInvalidTest__<char[]>
{
	/**
	 * {@inheritDoc}
	 * @since 2020/06/22
	 */
	@Override
	protected char[] arrayNew(int __len)
	{
		return new char[__len];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/22
	 */
	@Override
	protected void arrayOperation(char[] __src, int __srcOff,
		char[] __dest, int __destOff, int __len)
	{
		ObjectShelf.arrayCopy(__src, __srcOff, __dest, __destOff, __len);
	}
}
