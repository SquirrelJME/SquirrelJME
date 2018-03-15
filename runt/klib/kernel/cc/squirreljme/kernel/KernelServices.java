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
		
		// Initialize each individual service
		for (Map.Entry<String, String> e : classmap.entrySet())
		{
			String client = e.getKey(),
				server = e.getValue();
			
			throw new todo.TODO();
		}
		
		throw new todo.TODO();
	}
}

