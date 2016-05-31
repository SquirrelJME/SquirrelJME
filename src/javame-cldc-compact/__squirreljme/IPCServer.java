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
 * This class handles the server side aspects of the IPC socket.
 *
 * @since 2016/05/30
 */
public final class IPCServer
	extends IPC
{
	/** Server socket handle. */
	private final int _handle;
	
	/**
	 * This creates a new server socket which is used by clients which want
	 * to access a service that the current process provides.
	 *
	 * @param __service The service identifier to provide.
	 * @throws IllegalArgumentException If the service identifier is negative.
	 * @throws IPCException If the service could not be hosted.
	 * @since 2016/05/30
	 */
	public IPCServer(int __service)
		throws IllegalArgumentException, IPCException
	{
		this(false, null, __service);
	}
	/**
	 * This creates a new server socket which is used by clients which want
	 * to access a service that the current process provides.
	 *
	 * @param __alt Alternative socket implementation to use instead of the
	 * kernel based interface.
	 * @param __service The service identifier to provide.
	 * @throws IllegalArgumentException If the service identifier is negative.
	 * @throws IPCException If the service could not be hosted.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/30
	 */
	public IPCServer(IPCAlternative __alt, int __service)
		throws IllegalArgumentException, IPCException, NullPointerException
	{
		this(true, __alt, __service);
	}
	
	/**
	 * This creates a new server socket which is used by clients which want
	 * to access a service that the current process provides.
	 *
	 * @param __ua Use alternative implementation?
	 * @param __alt Alternative socket implementation to use instead of the
	 * kernel based interface.
	 * @param __service The service identifier to provide.
	 * @throws IllegalArgumentException If the service identifier is negative.
	 * @throws IPCException If the service could not be hosted.
	 * @since 2016/05/30
	 */
	IPCServer(boolean __ua, IPCAlternative __alt, int __service)
		throws IllegalArgumentException, IPCException
	{
		super(__ua, __alt);
		
		// {@squirreljme.error ZZ09 Negative service IDs are not permitted.}
		if (__service < 0)
			throw new IllegalArgumentException("ZZ09");
		
		// Initialize the server side socket
		int handle = (__ua ? __alt.listen(__service) :
			IPC.socketListen(__service));
		if (handle < 0)
			throw new IPCException(handle);
		this._handle = handle;
	}
}

