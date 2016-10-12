// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class is used to create new connections via the generic connection
 * framework. All connection resources are URIs which describe the resource
 * to be accessed.
 *
 * @since 2016/10/12
 */
public class Connector
{
	/** Access mode to allow reading from the connection stream. */
	public static final int READ =
		1;
	
	/** Access mode to allow for writing to the connection stream. */
	public static final int WRITE =
		2;
	
	/** Access mode to allow for read and writing from/to the stream. */
	public static final int READ_WRITE =
		READ | WRITE;
	
	/**
	 * Not used.
	 *
	 * @since 2016/10/12
	 */
	private Connector()
	{
	}
	
	public static long getBytesRead(Connection __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	public static long getBytesWritten(Connection __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	public static boolean isProtocolSupported(String __a, boolean __b)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Invokes:
	 * {@code open(__uri, READ_WRITE, false, (ConnectionOption<?>[])null)}.
	 *
	 * @param __uri As forwarded.
	 * @return As forwarded.
	 * @throws IOException As forwarded.
	 * @since 2016/10/12
	 */
	public static Connection open(String __uri)
		throws IOException
	{
		return open(__uri, READ_WRITE, false, (ConnectionOption<?>[])null);
	}
	
	/**
	 * Invokes:
	 * {@code open(__uri, READ_WRITE, false, __opts)}.
	 *
	 * @param __uri As forwarded.
	 * @param __opts As forwarded.
	 * @return As forwarded.
	 * @throws IOException As forwarded.
	 * @since 2016/10/12
	 */
	public static Connection open(String __uri, ConnectionOption<?>... __opts)
		throws IOException
	{
		return open(__uri, READ_WRITE, false, __opts);
	}
	
	/**
	 * Invokes:
	 * {@code open(__uri, __mode, false, (ConnectionOption<?>[])null)}.
	 *
	 * @param __uri As forwarded.
	 * @param __mode As forwarded.
	 * @return As forwarded.
	 * @throws IOException As forwarded.
	 * @since 2016/10/12
	 */
	public static Connection open(String __uri, int __mode)
		throws IOException
	{
		return open(__uri, __mode, false, (ConnectionOption<?>[])null);
	}
	
	/**
	 * Invokes:
	 * {@code open(__uri, __mode, false, __opts)}.
	 *
	 * @param __uri As forwarded.
	 * @param __mode As forwarded.
	 * @param __opts As forwarded.
	 * @return As forwarded.
	 * @throws IOException As forwarded.
	 * @since 2016/10/12
	 */
	public static Connection open(String __uri, int __mode,
		ConnectionOption<?>... __opts)
		throws IOException
	{
		return open(__uri, __mode, false, __opts);
	}
	
	/**
	 * Invokes:
	 * {@code open(__uri, __mode, __timeouts, (ConnectionOption<?>[])null)}.
	 *
	 * @param __uri As forwarded.
	 * @param __mode As forwarded.
	 * @param __timeouts As forwarded.
	 * @return As forwarded.
	 * @throws IOException As forwarded.
	 * @since 2016/10/12
	 */
	public static Connection open(String __uri, int __mode, boolean __timeouts)
		throws IOException
	{
		return open(__uri, __mode, __timeouts, (ConnectionOption<?>[])null);
	}
	
	/**
	 * This opens and creates a connection to the specified resource which is
	 * named by the URI.
	 *
	 * @param __timeouts This is a hint to the connection system that it is
	 * acceptable for {@link InterruptedIOException}s to be generated. This
	 * is not required to be followed.
	 * @param __opts Options which modify the properties of a stream.
	 * @throws ConnectionNotFoundException Either the protocol is not supported
	 * or the target to connect to could not be located.
	 * @throws IllegalArgumentException If a parameter is not valid.
	 * @throws IOException On other unspecified exceptions.
	 * @throws NullPointerException If no URI was specified.
	 * @throws SecurityException If access to the protocol is not permitted.
	 * @since 2016/10/12
	 */
	public static Connection open(String __uri, int __mode, boolean __timeouts,
		ConnectionOption<?>... __opts)
		throws ConnectionNotFoundException, IllegalArgumentException,
			IOException, NullPointerException, SecurityException
	{
		// Check
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	/**
	 * Invokes {@code new DataInputStream(openInputStream(__uri))}.
	 *
	 * @param __uri The URI to open.
	 * @return The data stream.
	 * @throws IOException As forwarded.
	 * @since 2016/10/12
	 */
	public static DataInputStream openDataInputStream(String __uri)
		throws IOException
	{
		return new DataInputStream(openInputStream(__uri));
	}
	
	/**
	 * Invokes {@code new DataOutputStream(openOutputStream(__uri))}.
	 *
	 * @param __uri The URI to open.
	 * @return The data stream.
	 * @throws IOException As forwarded.
	 * @since 2016/10/12
	 */
	public static DataOutputStream openDataOutputStream(String __uri)
		throws IOException
	{
		return new DataOutputStream(openOutputStream(__uri));
	}
	
	/**
	 * This opens a connection, obtains the input stream, then closes the
	 * connection.
	 *
	 * @return An input stream for the given connection.
	 * @throws IllegalArgumentException If the connection is not an
	 * {@link InputConnection}.
	 * @throws IOException On open errors.
	 * @since 2016/10/12
	 */
	public static InputStream openInputStream(String __uri)
		throws IllegalArgumentException, IOException
	{
		// Open it, then close it
		try (Connection c = open(__uri))
		{
			// {@squirreljme.error EC01 The specified URI is not an input
			// connection. (The URI)}
			if (!(c instanceof InputConnection))
				throw new IllegalArgumentException(String.format("EC01 %s",
					__uri));
			
			// Open it
			return ((InputConnection)c).openInputStream();
		}
	}
	
	/**
	 * This opens a connection, obtains the output stream, then closes the
	 * connection.
	 *
	 * @return An output stream for the given connection.
	 * @throws IllegalArgumentException If the connection is not an
	 * {@link OutputConnection}.
	 * @throws IOException On open errors.
	 * @since 2016/10/12
	 */
	public static OutputStream openOutputStream(String __uri)
		throws IllegalArgumentException, IOException
	{
		// Open it, then close it
		try (Connection c = open(__uri))
		{
			// {@squirreljme.error EC02 The specified URI is not an output
			// connection. (The URI)}
			if (!(c instanceof OutputConnection))
				throw new IllegalArgumentException(String.format("EC02 %s",
					__uri));
			
			// Open it
			return ((OutputConnection)c).openOutputStream();
		}
	}
}


