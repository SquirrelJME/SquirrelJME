// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.io.IOException;
import java.util.ServiceLoader;
import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionOption;
import javax.microedition.io.Connector;

/**
 * Factory used to access custom connectors and otherwise with
 * {@link Connector}.
 * 
 * Used with {@link ServiceLoader}.
 *
 * @since 2021/11/30
 */
@SquirrelJMEVendorApi
public interface CustomConnectionFactory
{
	/**
	 * Connects to the given part.
	 * 
	 * @param __part The part of the URI to connect to.
	 * @param __mode The connection mode.
	 * @param __timeouts Timeouts are used?
	 * @param __opts Any connection options to use.
	 * @return The connection.
	 * @throws IOException If the connection could not be opened.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/11/30
	 */
	@SquirrelJMEVendorApi
	Connection connect(String __part, int __mode, boolean __timeouts,
		ConnectionOption<?>[] __opts)
		throws IOException, NullPointerException;
	
	/**
	 * Returns the scheme this factory uses.
	 * 
	 * @return The protocol scheme used.
	 * @since 2021/11/30
	 */
	@SquirrelJMEVendorApi
	String scheme();
}
