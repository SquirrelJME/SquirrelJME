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

import cc.squirreljme.runtime.gcf.HTTPAddress;
import cc.squirreljme.runtime.gcf.HTTPClientConnection;
import cc.squirreljme.runtime.gcf.IPAddress;
import cc.squirreljme.runtime.gcf.TCPClientConnection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
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
		throw new todo.TODO();
	}
	
	public static long getBytesWritten(Connection __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	/**
	 * This checks whether the specified protocol is supported.
	 *
	 * @param __uri The scheme to check if it is supported, if there is a colon
	 * then only the characters up to the first colon are used.
	 * @param __server If {@code true} then check for support for being a
	 * server.
	 * @return {@code true} if the protocol is supported.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/12
	 */
	public static boolean isProtocolSupported(String __uri, boolean __server)
		throws NullPointerException
	{
		// Check
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		// Only use up to the colon, if one exists
		int fc = __uri.indexOf(':');
		if (fc > 0)
			__uri = __uri.substring(0, fc);
		
		// Only these protocols are handled
		switch (__uri)
		{
				// Does not matter
			case "comm":
			case "datagram":
			case "dtls":
			case "file":
			case "imc":
			case "multicast":
			case "socket":
			case "ssl":
				return true;
			
				// Client only
			case "http":
			case "https":
				return !__server;
			
				// Unknown
			default:
				return false;
		}
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
	 * @param __uri The URI to open.
	 * @param __mode The open mode of the socket.
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
		// Debug
		todo.DEBUG.note("Open %s", __uri);
		
		// Used to debug connections
		try
		{
			return Connector.__open(__uri, __mode, __timeouts, __opts);
		}
		
		// Print the exception
		catch (IllegalArgumentException|
			IOException|NullPointerException|SecurityException e)
		{
			e.printStackTrace();
			
			throw e;
		}
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
			// {@squirreljme.error EC03 The specified URI is not an input
			// connection. (The URI)}
			if (!(c instanceof InputConnection))
				throw new IllegalArgumentException(String.format("EC03 %s",
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
			// {@squirreljme.error EC04 The specified URI is not an output
			// connection. (The URI)}
			if (!(c instanceof OutputConnection))
				throw new IllegalArgumentException(String.format("EC04 %s",
					__uri));
			
			// Open it
			return ((OutputConnection)c).openOutputStream();
		}
	}
	
	/**
	 * This opens and creates a connection to the specified resource which is
	 * named by the URI.
	 *
	 * @param __uri The URI to open.
	 * @param __mode The open mode of the socket.
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
	private static Connection __open(String __uri, int __mode,
		boolean __timeouts, ConnectionOption<?>... __opts)
		throws ConnectionNotFoundException, IllegalArgumentException,
			IOException, NullPointerException, SecurityException
	{
		// Check
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		// If missing, create
		if (__opts == null)
			__opts = new ConnectionOption<?>[0];
		
		// {@squirreljme.error EC01 The URI does not have a scheme. (The URI)}
		int fc = __uri.indexOf(':');
		if (fc < 0)
			throw new IllegalArgumentException(String.format("EC01 %s",
				__uri));
		String scheme = __uri.substring(0, fc),
			part = __uri.substring(fc + 1);
		
		// Sockets of a given protocol must be of a given class type
		switch (scheme)
		{
				// Communication port, which may be a modem
			case "comm":
				throw new todo.TODO();
				
				// UDP datagrams
			case "datagram":
				throw new todo.TODO();
				
				// SSL UDP datagrams
			case "dtls":
				throw new todo.TODO();
				
				// Local Files
			case "file":
				throw new todo.TODO();
				
				// HTTP
			case "http":
				{
					// Decode HTTP address
					HTTPAddress addr = HTTPAddress.fromUriPart(part);
					
					// Need to connect to the 
					IPAddress ipaddr = addr.ipAddress();
					StreamConnection stream = null;
					try
					{
						// Open stream connection since HTTP is backed off it
						stream = (StreamConnection)open("socket://" + ipaddr,
							__mode, __timeouts, __opts);
						
						// Create connection
						return HTTPClientConnection.connect(addr, stream);
					}
					
					// Close the stream connection if the HTTP connect failed
					catch (IOException e)
					{
						// Close stream
						if (stream != null)
							try
							{
								stream.close();
							}
							catch (Throwable t)
							{
								e.addSuppressed(t);
							}
						
						// Rethrow
						throw e;
					}
				}
				
				// HTTPS
			case "https":
				throw new todo.TODO();
				
				// Intermidlet communication
			case "imc":
				throw new todo.TODO();
				
				// UDP Multicast
			case "multicast":
				throw new todo.TODO();
				
				// TCP Socket
			case "socket":
				{
					// Decode address
					IPAddress addr = IPAddress.fromUriPart(part);
					
					// Creating server
					if (addr.isServer())
						throw new todo.TODO();
					
					// Creating client
					else
						return TCPClientConnection.connect(addr);
				}
				
				// SSL/TLS TCP Socket
			case "ssl":
				throw new todo.TODO();
				
				// {@squirreljme.error EC02 Unhandled URI protocol. (The URI)}.
			default:
				throw new ConnectionNotFoundException(String.format("EC02 %s",
					__uri));
		}
	}
}


