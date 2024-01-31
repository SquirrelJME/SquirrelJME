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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.gcf.CustomConnectionFactory;
import cc.squirreljme.runtime.gcf.HTTPAddress;
import cc.squirreljme.runtime.gcf.HTTPClientConnection;
import cc.squirreljme.runtime.gcf.IPAddress;
import cc.squirreljme.runtime.gcf.IPConnectionFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.util.ServiceLoader;
import org.intellij.lang.annotations.Language;

/**
 * This class is used to create new connections via the generic connection
 * framework. All connection resources are URIs which describe the resource
 * to be accessed.
 *
 * @since 2016/10/12
 */
@Api
public class Connector
{
	/** Access mode to allow reading from the connection stream. */
	@Api
	public static final int READ =
		1;
	
	/** Access mode to allow for writing to the connection stream. */
	@Api
	public static final int WRITE =
		2;
	
	/** Access mode to allow for read and writing from/to the stream. */
	@Api
	public static final int READ_WRITE = Connector.READ | Connector.WRITE;
	
	/** Services support. */
	private static final ServiceLoader<CustomConnectionFactory> _SERVICES =
		ServiceLoader.load(CustomConnectionFactory.class);
	
	/**
	 * Not used.
	 *
	 * @since 2016/10/12
	 */
	private Connector()
	{
	}
	
