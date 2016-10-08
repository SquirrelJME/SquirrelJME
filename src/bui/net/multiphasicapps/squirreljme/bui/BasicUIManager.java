// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bui;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * This class is used as a base for basic UI managers.
 *
 * @param <U> The UI to provide.
 * @param <P> The provider for the given UI.
 * @since 2016/10/08
 */
public abstract class BasicUIManager<U extends BasicUI,
	P extends BasicUIProvider<U>>
{
	/** The service lookup. */
	protected final ServiceLoader<P> services;
	
	/**
	 * Initializes the basic UI manager.
	 *
	 * @param __pl The class used for the provider.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/08
	 */
	public BasicUIManager(Class<P> __pl)
		throws NullPointerException
	{
		// Check
		if (__pl == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.services = ServiceLoader.<P>load(__pl);
	}
	
	/**
	 * This creates a generic array.
	 *
	 * @param __len The length of the array.
	 * @return The allocated array.
	 * @since 2016/10/08
	 */
	protected abstract U[] newArray(int __len);
	
	/**
	 * Returns an array of UI elements that exist.
	 *
	 * @return The UIs.
	 * @since 2016/10/08
	 */
	public final U[] uis()
	{
		// Load elements into the array
		List<U> rv = new ArrayList<>();
		for (P p : this.services)
			for (U u : p.uis())
				rv.add(u);
		
		// Return
		return rv.<U>toArray(newArray(rv.size()));
	} 
}

