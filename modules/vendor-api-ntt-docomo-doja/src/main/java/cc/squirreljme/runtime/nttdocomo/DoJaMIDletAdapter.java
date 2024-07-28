// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo;

import cc.squirreljme.jvm.launch.Application;
import cc.squirreljme.jvm.launch.AvailableSuites;
import cc.squirreljme.jvm.launch.DefaultJarPackageShelf;
import cc.squirreljme.jvm.launch.IModeApplication;
import cc.squirreljme.jvm.launch.SuiteScanner;
import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.ReflectionShelf;
import cc.squirreljme.jvm.mle.TypeShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.brackets.TypeBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * SquirrelJME specific adapter to be able to launch DoJa applications from
 * the MIDlet subsystem.
 *
 * @since 2024/07/28
 */
@SquirrelJMEVendorApi
public class DoJaMIDletAdapter
	extends MIDlet
{
	/**
	 * {@inheritDoc}
	 * @since 2024/07/28
	 */
	@Override
	@SquirrelJMEVendorApi
	protected void destroyApp(boolean __uc)
		throws MIDletStateChangeException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/28
	 */
	@Override
	@SquirrelJMEVendorApi
	protected void startApp()
		throws MIDletStateChangeException
	{
		// Load in all suites that we know of
		SuiteScanner scanner = new SuiteScanner(true,
			new DefaultJarPackageShelf());
		AvailableSuites suites = scanner.scanSuites();
		
		// Get our own class path and our context Jar
		JarPackageBracket[] classPath = JarPackageShelf.classPath();
		JarPackageBracket context = classPath[classPath.length - 1];
		
		// Find the first DoJa application under this
		Application[] apps = suites.findApplications(context);
		if (apps == null || apps.length == 0)
			throw new Error("NOPE");
		
		// Find it
		IModeApplication app = null;
		for (Application maybe : apps)
			if (maybe instanceof IModeApplication)
			{
				app = (IModeApplication)maybe;
				break;
			}
		
		// Not found?
		if (app == null)
			throw new Error("NOPE");
		
		// Try to find the entry class
		String maybeClass = app.loaderEntryClass();
		TypeBracket type;
		try
		{
			type = TypeShelf.classToType(
				Class.forName(maybeClass));
		}
		catch (ClassNotFoundException __e)
		{
			throw new Error(__e);
		}
		
		// Forward invoke application
		try
		{
			// Forward call to main
			ReflectionShelf.invokeMain(type, app.loaderEntryArgs());
		}
		catch (Throwable __e)
		{
			if (__e instanceof Error)
				throw (Error)__e;
			else if (__e instanceof RuntimeException)
				throw (RuntimeException)__e;
			throw new RuntimeException(__e);
		}
	}
}
