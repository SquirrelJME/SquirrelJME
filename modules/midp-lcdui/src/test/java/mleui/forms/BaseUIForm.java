// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package mleui.forms;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import mleui.BaseBackend;

/**
 * Base tests on forms associated with a display.
 *
 * @since 2020/07/01
 */
public abstract class BaseUIForm
	extends BaseBackend
{
	/**
	 * Performs the UI test.
	 * 
	 * @param __backend The backend to use for calls.
	 * @param __display The display.
	 * @param __form The form.
	 * @throws Throwable Any exceptions as needed.
	 * @since 2020/07/01
	 */
	protected abstract void test(UIBackend __backend,
		UIDisplayBracket __display, UIFormBracket __form)
		throws Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/01
	 */
	@Override
	public final void test(UIBackend __backend, UIDisplayBracket __display)
		throws Throwable
	{
		UIFormBracket form = __backend.formNew();
		try
		{
			// We should actually display the form in order to see if it
			// works
			__backend.displayShow(__display, form);
			__backend.flushEvents();
			
			// Run the test
			this.test(__backend, __display, form);
		}
		
		// Delete the form
		finally
		{
			// Debug
			Debugging.debugNote("Cleaning up form...");
			
			// Remove from the display and delete it
			__backend.displayShow(__display, null);
			__backend.formDelete(form);
			
			Debugging.debugNote("Cleaned up!");
		}
	}
}
