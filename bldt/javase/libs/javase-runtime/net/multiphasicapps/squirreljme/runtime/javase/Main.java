// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * This initializes the SquirrelJME CLDC run-time interfaces and provides a
 * bridge so that the run-time functions properly on non-SquirrelJME Java VMs.
 *
 * @since 2017/12/07
 */
public class Main
{
	/**
	 * Wrapped main entry point.
	 *
	 * @param __args Program arguments, these are forwarded.
	 * @throws Throwable On any kind of throwable.
	 * @since 2017/12/07
	 */
	public static void main(String... __args)
		throws Throwable
	{
		// Determine the main class to actually call using the copied
		// manifest
		String mainclassname;
		try (InputStream is = Main.class.getResourceAsStream(
			"/SQUIRRELJME-BOOTSTRAP.MF"))
		{
			// {@squirreljme.error AF01 No manifest is available?}
			if (is == null)
				throw new RuntimeException("AF01");
			
			// {@squirreljme.error AF02 No main class is available?}
			mainclassname = new Manifest(is).getMainAttributes().
				getValue("Main-Class");
			if (mainclassname == null || mainclassname.isEmpty())
				throw new RuntimeException("AF02");
		}
		
		// Exceptions generated as of the result of the method call are
		// wrapped so they must be unwrapped
		try
		{
			// {@squirreljme.error AF03 The main method is not static.}
			Method mainmethod = Class.forName(mainclassname).
				getMethod("main", String[].class);
			if ((mainmethod.getModifiers() & Modifier.STATIC) == 0)
				throw new RuntimeException("AF03");
			
			// Call it
			mainmethod.invoke(null, new Object[]{__args});
		}
		
		// Completely hide call exceptions
		catch (InvocationTargetException e)
		{
			Throwable c = e.getCause();
			if (c != null)
				throw c;
			else
				throw e;
		}
	}
}

