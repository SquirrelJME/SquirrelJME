// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle.errors;

import cc.squirreljme.jvm.mle.AtomicShelf;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * Test invalid {@link AtomicShelf} calls.
 *
 * @since 2020/06/22
 */
public class TestAtomicShelfInvalid
	extends __BaseMleErrorTest__
{
	/**
	 * {@inheritDoc}
	 * @since 2020/06/22
	 */
	@Override
	public boolean test(int __index)
		throws MLECallError
	{
		switch (__index)
		{
			case 0:
				AtomicShelf.gcUnlock(1234565789);
				break;
			
			case 1:
				AtomicShelf.spinLock(-12345678);
				break;
			
			default:
				return true;
		}
		
		return false;
	}
}
