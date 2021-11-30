// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.midlet.ApplicationInterface;

/**
 * Handles i-appli and i-mode applications for launching.
 *
 * @since 2021/11/30
 */
final class __IAppliInterface__
	implements ApplicationInterface<IApplication>
{
	/** Main application class. */
	protected final String mainClass;
	
	/** Arguments to the class. */
	private final String[] _args;
	
	/**
	 * Initializes the i-application interface.
	 * 
	 * @param __mainClass The main class.
	 * @param __args The application arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/11/30
	 */
	public __IAppliInterface__(String __mainClass, String... __args)
		throws NullPointerException
	{
		if (__mainClass == null)
			throw new NullPointerException("NARG");
		
		this.mainClass = __mainClass;
		this._args = __args;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/30
	 */
	@Override
	public void destroy(IApplication __instance, Throwable __thrown)
		throws NullPointerException, Throwable
	{
		if (__instance == null)
			throw new NullPointerException("NARG");
		
		// Indicate termination
		__instance.terminate();
		
		// We need to exit the VM ourself here
		System.exit((__thrown != null ? 1 : 0));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/30
	 */
	@Override
	public IApplication newInstance()
		throws Throwable
	{
		// Load application details
		synchronized (IApplication.class)
		{
			IApplication._appArgs = this._args;
		}
		
		// Locate the main class before we initialize it
		String mainClass = this.mainClass;
		Class<?> classType;
		try
		{
			classType = Class.forName(mainClass);
		}
		catch (ClassNotFoundException e)
		{
			// {@squirreljme.error AH01 Could not find main i-appli. (Class)}
			throw new RuntimeException(String.format(
				"AH01 %s", mainClass), e);
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
				return (IApplication)rawInstance;
			}
			catch (ClassCastException e)
			{
				// {@squirreljme.error AH02 Class not an i-appli.}
				throw new RuntimeException("AH02", e);
			}
		}
		catch (IllegalAccessException|InstantiationException e)
		{
			// {@squirreljme.error AH03 Could not instantiate class.}
			throw new RuntimeException("AH03", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/30
	 */
	@Override
	public void startApp(IApplication __instance)
		throws NullPointerException, Throwable
	{
		if (__instance == null)
			throw new NullPointerException("NARG");
		
		// Start the application
		__instance.start();
	}
}
