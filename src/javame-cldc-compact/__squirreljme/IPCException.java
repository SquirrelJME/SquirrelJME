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
 * This is thrown when there is an error sending or receiving data from an IPC
 * socket hosted by another process.
 *
 * @since 2016/05/30
 */
public class IPCException
	extends RuntimeException
{
	/**
	 * Initializes the exception with the given message.
	 *
	 * @param __s The message to use.
	 * @since 2016/05/30
	 */
	public IPCException(String __s)
	{
		super(__s);
	}
	
	/**
	 * Initializes the exception with the given error code.
	 *
	 * @param __i The error code.
	 * @since 2016/05/31
	 */
	public IPCException(int __i)
	{
		super(__codeToString(__i));
	}
	
	/**
	 * Translates the given error code to a string.
	 *
	 * @param __i The input error code.
	 * @return The string representation of that error code.
	 * @since 2016/05/31
	 */
	private static String __codeToString(int __i)
	{
		// {@squirreljme.error ZZ0d IPC operation was a success.}
		if (__i >= 0)
			return "ZZ0d";
		
		switch (__i)
		{
				// {@squirreljme.error ZZ0f The IPC socket has been closed.}
			case IPC.ERROR_CONNECTION_CLOSED:
				return "ZZ0f";
				
				// {@squirreljme.error ZZ0g Connection to the remote IPC
				// socket has been refused.}
			case IPC.ERROR_CONNECTION_REFUSED:
				return "ZZ0g";
			
				// {@squirreljme.error ZZ0h The IPC service identifier is
				// already associated with another socket.}
			case IPC.ERROR_SERVICE_IN_USE:
				return "ZZ0h";
			
				// {@squirreljme.error ZZ0i The IPC operation is not
				// permitted.}
			case IPC.ERROR_PERMISSION_DENIED:
				return "ZZ0i";
			
				// {@squirreljme.error ZZ0j A request was made to host an IPC
				// server with an invalid service ID.}
			case IPC.ERROR_INVALID_SERVICE_ID:
				return "ZZ0j";
			
				// {@squirreljme.error ZZ0e Unknown IPC error code.}
			default:
				return "ZZ0e";
		}
	}
}

