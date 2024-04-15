// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle.errors;

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.constants.VMDescriptionType;
import cc.squirreljme.jvm.mle.constants.VMStatisticType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * Tests {@link RuntimeShelf} for invalid inputs.
 *
 * @since 2020/06/22
 */
public class TestRuntimeShelfInvalid
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
				RuntimeShelf.vmDescription(-1234);
				break;
			
			case 1:
				RuntimeShelf.vmDescription(VMDescriptionType.NUM_TYPES);
				break;
			
			case 2:
				RuntimeShelf.vmStatistic(-1234);
				break;
			
			case 3:
				RuntimeShelf.vmStatistic(VMStatisticType.NUM_STATISTICS);
				break;
			
			case 4:
				RuntimeShelf.systemProperty(null);
				break;
			
			default:
				return true;
		}
		
		return false;
	}
}
