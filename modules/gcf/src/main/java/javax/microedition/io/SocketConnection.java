// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;

@Api
public interface SocketConnection
	extends StreamConnection
{
	/** Use the nagle algorithm? Zero disables, non-zero enables. */
	@Api
	byte DELAY =
		0;
	
	/** Keep connections alive? Zero disables, non-zero enables. */
	@Api
	byte KEEPALIVE =
		2;
	
	/**
	 * Time to wait in seconds before closing connections. Zero disables and
	 * only positive values are used.
	 */
	@Api
	byte LINGER =
		1;
	
	/** Receive buffer size? Zero is default, only positive is permitted. */
	@Api
	byte RCVBUF =
		3;
	
	/** Send buffer size? Zero is default, only positive is permitted. */
	@Api
	byte SNDBUF =
		4;
	
	/**
	 * Sets the timeout on blocking read/write. Zero is default, otherwise a
	 * positive value in milliseconds.
	 */
	@Api
	byte TIMEOUT =
		5;
	
	@Api
	AccessPoint getAccessPoint()
		throws IOException;
	
	@Api
	String getAddress()
		throws IOException;
	
	@Api
	String getLocalAddress()
		throws IOException;
	
	@Api
	int getLocalPort()
		throws IOException;
	
	@Api
	int getPort()
		throws IOException;
	
	@Api
	int getSocketOption(byte __o)
		throws IllegalArgumentException, IOException;
	
	/**
	 * Sets an option for the socket.
	 *
	 * @param __o The option to use.
	 * @param __v The value to use.
	 * @throws IllegalArgumentException If the option is not valid.
	 * @throws IOException If it could not be set.
	 * @since 2019/05/12
	 */
	@Api
	void setSocketOption(byte __o, int __v)
		throws IllegalArgumentException, IOException;
}