	@Api
	public static long getBytesRead(Connection __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static long getBytesWritten(Connection __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
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
	@Api
	public static boolean isProtocolSupported(
		@Language("http-url-reference") String __uri, boolean __server)
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
				// Is there a matching custom connector 
				synchronized (Connector.class)
				{
					for (CustomConnectionFactory custom : Connector._SERVICES)
						if (__uri.equalsIgnoreCase(custom.scheme()))
							return true;
				}
				
				// Not supported
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
	@Api
	public static Connection open(@Language("http-url-reference") String __uri)
		throws IOException
	{
		return Connector.open(__uri, Connector.READ_WRITE, false,
			(ConnectionOption<?>[])null);
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
	@Api
	public static Connection open(@Language("http-url-reference") String __uri,
		ConnectionOption<?>... __opts)
		throws IOException
	{
		return Connector.open(__uri, Connector.READ_WRITE,
			false, __opts);
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
	@Api
	public static Connection open(@Language("http-url-reference") String __uri,
		int __mode)
		throws IOException
	{
		return Connector.open(__uri, __mode, false,
			(ConnectionOption<?>[])null);
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
	@Api
	public static Connection open(@Language("http-url-reference") String __uri,
		int __mode, ConnectionOption<?>... __opts)
		throws IOException
	{
		return Connector.open(__uri, __mode, false, __opts);
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
	@Api
	public static Connection open(@Language("http-url-reference") String __uri,
		int __mode, boolean __timeouts)
		throws IOException
	{
		return Connector.open(__uri, __mode, __timeouts,
			(ConnectionOption<?>[])null);
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
	 * @return The opened connection.
	 * @throws ConnectionNotFoundException Either the protocol is not supported
	 * or the target to connect to could not be located.
	 * @throws IllegalArgumentException If a parameter is not valid.
	 * @throws IOException On other unspecified exceptions.
	 * @throws NullPointerException If no URI was specified.
	 * @throws SecurityException If access to the protocol is not permitted.
	 * @since 2016/10/12
	 */
	@Api
	public static Connection open(@Language("http-url-reference") String __uri,
		int __mode, boolean __timeouts, ConnectionOption<?>... __opts)
		throws ConnectionNotFoundException, IllegalArgumentException,
			IOException, NullPointerException, SecurityException
	{
		// Debug
		Debugging.debugNote("Open %s", __uri);
		
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
	@Api
	public static DataInputStream openDataInputStream(
		@Language("http-url-reference") String __uri)
		throws IOException
	{
		return new DataInputStream(Connector.openInputStream(__uri));
	}
	
	/**
	 * Invokes {@code new DataOutputStream(openOutputStream(__uri))}.
	 *
	 * @param __uri The URI to open.
	 * @return The data stream.
	 * @throws IOException As forwarded.
	 * @since 2016/10/12
	 */
	@Api
	public static DataOutputStream openDataOutputStream(
		@Language("http-url-reference") String __uri)
		throws IOException
	{
		return new DataOutputStream(Connector.openOutputStream(__uri));
	}
	
	/**
	 * This opens a connection, obtains the input stream, then closes the
	 * connection.
	 *
	 * @param __uri The URI to open.
	 * @return An input stream for the given connection.
	 * @throws IllegalArgumentException If the connection is not an
	 * {@link InputConnection}.
	 * @throws IOException On open errors.
	 * @since 2016/10/12
	 */
	@Api
	public static InputStream openInputStream(
		@Language("http-url-reference") String __uri)
		throws IllegalArgumentException, IOException
	{
		// Open it, then close it
		try (Connection c = Connector.open(__uri))
		{
			/* {@squirreljme.error EC0z The specified URI is not an input
			connection. (The URI)} */
			if (!(c instanceof InputConnection))
				throw new IllegalArgumentException(String.format("EC0z %s",
					__uri));
			
			// Open it
			return ((InputConnection)c).openInputStream();
		}
	}
	
	/**
	 * This opens a connection, obtains the output stream, then closes the
	 * connection.
	 *
	 * @param __uri The URI to open.
	 * @return An output stream for the given connection.
	 * @throws IllegalArgumentException If the connection is not an
	 * {@link OutputConnection}.
	 * @throws IOException On open errors.
	 * @since 2016/10/12
	 */
	@Api
	public static OutputStream openOutputStream(
		@Language("http-url-reference") String __uri)
		throws IllegalArgumentException, IOException
	{
		// Open it, then close it
		try (Connection c = Connector.open(__uri))
		{
			/* {@squirreljme.error EC10 The specified URI is not an output
			connection. (The URI)} */
			if (!(c instanceof OutputConnection))
				throw new IllegalArgumentException(String.format("EC10 %s",
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
	@Api
	private static Connection __open(
		@Language("http-url-reference") String __uri, int __mode,
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
		
		/* {@squirreljme.error EC11 The URI does not have a scheme.
		(The URI)} */
		int fc = __uri.indexOf(':');
		if (fc < 0)
			throw new IllegalArgumentException(String.format("EC11 %s",
				__uri));
		
		String scheme = __uri.substring(0, fc);
		String part = __uri.substring(fc + 1);
		
		// Sockets of a given protocol must be of a given class type
		switch (scheme)
		{
				// Communication port, which may be a modem
			case "comm":
				throw Debugging.todo();
				
				// UDP datagrams
			case "datagram":
				throw Debugging.todo();
				
				// SSL UDP datagrams
			case "dtls":
				throw Debugging.todo();
				
				// Local Files
			case "file":
				throw Debugging.todo();
				
				// HTTP
			case "http":
				return HTTPClientConnection.connectDefault(
					HTTPAddress.fromUriPart(part));
				
				// HTTPS
			case "https":
				throw Debugging.todo();
				
				// Intermidlet communication
			case "imc":
				throw Debugging.todo();
				
				// UDP Multicast
			case "multicast":
				throw Debugging.todo();
				
				// TCP Socket
			case "socket":
				{
					// Decode address
					IPAddress addr = IPAddress.fromUriPart(part);
					
					// Creating server
					if (addr.isServer())
						throw Debugging.todo();
					
					// Creating client
					else
					{
						IPConnectionFactory pf = IPConnectionFactory.factory();
						return pf.tcpClientConnect(pf.resolveAddress(addr));
					}
				}
				
				// SSL/TLS TCP Socket
			case "ssl":
				throw Debugging.todo();
		}
		
		// Is there a matching custom connector 
		synchronized (Connector.class)
		{
			for (CustomConnectionFactory custom : Connector._SERVICES)
				if (scheme.equalsIgnoreCase(custom.scheme()))
					return custom.connect(part, __mode, __timeouts, __opts);
		}
		
		/* {@squirreljme.error EC12 Unhandled URI protocol. (The URI)} */
		throw new ConnectionNotFoundException(String.format("EC12 %s",
			__uri));
	}
}


