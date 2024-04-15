// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle.errors;

import cc.squirreljme.jvm.mle.ReferenceShelf;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * Tests {@link ReferenceShelf} for invalid inputs.
 *
 * @since 2020/06/22
 */
public class TestReferenceShelfInvalid
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
				ReferenceShelf.linkGetObject(null);
				break;
			
			case 1:
				ReferenceShelf.linkGetPrev(null);
				break;
			
			case 2:
				ReferenceShelf.linkGetNext(null);
				break;
			
			case 3:
				ReferenceShelf.objectGet(null);
				break;
			
			case 4:
				ReferenceShelf.objectSet(null, null);
				break;
			
			case 5:
				ReferenceShelf.objectSet(null,
					ReferenceShelf.newLink());
				break;
			
			case 6:
				ReferenceShelf.deleteLink(null);
				break;
			
			case 7:
				ReferenceShelf.linkSetPrev(null, null);
				break;
			
			case 8:
				ReferenceShelf.linkSetPrev(null,
					ReferenceShelf.newLink());
				break;
			
			case 9:
				ReferenceShelf.linkSetNext(null, null);
				break;
			
			case 10:
				ReferenceShelf.linkSetNext(null,
					ReferenceShelf.newLink());
				break;
			
			case 11:
				ReferenceShelf.linkSetObject(null,
					new Object());
				break;
			
			default:
				return true;
		}
		
		return false;
	}
}
