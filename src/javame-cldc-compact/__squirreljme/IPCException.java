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
@Deprecated
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
	 * Converts this IPC exception to an error code.
	 *
	 * @return The error code version of this exception.
	 * @since 2016/05/31
	 */
	public int toErrorCode()
	{
		// If there is no message, use a general code
		String msg = getMessage();
		if (msg == null)
			return IPC.ERROR_GENERAL_ERROR;
		
		// Try converting to base36
		try
		{
			// If the message is short, directly use it
			int n = msg.length();
			if (n < 4)
				return (Integer.valueOf(msg, 36) & 0x3FFF_FFFF) | 0x8000_0000;
			
			// Otherwise use the 2nd, 3rd, and 4th characters.
			else
				return (Integer.valueOf(msg.substring(1, 4), 36) &
					0x3FFF_FFFF) | 0x8000_0000;
		}
		
		// Failed to do that
		catch (NumberFormatException e)
		{
			return IPC.ERROR_GENERAL_ERROR;
		}
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
		
		// {@squirreljme.error ZZ0k An unspecified error code was returned.
		// (The error code)}
		if (0 == (__i & 0x4000_0000))
			return String.format("ZZ0k %s",
				Integer.toString((__i & 0x3FFF_FFFF), 36));
		
		// Depends on the code
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

