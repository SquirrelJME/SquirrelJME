// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.gcf.CustomConnectionFactory;
import cc.squirreljme.runtime.gcf.uri.UriGenericPart;
import cc.squirreljme.runtime.gcf.uri.UriPart;
import cc.squirreljme.runtime.gcf.uri.UriSchemeSpecificPart;
import java.io.IOException;
import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionOption;
import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

/**
 * Factory for creating endpoint files.
 *
 * @since 2025/12/29
 */
@SquirrelJMEVendorApi
public class FileEndPointConnectionFactory
	implements CustomConnectionFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2025/12/29
	 */
	@Override
	public Connection connect(UriPart __part, int __mode, boolean __timeouts,
		ConnectionOption<?>[] __opts)
		throws IOException, NullPointerException
	{
		if (__part == null)
			throw new NullPointerException("NARG");
		
		// If this is a scheme specific part, then this is very likely blank
		/* {@squirreljme.error GF09 File URI is of the incorrect syntax.
		(The URI)} */
		if (__part instanceof UriSchemeSpecificPart)
		{
			// It really must be blank
			if (!"".equals(__part.toString()))
				throw new IOException(
					__error__("GF09 %s", __part));
			
			__part = null;
		}
		
		// And now it must truly be a generic part
		if (!(__part instanceof UriGenericPart))
			throw new IOException(
				__error__("GF09 %s", __part));
		
		// The connection could start connected to an endpoint or just be
		// connected to nothing
		FileEndPointConnection connection = new FileEndPointConnection(__mode);
		if (__part != null)
			return connection.__changeEndPoint((UriGenericPart)__part,
				null);
		return connection;
			
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/29
	 */
	@Override
	public String scheme()
	{
		return "file";
	}
}
