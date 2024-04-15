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
import cc.squirreljme.jvm.mle.TypeShelf;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * Tests {@link ObjectShelf}.
 *
 * @since 2020/06/22
 */
public class TestObjectShelfInvalid
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
				ObjectShelf.arrayNew(null, -1);
				break;
			
			case 1:
				ObjectShelf.arrayNew(
					TypeShelf.typeOfByte(), 1234);
				break;
			
			case 2:
				ObjectShelf.arrayNew(
					TypeShelf.classToType(byte[].class), -1234);
				break;
			
			case 3:
				ObjectShelf.arrayLength(null);
				break;
			
			case 4:
				ObjectShelf.holdsLock(null, null);
				break;
			
			case 5:
				ObjectShelf.holdsLock(Thread.currentThread(), null);
				break;
			
			case 6:
				ObjectShelf.holdsLock(null, new Object());
				break;
			
			case 7:
				ObjectShelf.identityHashCode(null);
				break;
			
			case 8:
				ObjectShelf.newInstance(null);
				break;
			
			case 9:
				ObjectShelf.newInstance(TypeShelf.classToType(byte[].class));
				break;
			
			case 10:
				ObjectShelf.notify(null, false);
				break;
			
			case 11:
				ObjectShelf.wait(null, -1, -1);
				break;
			
			default:
				return true;
		}
		
		return false;
	}
}
