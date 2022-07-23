// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package mleui;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import cc.squirreljme.runtime.lcdui.mle.UIBackendFactory;
import cc.squirreljme.runtime.lcdui.mle.UIBackendType;
import net.multiphasicapps.tac.TestConsumer;
import net.multiphasicapps.tac.UntestableException;

/**
 * Base for all backend tests.
 *
 * @since 2020/10/09
 */
public abstract class BaseBackend
	extends TestConsumer<String>
{
	/**
	 * Performs the testing of the given backend.
	 * 
	 * @param __backend The backend to test.
	 * @param __display The display to test on.
	 * @throws Throwable On any exception.
	 * @since 2020/10/09
	 */
	public abstract void test(UIBackend __backend,
		UIDisplayBracket __display)
		throws Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/10
	 */
	@Override
	public void test(String __backend)
		throws Throwable
	{
		// Debugging
		/*DebugShelf.verbose(VerboseDebugFlag.ALL);
		DebugShelf.verboseInternalThread(VerboseDebugFlag.ALL);*/
		
		UIBackend backend = BaseBackend.__getBackend(__backend);
		this.test(backend, backend.displays()[0]);
	}
	
	/**
	 * Returns the backend used for tests.
	 * 
	 * @param __backend The backend to get.
	 * @return The resultant backend.
	 * @throws NullPointerException On null arguments.
	 * @throws UntestableException If the backend is not operational.
	 * @since 2020/10/10
	 */
	private static UIBackend __getBackend(String __backend)
		throws NullPointerException, UntestableException
	{
		// Is this supported?
		UIBackendType type = UIBackendType.valueOf(__backend);
		if (!UIBackendFactory.isSupported(type))
			throw new UntestableException(__backend);
		
		// Get instance of it
		UIBackendFactory.setDefault(type);
		return UIBackendFactory.getInstance();
	}
}
