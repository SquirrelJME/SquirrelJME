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
 * Tests that suppressed exceptions work properly.
 *
 * @since 2023/07/09
 */
public class TestAutoCloseSuppressed
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2023/07/09
	 */
	@SuppressWarnings({"ConstantValue"})
	@Override
	public void test()
	{
		try
		{
			try (__Closing__ closing = new __Closing__())
			{
				// Fails here
				if (true)
					throw new IllegalArgumentException();
			}
		}
		catch (Throwable __t)
		{
			this.secondary("thrown", __t);
			
			Throwable[] suppressed = __t.getSuppressed();
			this.secondary("has-suppressed",
				suppressed != null && suppressed.length > 0);
			
			if (suppressed != null && suppressed.length > 0)
			{
				this.secondary("suppressed-len", suppressed.length);
				this.secondary("suppressed-first", suppressed[0]);
			}
		}
	}
	
	/**
	 * Class to test closing with.
	 * 
	 * @since 2023/07/09
	 */
	static class __Closing__
		implements AutoCloseable
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/07/09
		 */
		@Override
		public void close()
			throws Exception
		{
			throw new IllegalStateException();
		}
	}
}
