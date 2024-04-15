// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import cc.squirreljme.jvm.mle.ReflectionShelf;
import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.jvm.mle.TypeShelf;

/**
 * Proxy main class for testing.
 *
 * @since 2022/09/07
 */
public class ProxyMain
{
	/** Was this called? */
	public static volatile boolean called;
	
	/**
	 * Main proxy entry point.
	 * 
	 * @param __args Main arguments.
	 * @since 2022/09/07
	 */
	public static void main(String... __args)
		throws Throwable
	{
		// Set called
		synchronized (ProxyMain.class)
		{
			ProxyMain.called = true;
		}
		
		// Copy over
		String mainClass = __args[0];
		String[] mainArgs = new String[__args.length - 1];
		System.arraycopy(__args, 1,
			mainArgs, 0, __args.length - 1);
		
		// Use shelf call to main
		ReflectionShelf.invokeMain(
			TypeShelf.findType(mainClass.replace('.', '/')),
			mainArgs);
	}
}
