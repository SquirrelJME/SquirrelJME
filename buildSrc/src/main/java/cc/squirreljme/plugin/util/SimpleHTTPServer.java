// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.function.BiFunction;

/**
 * This implements a basic HTTP server.
 *
 * @param <S> Session type, if used at all.
 * @since 2020/06/26
 */
public class SimpleHTTPServer<S>
	implements Closeable
{
	/** Connection backlog. */
	private static final int _BACKLOG =
		64;
	
	/** The hostname. */
	public final String hostname;
	
	/** The service port. */
	public final int port;
	
	/** The session used. */
	protected final S session;
	
	/** The socket to listen on. */
	private final ServerSocket _socket;
	
	/**
	 * Initializes the simple server.
	 * 
	 * @param __session The session to use, may be {@code null}.
	 * @throws IOException If the server could not be opened.
	 * @since 2020/06/26
	 */
	@SuppressWarnings("resource")
	public SimpleHTTPServer(S __session)
		throws IOException
	{
		// Open server, use a random port
		ServerSocket socket;
		this._socket = (socket = new ServerSocket(0,
			SimpleHTTPServer._BACKLOG, InetAddress.getLoopbackAddress()));
		
		// Set timeout for any connections accordingly to not hang forever
		socket.setSoTimeout(5_000);
		
		// Where do we connect?
		this.hostname = this._socket.getInetAddress().getHostName();
		this.port = this._socket.getLocalPort();
		
		// Store session
		this.session = __session;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/26
	 */
	@Override
	public final void close()
		throws IOException
	{
		this._socket.close();
	}
	
	/**
	 * Handles the next HTTP call.
	 * 
	 * @param __handler The handler, this may return {@code null} which returns
	 * {@code false} from this method.
	 * @return If {@code __handler} returned {@code null} then {@code false}
	 * will be returned, otherwise {@code true}.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @throws SimpleHTTPProtocolException On protocol errors.
	 * @since 2020/06/26
	 */
	public final boolean next(
		BiFunction<S, SimpleHTTPRequest, SimpleHTTPResponse> __handler)
		throws IOException, NullPointerException, SimpleHTTPProtocolException
	{
		if (__handler == null)
			throw new NullPointerException("NARG");
		
		// Wait for a client request
		try (Socket client = this._socket.accept();
			InputStream in = client.getInputStream())
		{
			// Do not delay TCP at all, this is very short lived
			client.setTcpNoDelay(true);
			
			// Read in the request
			SimpleHTTPRequest request = SimpleHTTPRequest.parse(in);
			
			// Handle the request, always do our own favorite icon
			SimpleHTTPResponse response;
			if ("/favicon.ico".equals(request.path.getPath()))
				response = SimpleHTTPServer.__responseFavIcon();
			else
				response = __handler.apply(this.session, request);
			
			// Stop handling? Do not just close the connection as that can
			// seem a bit rude
			boolean stopHandling = (response == null);
			if (stopHandling)
				response = SimpleHTTPServer.__responseEnd();
			
			// Send back to the client
			try (OutputStream out = client.getOutputStream())
			{
				response.writeTo(out);
				
				// Flush
				out.flush();
			}
			
			// Stop the loop now?
			if (stopHandling)
				return false;
		}
		
		// Could not read, so ignore and try again later
		catch (InterruptedIOException ignored)
		{
			return true;
		}
		
		// Request was handled and one was returned
		return true;
	}
	
	/**
	 * Returns the final server response.
	 * 
	 * @return The response.
	 * @since 2020/06/27
	 */
	private static SimpleHTTPResponse __responseEnd()
	{
		SimpleHTTPResponseBuilder response = new SimpleHTTPResponseBuilder();
		
		response.status(SimpleHTTPStatus.OK);
		response.addHeader("Content-Type", 
			"text/html");
		response.spliceResources(SimpleHTTPResponse.class,
			"end.html", null);
		
		return response.build();
	}
	
	/**
	 * Returns the server's icon.
	 * 
	 * @return The response.
	 * @since 2020/06/27
	 */
	private static SimpleHTTPResponse __responseFavIcon()
	{
		SimpleHTTPResponseBuilder response = new SimpleHTTPResponseBuilder();
		
		response.status(SimpleHTTPStatus.OK);
		response.addHeader("Content-Type", 
			"image/vnd.microsoft.icon");
		
		// Read in the icon
		try (InputStream in = SimpleHTTPResponse.class.getResourceAsStream(
			"favicon.ico.base64");
			ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			// Copy the data
			byte[] buf = new byte[4096];
			for (;;)
			{
				int rc = in.read(buf);
				
				if (rc < 0)
					break;
				
				baos.write(buf, 0, rc);
			}
			
			// Decode it
			response.body(Base64.getMimeDecoder().decode(baos.toByteArray()));
		}
		
		// Failed to decode
		catch (IOException|NullPointerException e)
		{
			throw new RuntimeException("Could not read favicon", e);
		}
		
		return response.build();
	}
}
