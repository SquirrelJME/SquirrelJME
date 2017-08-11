// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.unsafe;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This contains the interfaces for the system environment.
 *
 * @see __Ext_systemenvironment__
 * @since 2017/08/10
 */
public final class SystemEnvironment
{
	/** Cached system services. */
	private static final Map<Class<?>, Object> _CACHED_SERVICES =
		new LinkedHashMap<>();
	
	/**
	 * Not used.
	 *
	 * @since 2017/08/10
	 */
	private SystemEnvironment()
	{
	}
	
	/**
	 * This obtains a class which implements a system specific service.
	 * All services that exist are singletons, calling this same method twice
	 * must return the same object for the given class.
	 *
	 * @param <C> The class to get the singleton service for.
	 * @param __cl The class to get the singleton service for.
	 * @return The singleton instance of the given class or {@code null} if
	 * no service for that class exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/20
	 */
	public static <C> C systemService(Class<C> __cl)
		throws NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Only a single thread may access services
		Map<Class<?>, Object> services = _CACHED_SERVICES;
		synchronized (services)
		{
			// If the service is already loaded use it, use contains key
			// instead of a null check because it is possible the services does
			// not actually exist so there is no point in trying again
			Object v = services.get(__cl);
			if (services.containsKey(__cl))
				return __cl.cast(v);
			
			// Check system properties for system service overrides which may
			// be useful when needed
			String sname = __cl.getName(),
				pname = "net.multiphasicapps.squirreljme.unsafe.service." +
					sname,
				vclass = System.getProperty(pname);
			
			// Try creating an instance of it
			if (vclass != null)
				try
				{
					C rv = __cl.cast(Class.forName(sname).newInstance());
					services.put(__cl, rv);
					return rv;
				}
				
				// Does not exist, ignore, fallback to the system
				catch (Exception e)
				{
				}
			
			// See if the system internally declares a service class for the
			// given service
			vclass = __Ext_systemenvironment__.mapService(sname);
			if (vclass == null)
			{
				services.put(__cl, null);
				return null;
			}
			
			// Create instance if possible
			try
			{
				C rv = __cl.cast(Class.forName(vclass).newInstance());
				services.put(__cl, rv);
				return rv;
			}
			
			// Failed, cache null so lookup does not happen again
			catch (Exception e)
			{
				services.put(__cl, null);
				return null;
			}
		}
	}
}

