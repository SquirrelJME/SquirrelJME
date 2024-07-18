// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.constants.UIMetricType;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Display;
import net.multiphasicapps.tac.TestConsumer;
import net.multiphasicapps.tac.UntestableException;

/**
 * Base class for display tests.
 *
 * @since 2020/07/26
 */
public abstract class BaseDisplay
	extends TestConsumer<String>
{
	/**
	 * Tests with the given display.
	 * 
	 * @param __display The display to test on.
	 * @param __param The parameter to use, this is optional.
	 * @throws Throwable On any exception.
	 * @since 2020/07/26
	 */
	public abstract void test(Display __display, String __param)
		throws Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/26
	 */
	@Override
	public final void test(String __param)
		throws Throwable
	{
		UIDisplayBracket[] displays = UIFormShelf.displays();
		
		Debugging.todoNote("BaseDisplay::test() -- Headless check.");
		if (displays == null || displays.length == 0 ||
			0 == UIFormShelf.metric(displays[0],
				UIMetricType.UIFORMS_SUPPORTED))
			throw new UntestableException("Native forms not supported.");
		
		// Forward test
		this.test(Display.getDisplays(0)[0], __param);
	}
}
