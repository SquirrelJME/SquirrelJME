// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui.common;

/**
 * This interface contains the protocol information for the display server
 * system.
 *
 * @since 2016/10/15
 */
public interface DisplayProtocol
{
	/**
	 * {@squirreljme.property
	 * net.multiphasicapps.squirreljme.lcdui.client.address=uri
	 * This property when set changes the address that the display client
	 * will use when connecting to a display server.}
	 */
	public static final String DISPLAY_CLIENT_PROPERTY =
		"net.multiphasicapps.squirreljme.lcdui.client.address";
	
	/**
	 * {@squirreljme.property
	 * net.multiphasicapps.squirreljme.lcdui.server.address=uri
	 * This property when set changes the address that the server listens
	 * on for display client connections.}
	 */
	public static final String DISPLAY_SERVER_PROPERTY =
		"net.multiphasicapps.squirreljme.lcdui.server.address";
	
	/** The URI the client uses to connect. */
	public static final String DEFAULT_CLIENT_URI =
		"imc://*:net.multiphasicapps.squirreljme.midp.lcdui.DisplayServer:" +
		"0.0.2;authmode=false";
	
	/** The URI the server uses to host. */
	public static final String DEFAULT_SERVER_URI =
		"imc://:net.multiphasicapps.squirreljme.midp.lcdui.DisplayServer:" +
		"0.0.2;authmode=false";
}

