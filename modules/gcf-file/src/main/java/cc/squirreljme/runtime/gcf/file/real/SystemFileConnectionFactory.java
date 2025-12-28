// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file.real;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.gcf.CustomConnectionFactory;
import cc.squirreljme.runtime.gcf.uri.UriPart;
import java.io.IOException;
import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionOption;

/**
 * Provides access to {@link SystemFileConnection}.
 *
 * @since 2025/12/27
 */
@SquirrelJMEVendorApi
public class SystemFileConnectionFactory
	implements CustomConnectionFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2025/12/27
	 */
	@Override
	public Connection connect(UriPart __part, int __mode, boolean __timeouts,
		ConnectionOption<?>[] __opts)
		throws IOException, NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/27
	 */
	@Override
	public String scheme()
	{
		return "file";
	}
}
