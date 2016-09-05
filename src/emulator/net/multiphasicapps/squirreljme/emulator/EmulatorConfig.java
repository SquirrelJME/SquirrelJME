// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator;

/**
 * This is used to configure the emulator for when it runs.
 *
 * @since 2016/08/21
 */
public final class EmulatorConfig
{
	/** Configuration lock. */
	protected final Object lock =
		new Object();
	
	/**
	 * Initializes the base configuration.
	 *
	 * @since 2016/08/21
	 */
	public EmulatorConfig()
	{
	}
	
	/**
	 * Creates an immutable copy of this configuration.
	 *
	 * @return The configuration copy.
	 * @since 2016/09/03
	 */
	public final Immutable immutable()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Registers the given object to the emulator configuration.
	 *
	 * @param <C> The class to register it under.
	 * @param __cl The class to register it under.
	 * @param __o The object to register.
	 * @throws ClassCastException If the object is not an instance of the
	 * given class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/05
	 */
	public final <C> void registerObject(Class<C> __cl, C __o)
		throws ClassCastException, NullPointerException
	{
		// Check
		if (__cl == null || __o == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CB01 The specified object is not an instance of
		// the given class. (The input class; The class of the object)}
		if (!__cl.isInstance(__o))
			throw new ClassCastException(String.format("CB01 %s", __cl,
				__o.getClass()));
		
		throw new Error("TODO");
	}
	
	/**
	 * This contains an immutable copy of the emulator configuration.
	 *
	 * @since 2016/09/03
	 */
	public static final class Immutable
	{
	}
}

