// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package __squirreljme;

/**
 * This class provides access .
 *
 * IPCs only have single endpoints on each end.
 *
 * @since 2016/05/30
 */
public abstract class IPC
{
	/** The connection to the remote end was closed. */
	public static final int ERROR_CONNECTION_CLOSED =
		-2147483648;
	
	/** The connection to the remote end was refused. */
	public static final int ERROR_CONNECTION_REFUSED =
		-2147483647;
	
	/** The service is already being hosted by this process. */
	public static final int ERROR_SERVICE_IN_USE =
		-2147483646;
	
	/** The alternative implementation to use. */
	final IPCAlternative _alternate;
	
	/**
	 * Initializes the base IPC information.
	 *
	 * @param __ua Use alternative implementation?
	 * @param __alt The alternative implementation to use.
	 * @throws NullPointerException If an alternative is being used and one
	 * was not specified.
	 * @since 2016/05/30
	 */
	IPC(boolean __ua, IPCAlternative __alt)
		throws NullPointerException
	{
		// Check
		if (__ua && __alt == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._alternate = __alt;
	}
	
	/**
	 * Accepts a socket connection from the given socket so that the server
	 * may communicate with the client.
	 *
	 * @param __sock The socket to accept connections on.
	 * @return The socket used for the accepted client, zero if there were no
	 * newly connected clients, or a negative value on error.
	 * @see __socketListen(int)
	 * @since 2016/05/30
	 */
	static final int socketAccept(int __sock)
	{
		// {@squirreljme.error ZZ07 Pure virtual call of IPC accept.}
		throw new IPCException("ZZ07");
	}
	
	/**
	 * Creates a new socket which connects to the given port on another
	 * process.
	 *
	 * @param __host The host process ID to connect to.
	 * @param __service The service on that host process to connect to.
	 * @return The socket identifier or a negative value on error.
	 * @since 2016/05/30
	 */
	static final int socketConnect(int __host, int __service)
	{
		// {@squirreljme.error ZZ05 Pure virtual call of IPC connect.}
		throw new IPCException("ZZ05");
	}
	
	/**
	 * Listens for incoming IPC connections on the given port.
	 *
	 * @param __port The port to listen for connections on.
	 * @return The socket identifier for the newly created socket or a negative
	 * value on error.
	 * @see __socketAccept(int)
	 * @since 2016/05/30
	 */
	static final int socketListen(int __port)
	{
		// {@squirreljme.error ZZ03 Pure virtual call of IPC listen.}
		throw new IPCException("ZZ03");
	}
	
	/**
	 * Returns the number of bytes which are ready to be read from the source
	 * socket at this time.
	 *
	 * @param __sock The socket to read the number of available bytes from.
	 * @return The number of bytes available (may be zero for no bytes),
	 * negative return values indicate error.
	 * @since 2016/05/30
	 */
	static final int socketReady(int __sock)
	{
		// {@squirreljme.error ZZ06 Pure virtual call of IPC ready.}
		throw new IPCException("ZZ06");
	}
	
	/**
	 * Receives data from the specified socket blocking until data is
	 * available.
	 *
	 * @param __sock The IPC socket ID.
	 * @param __b The destination buffer to write bytes into.
	 * @param __o The offset to start writing bytes into.
	 * @param __l The number of bytes to write.
	 * @return The number of bytes read, a negative value indicates an error.
	 * @since 2016/05/30
	 */
	static final int socketReceive(int __sock, byte[] __b,
		int __o, int __l)
	{
		// {@squirreljme.error ZZ01 Pure virtual call of IPC receive.}
		throw new IPCException("ZZ01");
	}
	
	/**
	 * Sends data to the destination endpoint in the socket.
	 *
	 * @param __sock The IPC socket ID.
	 * @param __b The source buffer to read bytes from.
	 * @param __o The offset to the start of the buffer.
	 * @param __l The number of bytes to write.
	 * @return Zero on success, a negative value indicates an error.
	 * @since 2016/05/30
	 */
	static final int socketSend(int __sock, byte[] __b, int __o,
		int __l)
	{
		// {@squirreljme.error ZZ02 Pure virtual call of IPC send.}
		throw new IPCException("ZZ02");
	}
}

