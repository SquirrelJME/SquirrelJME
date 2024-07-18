// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package mleui;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.constants.UIMetricType;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import cc.squirreljme.runtime.lcdui.mle.headless.HeadlessAttachment;
import cc.squirreljme.runtime.lcdui.mle.pure.NativeUIBackend;
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
		switch (__backend)
		{
			case "NATIVE":
				UIDisplayBracket[] displays = UIFormShelf.displays();
				if (displays != null && displays.length > 0 &&
					0 != UIFormShelf.metric(displays[0],
						UIMetricType.UIFORMS_SUPPORTED))
					return new NativeUIBackend();
				throw new UntestableException(__backend);
			
			case "FRAMEBUFFER":
				return new FBUIBackend(new HeadlessAttachment(
					UIPixelFormat.INT_RGB888, 240, 320));
			
			default:
				throw new IllegalArgumentException(__backend);
		}
	}
}
