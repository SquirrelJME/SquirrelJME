// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import java.io.IOException;

public interface SocketConnection
	extends StreamConnection
{
	/** Use the nagle algorithm? Zero disables, non-zero enables. */
	byte DELAY =
		0;
	
	/** Keep connections alive? Zero disables, non-zero enables. */
	byte KEEPALIVE =
		2;
	
	/**
	 * Time to wait in seconds before closing connections. Zero disables and
	 * only positive values are used.
	 */
	byte LINGER =
		1;
	
	/** Receive buffer size? Zero is default, only positive is permitted. */
	byte RCVBUF =
		3;
	
	/** Send buffer size? Zero is default, only positive is permitted. */
	byte SNDBUF =
		4;
	
	/**
	 * Sets the timeout on blocking read/write. Zero is default, otherwise a
	 * positive value in milliseconds.
	 */
	byte TIMEOUT =
		5;
	
	AccessPoint getAccessPoint()
		throws IOException;
	
	String getAddress()
		throws IOException;
	
	String getLocalAddress()
		throws IOException;
	
	int getLocalPort()
		throws IOException;
	
	int getPort()
		throws IOException;
	
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
	void setSocketOption(byte __o, int __v)
		throws IllegalArgumentException, IOException;
}


