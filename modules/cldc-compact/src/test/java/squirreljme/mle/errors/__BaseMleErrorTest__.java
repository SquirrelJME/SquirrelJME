// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle.errors;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import net.multiphasicapps.tac.TestInteger;

/**
 * Base class for MLE error testing, to make sure that invalid inputs are
 * properly blocked.
 *
 * @since 2020/06/22
 */
abstract class __BaseMleErrorTest__
	extends TestInteger
{
	/**
	 * Performs the test by index.
	 * 
	 * @param __index The index.
	 * @return If testing should stop.
	 * @throws MLECallError This should be thrown.
	 * @since 2020/06/22
	 */
	public abstract boolean test(int __index)
		throws MLECallError;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/22
	 */
	@Override
	public final int test()
	{
		// Call sub-test methods and make sure they fail
		int callCount = 0;
		int errorCount = 0;
		for (int i = 0;; i++)
		{
			// Perform the call
			try
			{
				callCount++;
				
				// Run the test and stop if this is the end
				if (this.test(i))
				{
					callCount--;
					break;
				}
				
				// Send invalid secondary to flag that something is wrong
				this.secondary("not-thrown-" + i, i);
			}
			
			// Caught exception that we want
			catch (MLECallError ignored)
			{
				errorCount++;
			}
		}
		
		// Report the count
		return errorCount;
	}
}
