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
			
			case 6:
				ReferenceShelf.deleteLink(null);
				break;
			
			case 3:
			case 4:
			case 5:
			case 7:
			case 8:
			case 1:
			case 2:
			case 9:
			case 10:
				throw new MLECallError("GONE");
			
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
