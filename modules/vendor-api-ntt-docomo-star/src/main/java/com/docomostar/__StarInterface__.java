// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.docomostar;

import cc.squirreljme.jvm.launch.IModeProperty;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.midlet.ApplicationHandler;
import cc.squirreljme.runtime.midlet.ApplicationInterface;
import cc.squirreljme.runtime.midlet.ApplicationType;
import java.util.Objects;

/**
 * Handles launching and handling of Star Applications.
 *
 * @since 2022/02/28
 */
@SquirrelJMEVendorApi
final class __StarInterface__
	implements ApplicationInterface<StarApplication>
{
	/** Main application class. */
	protected final String mainClass;
	
	/** Arguments to the class. */
	private final String[] _args;
	
	/**
	 * Initializes the Star interface.
	 * 
	 * @param __mainClass The main class.
	 * @param __args The application arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/28
	 */
	@SquirrelJMEVendorApi
	__StarInterface__(String __mainClass, String... __args)
		throws NullPointerException
	{
		if (__mainClass == null)
			throw new NullPointerException("NARG");
		
		this.mainClass = __mainClass;
		this._args = __args;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/28
	 */
	@Override
	@SquirrelJMEVendorApi
	public void destroy(StarApplication __instance, Throwable __thrown)
		throws NullPointerException, Throwable
	{
		if (__instance == null)
			throw new NullPointerException("NARG");
		
		// Unlike DoJa, the terminate handler must handle cleanup as it does
		// the true application termination
		__instance._terminateException = __thrown;
		__instance.terminate();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/28
	 */
	@Override
	@SquirrelJMEVendorApi
	public StarApplication newInstance()
		throws Throwable
	{
		// Load application details
		synchronized (StarApplicationManager.class)
		{
			StarApplicationManager._appArgs = this._args;
		}
		
		// Main class for entry
		String mainClass = this.mainClass;
		
		// Load the suite and vendor which is needed for RMS to properly
		// identify our own records
		String appName = System.getProperty(IModeProperty.NAME_PROPERTY);
		String appVend = System.getProperty(IModeProperty.VENDOR_PROPERTY);
		ApplicationHandler.setNameAndVendor(
			Objects.toString(appName, mainClass),
			Objects.toString(appVend, "SquirrelJME-Star"));
		
		// Locate the main class before we initialize it
		Class<?> classType;
		try
		{
			classType = Class.forName(mainClass);
		}
		catch (ClassNotFoundException e)
		{
			/* {@squirreljme.error AN02 Could not find main Star class.
			(Class)} */
			throw new RuntimeException(String.format(
				"AN02 %s", mainClass), e);
		}
		
		// Create instance of the MIDlet
		try
		{
			// Create it
			Object rawInstance = classType.newInstance();
			
			// Catch class casts here because if there is one while the
			// instance is being created it will not be erroneously caught
			try
			{
				return (StarApplication)rawInstance;
			}
			catch (ClassCastException e)
			{
				/* {@squirreljme.error AN04 Class not a StarApplication.} */
				throw new RuntimeException("AN04", e);
			}
		}
		catch (IllegalAccessException|InstantiationException e)
		{
			/* {@squirreljme.error AN03 Could not instantiate class.} */
			throw new RuntimeException("AN03", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/28
	 */
	@Override
	@SquirrelJMEVendorApi
	public void startApp(StarApplication __instance)
		throws NullPointerException, Throwable
	{
		if (__instance == null)
			throw new NullPointerException("NARG");
		
		// Start the application
		__instance.started(StarApplication.LAUNCHED_FROM_LAUNCHER);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/21
	 */
	@Override
	@SquirrelJMEVendorApi
	public ApplicationType type()
	{
		return ApplicationType.NTT_DOCOMO_STAR;
	}
}

