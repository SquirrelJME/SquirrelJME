// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.midlet;

/**
 * This class
 *
 * @since 2020/02/29
 */
final class __MainHandler__
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
		// {@squirreljme.error AD02 No main MIDlet class specified.}
		if (__args == null || __args.length < 1 || __args[0] == null)
			throw new IllegalArgumentException("AD02");
		
		// Locate the main class before we initialize it
		Class<?> classType;
		try
		{
			classType = Class.forName(__args[0]);
		}
		catch (ClassNotFoundException e)
		{
			// {@squirreljme.error AD03 Could not find main MIDlet. (Class)}
			throw new RuntimeException(String.format("AD03 %s", __args[0]), e);
		}
		
		// Create instance of the MIDlet
		MIDlet instance;
		try
		{
			// Create it
			Object rawInstance = classType.newInstance();
			
			// Catch class casts here because if there is one while the
			// instance is being created it will not be erroneously caught
			try
			{
				instance = (MIDlet)rawInstance;
			}
			catch (ClassCastException e)
			{
				// {@squirreljme.error AD05 Class not a MIDlet.}
				throw new RuntimeException("AD05", e);
			}
		}
		catch (IllegalAccessException|InstantiationException e)
		{
			// {@squirreljme.error AD04 Could not instantiate class.}
			throw new RuntimeException("AD04", e);
		}
		
		// Start the MIDlet and perform any potential handling of it
		try
		{
			instance.startApp();
		}
		finally
		{
			// Always try to destroy the MIDlet
			try
			{
				instance.destroyApp(true);
			}
			catch (MIDletStateChangeException e)
			{
				// Ignore, but still print a trace
				e.printStackTrace(System.err);
			}
			
			// Application is gone now, exit
			instance.notifyDestroyed();
		}
	}
}
