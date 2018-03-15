// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.service;

import cc.squirreljme.runtime.cldc.system.SystemCall;
import cc.squirreljme.runtime.cldc.system.type.ClassType;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides access to services which are available to the system
 * for clients to use.
 *
 * @since 2018/03/02
 */
public final class ServiceAccessor
{
	/** Mapping of client classes to instances. */
	private static final Map<Class<?>, Integer> _INSTANCEMAP =
		new HashMap<>();
	
	/** Services which have been initialized for clients. */
	private static final Map<Integer, Object> _INSTANCES =
		new HashMap<>();
	
	/**
	 * Not used.
	 *
	 * @since 2018/03/02
	 */
	private ServiceAccessor()
	{
	}
	
	/**
	 * Obtains the specified service instance.
	 *
	 * @param __cl The class of the client service interface.
	 * @return The instance of the client interface.
	 * @throws NoSuchServiceException If the specified service does not
	 * exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/02
	 */
	public static final <R> R service(Class<R> __cl)
		throws NoSuchServiceException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		Map<Class<?>, Integer> instancemap = ServiceAccessor._INSTANCEMAP;
		Map<Integer, Object> instances = ServiceAccessor._INSTANCES;
		
		// Need to obtain the service index first
		Integer svdx;
		synchronized (instancemap)
		{
			svdx = instancemap.get(__cl);
			if (svdx == null)
			{
				svdx = SystemCall.EASY.serviceQueryIndex(new ClassType(__cl));
				
				// {@squirreljme.error ZZ0h No such service exists for the
				// given class. (The service class)}
				if (svdx < 0)
					throw new NoSuchServiceException(String.format("ZZ0h %s",
						__cl));
				
				instancemap.put(__cl, svdx);
			}
		}
		
		// Obtain service instance
		Object rv;
		synchronized (instances)
		{
			rv = instances.get(svdx);
			if (rv == null)
			{
				// Create instance
				int dx = svdx;
				try
				{
					rv = ((ServiceClientProvider)((ClassType)SystemCall.EASY.
						serviceQueryClass(dx)).forClass().newInstance()).
						initializeClient(new ServiceCaller(dx));
				}
				
				// {@squirreljme.error ZZ0i Could not initialize the service.
				// (The client class)}
				catch (IllegalAccessException|InstantiationException e)
				{
					throw new IllegalStateException(String.format("ZZ0i %s",
						__cl), e);
				}
				
				// Cache it
				instances.put(svdx, rv);
			}
		}
		
		return __cl.cast(rv);
	}
}

