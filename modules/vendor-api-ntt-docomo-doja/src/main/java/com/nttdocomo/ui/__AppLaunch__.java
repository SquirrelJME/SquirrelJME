// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.midlet.ApplicationHandler;
import cc.squirreljme.runtime.nttdocomo.DoJaRuntime;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * This class takes care of launching {@link IApplication}.
 *
 * @since 2021/06/13
 */
@SquirrelJMEVendorApi
final class __AppLaunch__
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @throws Throwable On any exception.
	 * @since 2020/02/29
	 */
	@SquirrelJMEVendorApi
	public static void main(String... __args)
		throws Throwable
	{
		/* {@squirreljme.error AH04 No main i-mode class specified.} */
		if (__args == null || __args.length < 1 || __args[0] == null)
			throw new IllegalArgumentException("AH04");
		
		// Need to forward properties?
		Deque<String> inArgs = new ArrayDeque<>(Arrays.asList(__args));
		while (!inArgs.isEmpty())
		{
			// None left? This should not normally occur
			String arg = inArgs.peekFirst();
			if (arg == null)
				break;
			
			// ADF property?
			if (arg.startsWith("-Xadf:"))
			{
				// Consume it
				inArgs.pollFirst();
				
				// Starting position of the actual value used
				int base = "-Xadf:".length();
				
				// Is there an equal sign?
				int eq = arg.indexOf('=');
				if (eq >= 0)
					DoJaRuntime.putProperty(arg.substring(base, eq),
						arg.substring(eq + 1));
				else
					DoJaRuntime.putProperty(arg.substring(base), "");
			}
			
			// Otherwise stop, treat as normal application
			else
				break;
		}
		
		// Re-adjust with whatever is left
		__args = inArgs.toArray(new String[inArgs.size()]);
		
		// Are there any arguments to the application call?
		int argLen = inArgs.size();
		String[] appArgs =
			(argLen <= 1 ? new String[0] : new String[argLen - 1]);
		for (int i = 1, o = 0; i < argLen; i++, o++)
			appArgs[o] = __args[i];
		
		// Call the common application handler
		ApplicationHandler.main(new __IAppliInterface__(__args[0], appArgs));
	}
}
