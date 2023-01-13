// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package mleui;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.constants.UIMetricType;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import cc.squirreljme.runtime.lcdui.mle.fb.FBUIBackend;
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
	public void test(String __input)
		throws Throwable
	{
		int at = __input.indexOf('@');
		int displayNum = Integer.parseInt(__input.substring(0, at).trim());
		String backendId = __input.substring(at + 1).trim();
		
		// Determine which display we are poking
		UIBackend backend = BaseBackend.__getBackend(backendId);
		UIDisplayBracket[] displays = backend.displays();
		if (displayNum < 0 || displayNum >= displays.length)
			throw new UntestableException(String.format(
				"No display #%d for %s.", displayNum, backendId));
		
		// UIForms must be supported for this given backend
		UIDisplayBracket display = displays[displayNum];
		if (0 != UIFormShelf.metric(display, UIMetricType.UIFORMS_SUPPORTED))
			throw new UntestableException(backendId);
		
		// Run the test on this display
		this.test(backend, display);
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
				return new NativeUIBackend();
			
			case "FRAMEBUFFER":
				return new FBUIBackend(new HeadlessAttachment(
					UIPixelFormat.INT_RGB888, 240, 320));
			
			default:
				throw new IllegalArgumentException(__backend);
		}
	}
}
