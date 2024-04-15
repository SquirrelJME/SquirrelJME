// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle.errors;

import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.jvm.mle.brackets.TracePointBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * Tests that {@link ThreadShelf} invalid parameters fail.
 *
 * @since 2020/06/22
 */
public class TestThreadShelfInvalid
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
				ThreadShelf.vmThreadIsMain(null);
				break;
			
			case 1:
				ThreadShelf.javaThreadIsStarted(null);
				break;
			
			case 2:
				ThreadShelf.javaThreadSetAlive(null, false);
				break;
			
			case 3:
				ThreadShelf.createVMThread(null, null);
				break;
			
			case 4:
				ThreadShelf.javaThreadClearInterrupt(null);
				break;
			
			case 5:
				ThreadShelf.javaThreadFlagStarted(null);
				break;
			
			case 6:
				ThreadShelf.javaThreadRunnable(null);
				break;
			
			case 7:
				ThreadShelf.toJavaThread(null);
				break;
			
			case 8:
				ThreadShelf.toVMThread(null);
				break;
			
			case 9:
				ThreadShelf.vmThreadId(null);
				break;
			
			case 10:
				ThreadShelf.vmThreadInterrupt(null);
				break;
			
			case 11:
				ThreadShelf.vmThreadSetPriority(null, 
					Thread.MIN_PRIORITY - 1);
				break;
			
			case 12:
				ThreadShelf.vmThreadStart(null);
				break;
			
			case 13:
				ThreadShelf.waitForUpdate(-1234);
				break;
			
			case 14:
				ThreadShelf.sleep(-1234, 0);
				break;
			
			case 15:
				ThreadShelf.sleep(0, -1234);
				break;
			
			case 16:
				ThreadShelf.sleep(0, Integer.MAX_VALUE);
				break;
			
			case 17:
				ThreadShelf.vmThreadSetPriority(null, 
					Thread.MAX_PRIORITY + 1);
				break;
			
			case 18:
				ThreadShelf.setTrace(null, null);
				break;
			
			case 19:
				ThreadShelf.setTrace("message", null);
				break;
			
			case 20:
				ThreadShelf.setTrace(null,
					new TracePointBracket[0]);
				break;
			
			case 21:
				ThreadShelf.setTrace("message",
					new TracePointBracket[]{null});
				break;
			
			default:
				return true;
		}
		
		return false;
	}
}
