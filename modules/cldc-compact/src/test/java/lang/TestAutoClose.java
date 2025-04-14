// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that {@link AutoCloseable} with {@code try-with-resources} works
 * properly.
 *
 * @since 2023/07/09
 */
public class TestAutoClose
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2023/07/09
	 */
	@Override
	public void test()
		throws Throwable
	{
		__Closing__ closing = new __Closing__();
		this.secondary("before", closing._closed);
		
		try (__Closing__ bump = closing)
		{
			this.secondary("middle", closing._closed);
		}
		
		this.secondary("after", closing._closed);
	}
	
	/**
	 * Class to test closing with.
	 * 
	 * @since 2023/07/09
	 */
	static class __Closing__
		implements AutoCloseable
	{
		/** Was this closed? */
		volatile boolean _closed;
		
		/**
		 * {@inheritDoc}
		 * @since 2023/07/09
		 */
		@Override
		public void close()
			throws Exception
		{
			this._closed = true;
		}
	}
}
