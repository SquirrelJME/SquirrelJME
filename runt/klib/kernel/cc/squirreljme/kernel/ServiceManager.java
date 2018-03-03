// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.SortedTreeMap;

/**
 * This class manages the kernel end of all the services which are available
 * for usage.
 *
 * @since 2018/03/03
 */
public final class ServiceManager
{
	/** Services which are available for usage. */
	private final Map<Class<?>, Service> _services =
		new LinkedHashMap<>();
	
	/** Index to service mapping. */
	private final Map<Integer, Service> _servicesindex =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the service manager.
	 *
	 * @param __conf The configuration to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/03
	 */
	public ServiceManager(KernelConfiguration __conf)
		throws NullPointerException
	{
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Fill in the services which are potentially going to exist, use the
		// ones provided by the implementation configuration
		Set<String> serviceclasses = new LinkedHashSet<>();
		for (String s : __config.services())
			if (s != null)
				serviceclasses.add(s);
		
		// Add ones which are provided by default
		for (String s : DefaultKernelServices.services())
			if (s != null)
				serviceclasses.add(s);
		
		// Setup service providers and map them all to the same index
		Map<Class<?>, Service> services = this._services;
		Map<Integer, Service> servicesindex = this._servicesindex;
		synchronized (services)
		{
			// Always put a blank index in
			services.put(null, null);
			servicesindex.put(0, null);
			
			// Load in providers
			int nextdx = 1;
			for (String sv : serviceclasses)
				try
				{
					// Find the custom service provider or just fall back to
					// the default
					String mapped = Objects.toString(__config.mapService(sv),
						DefaultKernelServices.mapService(sv));
					
					// Make a service for it
					Service made = new Service(nextdx,
						Class.forName(sv),
						Class.forName(mapped));
					
					// If it worked, record it
					services.put(made._client, made);
					servicesindex.put(nextdx, made);
					
					// All services have unique IDs
					nextdx++;
				}
				
				// {@squirreljme.error AP0b Could not locate a class for the
				// specified service. (The service class)}
				catch (ClassNotFoundException e)
				{
					throw new RuntimeException(
						String.format("AP0b %s", sv), e);
				}
		}
	}
	
	/**
	 * Returns the number of services which are available.
	 *
	 * @return The number of available services.
	 * @since 2018/01/04
	 */
	public final int count()
	{
		// This may changed during initialization
		Map<Class<?>, __Service__> services = this._services;
		synchronized (services)
		{
			return services.size();
		}
	}
	
	/**
	 * Returns the service associated with the given index.
	 *
	 * @param __dx The index to get.
	 * @return The service at the given index.
	 * @throws IndexOutOfBoundsException If the service index is not valid.
	 * @since 2018/01/05
	 */
	public final ServiceProvider serviceGet(int __dx)
		throws IndexOutOfBoundsException
	{
		__Service__ sv;
		
		// Services may be lazily initialized, and the service index shares
		// the same map set
		Map<Class<?>, __Service__> services = this._services;
		Map<Integer, __Service__> servicesindex = this._servicesindex;
		synchronized (services)
		{
			sv = servicesindex.get(__dx);
		}
		
		// {@squirreljme.error AP04 Invalid service index. (The index)}
		if (sv == null)
			throw new IndexOutOfBoundsException(
				String.format("AP04 %d", __dx));
		
		// The service can be initialize outside of the lock because there is
		// another lock in the service itself
		return sv.__provider();
	}
	
	/**
	 * Returns the service index for the given class.
	 *
	 * @param __cl The class to get the service index for.
	 * @return The index for the class or {@code 0} if there is no service.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	public final int serviceIndex(Class<?> __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Lock on the services map because this could be lazily initialized
		Map<Class<?>, __Service__> services = this._services;
		synchronized (services)
		{
			__Service__ sv = services.get(__cl);
			if (sv == null)
				return 0;
			
			return sv._index;
		}
	}
	
	/**
	 * Instances of services for a single task.
	 *
	 * @since 2018/03/03
	 */
	public final class Instances
	{
	}
	
	/**
	 * This represents an available service.
	 *
	 * @since 2018/01/15
	 */
	public static final class Service
	{
		/** The index of the service. */
		final int _index;
		
		/** The client class */
		final Class<?> _client;
		
		/** The provider class. */
		final Class<?> _provider;
		
		/** The provider for the service. */
		private volatile ServiceProvider _instance;
		
		/**
		 * Initializes the service.
		 *
		 * @param __dx The service index.
		 * @param __client The client class.
		 * @param __provider The server provider class.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/01/15
		 */
		private Service(int __dx, Class<?> __client, Class<?> __provider)
			throws NullPointerException
		{
			if (__client == null || __provider == null)
				throw new NullPointerException("NARG");
			
			this._index = __dx;
			this._client = __client;
			this._provider = __provider;
		}
		
		/**
		 * Initializes the service provider.
		 *
		 * @return The service provider.
		 * @since 2018/01/15
		 */
		final ServiceProvider __provider()
		{
			// The service may need to be initialized accordingly
			synchronized (this)
			{
				// Is lazilly initialized
				ServiceProvider rv = this._instance;
				if (rv == null)
					try
					{
						rv = ((ServiceProviderFactory)this._provider.
							newInstance()).create();
						
						// {@squirreljme.error AP01 The server provides a
						// service for a different client than than the one
						// which was expected. (The expected client class;
						// The one the service provides)}
						Class<?> clclass = rv.clientClass(),
							wantcl = this._client;
						if (wantcl != clclass)
							throw new RuntimeException(String.format(
								"AP01 %s %s", wantcl, clclass));
						
						// It now exists, so it may be used again
						this._instance = rv;
					}
					
					// {@squirreljme.error AP02 Could not initialize the
					// service. (The service class; The provider class)}
					catch (ClassCastException|IllegalAccessException|
						InstantiationException e)
					{
						throw new RuntimeException(
							String.format("AP02 %s %s", this._client,
							this._provider), e);
					}
				
				return rv;
			}
		}
	}
}

