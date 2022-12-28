// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui;

import cc.squirreljme.runtime.lcdui.mle.UIBackendFactory;
import cc.squirreljme.runtime.lcdui.mle.UIBackendType;
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
	public final void test(String __backendAndTestParam)
		throws Throwable
	{
		// Split possibly
		String backend;
		String testParam;
		int at = __backendAndTestParam.indexOf('@');
		if (at < 0)
		{
			backend = __backendAndTestParam;
			testParam = null;
		}
		else
		{
			backend = __backendAndTestParam.substring(0, at);
			testParam = __backendAndTestParam.substring(at + 1);
		}
		
		// Is this actually supported?
		UIBackendType forceType = UIBackendType.valueOf(backend);
		if (!UIBackendFactory.isSupported(forceType))
			throw new UntestableException("Unsupported type: " + forceType);
		
		// Force the default
		UIBackendFactory.setDefault(forceType);
		
		// Forward test
		this.test(Display.getDisplays(0)[0], testParam);
	}
}
