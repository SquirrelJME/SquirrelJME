// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.standalone.main;

import cc.squirreljme.emulator.NativeBinding;
import cc.squirreljme.emulator.vm.VMFactory;
import java.util.Arrays;

/**
 * Main entry point for standalone virtual machine.
 *
 * @since 2022/06/13
 */
public class Main
{
	/**
	 * Main entry point for the virtual machine.
	 * 
	 * @param __args Arguments to the program.
	 * @since 2022/06/13
	 */
	public static void main(String... __args)
		throws Throwable
	{
		// We specifically only want to launch the debugger
		if (__args.length >= 1 && "-XdebuggerOnly".equals(__args[0]))
		{
			// Strip the leading flag
			cc.squirreljme.debugger.Main.main(
				Arrays.copyOfRange(__args, 1, __args.length));
			
			// Do not continue normal execution
			return;
		}
		
		// Setup arguments to wrap
		String[] realArgs = new String[__args.length + 1];
		System.arraycopy(__args, 0,
			realArgs, 1, __args.length);
		realArgs[0] = VMFactory.class.getName();
		
		// Forward to native binding handling
		NativeBinding.main(realArgs);
	}
}
