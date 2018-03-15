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

import cc.squirreljme.runtime.cldc.service.NoSuchServiceException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class provides the manager for services available on the kernel side.
 *
 * @since 2018/03/15
 */
public final class KernelServices
{
	/** Services which are provided by default in the kernel. */
	private static final String[] _DEFAULT_SERVICES =
		new String[]{
			"cc.squirreljme.kernel.trust.client.TrustClient",
			"cc.squirreljme.kernel.trust.server.noop.NoOpTrustProviderFactory",
		};
	
	/** Services which are available. */
	private final KernelService[] _services;
	
	/**
	 * Initializes services that the kernel provides for userspace tasks.
	 *
	 * @param __m The mapping of system provided services.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/15
	 */
	public KernelServices(String[] __m)
		throws NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Initialize the service map with default and implementation
		// provided services
		Map<String, String> classmap = new LinkedHashMap<>();
		for (int z = 0; z < 2; z++)
		{
			String[] from = (z == 0 ? KernelServices._DEFAULT_SERVICES : __m);
			for (int i = 0, n = from.length; i + 1 < n; i+= 2)
				classmap.put(from[i], from[i + 1]);
		}
		
		// Setup sub-classes for individual services
		int count = classmap.size();
		KernelService[] services = new KernelService[count];
		
		// Setup basic service information
		int i = 0;
		for (Map.Entry<String, String> ent : classmap.entrySet())
		{
			services[i] = new KernelService(i, ent.getKey(), ent.getValue());
			i++;
		}
		
		// Store
		this._services = services;
	}
	
	/**
	 * Locates a given service by the client class.
	 *
	 * @param __n The client class to locate.
	 * @return The found service.
	 * @throws NoSuchServiceException If the service does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/15
	 */
	public final KernelService byClientClass(String __n)
		throws NoSuchServiceException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		KernelService[] services = this._services;
		for (KernelService ks : services)
			if (__n.equals(ks.clientClass()))
				return ks;
		
		// {@squirreljme.error AP04 The service specified by the given
		// client class does not exist. (The client class)}
		throw new NoSuchServiceException(String.format("AP04 %s", __n));
	}
	
	/**
	 * Locates a given service by the service index.
	 *
	 * @param __dx The index of the service.
	 * @return The found service.
	 * @throws NoSuchServiceException If the service does not exist.
	 * @since 2018/03/15
	 */
	public final KernelService byIndex(int __dx)
		throws NoSuchServiceException
	{
		// {@squirreljme.error AP02 The service index is outside of bounds.
		// (The service index)}
		KernelService[] services = this._services;
		if (__dx < 0 || __dx >= services.length)
			throw new NoSuchServiceException(String.format("AP02 %d", __dx));
		return services[__dx];
	}
	
	/**
	 * Returns the number of available services.
	 *
	 * @return The service count.
	 * @since 2018/03/15
	 */
	public final int count()
	{
		return this._services.length;
	}
}

