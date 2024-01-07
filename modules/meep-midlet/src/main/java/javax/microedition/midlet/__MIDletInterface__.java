// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.midlet;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.midlet.ApplicationHandler;
import cc.squirreljme.runtime.midlet.ApplicationInterface;
import cc.squirreljme.runtime.midlet.ApplicationType;

/**
 * Interface for MIDlet based applications.
 *
 * @since 2021/11/30
 */
@SquirrelJMEVendorApi
final class __MIDletInterface__
	implements ApplicationInterface<MIDlet>
{
	/** The main entry class. */
	protected final String mainClass;
	
	/**
	 * Initializes the MIDlet parameters.
	 * 
	 * @param __mainClass The main entry class.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/11/30
	 */
	__MIDletInterface__(String __mainClass)
		throws NullPointerException
	{
		if (__mainClass == null)
			throw new NullPointerException("NARG");
		
		this.mainClass = __mainClass;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/30
	 */
	@Override
	@SquirrelJMEVendorApi
	public void destroy(MIDlet __instance, Throwable __thrown)
		throws NullPointerException, Throwable
	{
		if (__instance == null)
			throw new NullPointerException("NARG");
		
		// Always try to destroy the MIDlet
		try
		{
			__instance.destroyApp(true);
		}
		catch (MIDletStateChangeException e)
		{
			// Ignore, but still print a trace
			e.printStackTrace(System.err);
		}
		
		// MIDlet is gone now, exit
		__instance.notifyDestroyed();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/30
	 */
	@Override
	@SquirrelJMEVendorApi
	public MIDlet newInstance()
		throws Throwable
	{
		// Locate the main class before we initialize it
		String mainClass = this.mainClass;
		Class<?> classType;
		try
		{
			classType = Class.forName(mainClass);
		}
		catch (ClassNotFoundException e)
		{
			/* {@squirreljme.error AD03 Could not find main MIDlet. (Class)} */
			throw new RuntimeException(String.format(
				"AD03 %s", mainClass), e);
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
				return (MIDlet)rawInstance;
			}
			catch (ClassCastException e)
			{
				/* {@squirreljme.error AD05 Class not a MIDlet.} */
				throw new RuntimeException("AD05", e);
			}
		}
		catch (IllegalAccessException|InstantiationException e)
		{
			/* {@squirreljme.error AD04 Could not instantiate class.} */
			throw new RuntimeException("AD04", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/30
	 */
	@Override
	@SquirrelJMEVendorApi
	public void startApp(MIDlet __instance)
		throws NullPointerException, Throwable
	{
		if (__instance == null)
			throw new NullPointerException("NARG");
		
		__instance.startApp();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/21
	 */
	@Override
	@SquirrelJMEVendorApi
	public ApplicationType type()
	{
		return ApplicationType.MIDLET;
	}
}
