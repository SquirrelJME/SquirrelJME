// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
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
	public static final byte DELAY =
		0;
	
	/** Keep connections alive? Zero disables, non-zero enables. */
	public static final byte KEEPALIVE =
		2;
	
	/**
	 * Time to wait in seconds before closing connections. Zero disables and
	 * only positive values are used.
	 */
	public static final byte LINGER =
		1;
	
	/** Receive buffer size? Zero is default, only positive is permitted. */
	public static final byte RCVBUF =
		3;
	
	/** Send buffer size? Zero is default, only positive is permitted. */
	public static final byte SNDBUF =
		4;
	
	/**
	 * Sets the timeout on blocking read/write. Zero is default, otherwise a
	 * positive value in milliseconds.
	 */
	public static final byte TIMEOUT =
		5;
	
	public abstract AccessPoint getAccessPoint()
		throws IOException;
	
	public abstract String getAddress()
		throws IOException;
	
	public abstract String getLocalAddress()
		throws IOException;
	
	public abstract int getLocalPort()
		throws IOException;
	
	public abstract int getPort()
		throws IOException;
	
	public abstract int getSocketOption(byte __o)
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
	public abstract void setSocketOption(byte __o, int __v)
		throws IllegalArgumentException, IOException;
}


