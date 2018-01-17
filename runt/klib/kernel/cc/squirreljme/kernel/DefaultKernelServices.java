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

import java.util.Arrays;

/**
 * This class contains .
 *
 * @since 2018/01/03
 */
public final class DefaultKernelServices
{
	/**
	 * Not used.
	 *
	 * @since 2018/01/03
	 */
	private DefaultKernelServices()
	{
	}
	
	/**
	 * Maps the given service to a default provider.
	 *
	 * @param __sv The name of the client service.
	 * @return The class to map to for the server.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/03
	 */
	public static final String mapService(String __sv)
		throws NullPointerException
	{
		if (__sv == null)
			throw new NullPointerException("NARG");
		
		switch (__sv)
		{
			case "cc.squirreljme.kernel.trust.client.TrustClient":
				return "cc.squirreljme.kernel.trust.server.noop." +
					"NoOpTrustProviderFactory";
				
			default:
				return null;
		}
	}
	
	/**
	 * Returns the default set of services.
	 *
	 * @return The default services set.
	 * @since 2018/01/03
	 */
	public static final Iterable<String> services()
	{
		return Arrays.<String>asList(
			"cc.squirreljme.kernel.trust.client.TrustClient");
	}
}

