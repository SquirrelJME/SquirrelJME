// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.io;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.gcf.CustomConnectionFactory;
import com.nttdocomo.ui.IApplication;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.ConnectionOption;

/**
 * Factory to create {@link ResourceConnection}.
 *
 * @see ResourceConnection
 * @since 2021/11/30
 */
@SquirrelJMEVendorApi
public class ResourceConnectionFactory
	implements CustomConnectionFactory
{
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/11/30
	 */
	@SuppressWarnings("resource")
	@Override
	@SquirrelJMEVendorApi
	public Connection connect(String __part, int __mode, boolean __timeouts,
		ConnectionOption<?>[] __opts)
		throws IOException, NullPointerException
	{
		if (__part == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AH0i Resource URI does not start with triple
		// slash. (The URI part)}
		if (!__part.startsWith("///"))
			throw new ConnectionNotFoundException("AH0i " + __part);
		
		// Try loading the resource once
		Class<?> pivot = IApplication.getCurrentApp().getClass();
		String rcName = __part.substring(2);
		try (InputStream in = pivot.getResourceAsStream(rcName))
		{
			// {@squirreljme.error AH0j The specified resource does not exist.
			// (The URI part)}
			if (in == null)
				throw new ConnectionNotFoundException("AH0j " + __part);
			
			// Set up the connection
			return new ResourceConnection(pivot, rcName);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/30
	 */
	@Override
	@SquirrelJMEVendorApi
	public String scheme()
	{
		return "resource";
	}
}
