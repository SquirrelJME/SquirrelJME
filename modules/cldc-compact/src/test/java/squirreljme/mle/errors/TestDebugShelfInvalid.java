// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle.errors;

import cc.squirreljme.jvm.mle.DebugShelf;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * Tests invalid calls in {@link DebugShelf}.
 *
 * @since 2020/06/22
 */
public class TestDebugShelfInvalid
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
				DebugShelf.getThrowableTrace(null);
				break;
			
			case 1:
				DebugShelf.pointAddress(null);
				break;
			
			case 2:
				DebugShelf.pointClass(null);
				break;
			
			case 3:
				DebugShelf.pointFile(null);
				break;
			
			case 4:
				DebugShelf.pointJavaAddress(null);
				break;
			
			case 5:
				DebugShelf.pointJavaOperation(null);
				break;
			
			case 6:
				DebugShelf.pointLine(null);
				break;
			
			case 7:
				DebugShelf.pointMethodName(null);
				break;
			
			case 8:
				DebugShelf.pointMethodType(null);
				break;
			
			default:
				return true;
		}
		
		return false;
	}
}
