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
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * Represents a HTTP request.
 *
 * @since 2020/06/26
 */
public final class SimpleHTTPRequest
	implements SimpleHTTPData
{
	/** The HTTP method. */
	public final SimpleHTTPMethod method;
	
	/** The requested path. */
	public final URI path;
	
	/** HTTP headers. */
	public final Map<String, String> headers;
	
	/** The body of the request. */
	private final byte[] _body;
	
	/**
	 * Initializes the HTTP request.
	 * 
	 * @param __method The method.
	 * @param __path The path.
	 * @param __headers The headers.
	 * @param __body The body, this is optional.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/26
	 */
	public SimpleHTTPRequest(SimpleHTTPMethod __method, URI __path,
		Map<String, String> __headers, byte[] __body)
		throws NullPointerException
	{
		if (__method == null || __path == null || __headers == null)
			throw new NullPointerException("NARG");
		
		// Copy everything in the map
		Map<String, String> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		map.putAll(__headers);
		
		// Store all the request details
		this.method = __method;
		this.path = __path;
		this.headers = Collections.<String, String>unmodifiableMap(map);
		this._body = (__body == null ? new byte[0] : __body.clone());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/26
	 */
	@Override
	public final String toString()
	{
		return String.format(
			"{method=%s, path=%s, headers=%s, body=%d bytes}",
			this.method, this.path, this.headers, this._body.length);
	}
	
	/**
	 * Parses the HTTP request.
	 * 
	 * This does not strictly conform to HTTP but is close enough for it to
	 * work fine.
	 * 
	 * @param __in The stream to read from.
	 * @return The request.
	 * @throws IOException On read errors.
	 * @throws SimpleHTTPProtocolException If the protocol is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/26
	 */
	@SuppressWarnings("resource")
	public static SimpleHTTPRequest parse(InputStream __in)
		throws IOException, NullPointerException, SimpleHTTPProtocolException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// HTTP requests are in the following form:
		// GET / HTTP/1.1
		// Host: www.example.com
		SimpleHTTPMethod httpMethod;
		URI httpPath;
		Map<String, String> headers =
			new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		ByteArrayOutputStream body = new ByteArrayOutputStream();
		
		// Read the request line
		String requestLine = SimpleHTTPRequest.__readLine(__in);
		
		// The request line must be in 3 parts
		String[] requestSplice = requestLine.split(Pattern.quote(" "));
		if (requestSplice.length < 3)
			throw new SimpleHTTPProtocolException(
				"Invalid HTTP request: " + requestLine);
		
		// Use these for later
		httpMethod = SimpleHTTPMethod.valueOf(
			requestSplice[0].toUpperCase());
		httpPath = URI.create(requestSplice[1]);
		
		// The following is the header data
		for (;;)
		{
			String headerLine = SimpleHTTPRequest.__readLine(__in);
			
			// No more headers to send
			if (headerLine.isEmpty())
				break;
			
			// This must exist
			int col = headerLine.indexOf(':');
			if (col < 0)
				throw new SimpleHTTPProtocolException(
					"Expected colon in header: " + headerLine);
			
			// Store header for later
			headers.put(headerLine.substring(0, col).trim(),
				headerLine.substring(col + 1).trim());
		}
		
		// The rest is just character data for the body, if there is one
		if (httpMethod.hasRequestBody)
		{
			// Need to know the content length
			String contentLengthStr = headers.get("Content-Length");
			if (contentLengthStr == null)
				throw new SimpleHTTPProtocolException("No Content-Length");
			
			// Parse content length as value
			int contentLength;
			try
			{
				contentLength = Integer.parseInt(contentLengthStr, 10);
			}
			catch (NumberFormatException e)
			{
				throw new SimpleHTTPProtocolException(
					"Invalid Content-Length: " + contentLengthStr, e);
			}
			
			// Read in all the data
			for (int i = 0; i < contentLength; i++)
			{
				int rc = __in.read();
				
				if (rc < 0)
					throw new SimpleHTTPProtocolException("Got EOF in body");
				
				body.write(rc);
			}
		}
		
		// Build request
		return new SimpleHTTPRequest(httpMethod, httpPath, headers,
			body.toByteArray());
	}
	
	/**
	 * Reads an input line from the stream.
	 * 
	 * @param __in The stream to read from.
	 * @return The read line or {@code null} on end of file.
	 * @throws IOException On read errors.
	 * @throws SimpleHTTPProtocolException If the protocol is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	private static String __readLine(InputStream __in)
		throws IOException, NullPointerException, SimpleHTTPProtocolException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Write to the buffer
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			// Read single line
			for (;;)
			{
				int rc = __in.read();
				
				// For EOF just treat as the end of the data
				if (rc < 0)
					break;
				
				// New line?
				if (rc == '\n')
					break;
				
				// Ignore CR
				else if (rc == '\r')
					continue;
				
				// Append to the buffer
				baos.write(rc);
			}
			
			// Encode to UTF-8
			return new String(baos.toByteArray(), StandardCharsets.UTF_8);
		}
	}
}
