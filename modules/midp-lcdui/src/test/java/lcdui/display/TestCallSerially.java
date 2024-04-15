// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui.display;

import javax.microedition.lcdui.Display;
import lcdui.BaseDisplay;
import net.multiphasicapps.tac.OptionalFirstParameter;

/**
 * Tests that serial calls happen properly.
 *
 * @since 2020/10/03
 */
public class TestCallSerially
	extends BaseDisplay
	implements OptionalFirstParameter
{
	/**
	 * {@inheritDoc}
	 * @since 2020/10/03
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	@Override
	public void test(Display __display, String __param)
	{
		__SerialRun__ run = new __SerialRun__();
		
		// This call should run the code then wait for it to be completed
		__display.callSerially(run);
		
		// Then get the result of that, should be true
		for (;;)
			synchronized (run)
			{
				if (!run._flag)
					try
					{
						run.wait(5000);
						continue;
					}
					catch (InterruptedException ignored)
					{
						return;
					}
				
				this.secondary("flagged", run._flag);
				break;
			}
	}
}
