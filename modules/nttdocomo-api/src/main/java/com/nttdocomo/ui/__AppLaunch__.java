// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.Poking;
import cc.squirreljme.runtime.midlet.ApplicationHandler;

/**
 * This class takes care of launching {@link IApplication}s.
 *
 * @since 2021/06/13
 */
final class __AppLaunch__
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @throws Throwable On any exception.
	 * @since 2020/02/29
	 */
	public static void main(String... __args)
		throws Throwable
	{	
		// We might be on the emulator, so ensure our native interfaces and
		// otherwise are properly loaded
		Poking.poke();
		
		// {@squirreljme.error AH01 No main i-mode class specified.}
		if (__args == null || __args.length < 1 || __args[0] == null)
			throw new IllegalArgumentException("AH01");
		
		// Are there any arguments to the application call?
		int argLen = __args.length;
		String[] appArgs = (argLen <= 1 ? new String[0] :
			new String[argLen - 1]);
		for (int i = 1, o = 0; i < argLen; i++, o++)
			appArgs[o] = __args[i];
		
		// Call the common application handler
		ApplicationHandler.main(new __IAppliInterface__(__args[0], appArgs));
	}
}
