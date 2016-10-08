// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcduilui;

import java.util.ServiceLoader;

/**
 * This is a base class that is used for managing displays that are
 * available for usage.
 *
 * @param <D> The type of class to wrap via the display manager.
 * @param <P> The common display provider to use
 * @since 2016/10/08
 */
public class CommonDisplayManager<R, D extends CommonDisplay<R>,
	P extends CommonDisplayProvider<R, D>>
{
	/** The class to provide a service for. */
	protected final Class<D> driverclass;
	
	/** The raw class. */
	protected final Class<R> rawclass;
	
	/** The service loader for display providers. */
	protected final ServiceLoader<P> services;
	
	/**
	 * Initializes the common display manager.
	 *
	 * @param __rl The raw display class.
	 * @param __dl The driver class.
	 * @param __pl The provider class to use for service lookup.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/08
	 */
	public CommonDisplayManager(Class<R> __rl, Class<D> __dl, Class<P> __pl)
		throws NullPointerException
	{
		// Check
		if (__rl == null || __dl == null || __pl == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.rawclass = __rl;
		this.driverclass = __dl;
		this.services = ServiceLoader.<P>load(__pl);
	}
}

