// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This contains the HTTP response header.
 *
 * @since 2019/05/13
 */
public final class HTTPResponseHeader
{
	/** The response code. */
	public final int code;
	
	/** The response message. */
	public final String message;
	
	/** Header key/values. */
	private final Map<String, String> _headers;
	
	/**
	 * Initializes the response header.
	 *
	 * @param __rcode The response code.
	 * @param __rmesg The response message.
	 * @param __hkvs Header key/value pairs.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/13
	 */
	public HTTPResponseHeader(int __rcode, String __rmesg,
		Map<String, String> __hkvs)
		throws NullPointerException
	{
		if (__rmesg == null || __hkvs == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.code = __rcode;
		this.message = __rmesg;
		
		// Copy header values
		Map<String, String> headers = new LinkedHashMap<>();
		for (Map.Entry<String, String> e : __hkvs.entrySet())
		{
			String k = e.getKey(),
				v = e.getValue();
			
			if (k == null || v == null)
				throw new NullPointerException("NARG");
			
			headers.put(k.trim().toLowerCase(),
				v.trim().toLowerCase());
		}
		this._headers = headers;
	}
	
	/**
	 * Obtains the specified header value.
	 *
	 * @param __k The header to get.
	 * @return The header value or {@code null}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/13
	 */
	public final String header(String __k)
		throws NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		return this._headers.get(__k.toLowerCase());
	}
	
	/**
	 * Decodes the HTTP response header.
	 *
	 * @param __in The input bytes.
	 * @return The response header.
	 * @throws IOException On read errors or if the header is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/13
	 */
	public static final HTTPResponseHeader parse(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// HTTP headers
		int rcode = -1;
		String rmesg = "";
		Map<String, String> headers = new LinkedHashMap<>();
		
		// Decode the header line by line
		StringBuilder sb = new StringBuilder();
		for (int ln = 0;; ln++)
		{
			// Clear line
			sb.setLength(0);
			
			// Read until CRLF
			boolean cr = false;
			for (;;)
			{
				int c = __in.read();
				
				// EOF?
				if (c < 0)
					break;
				
				// Flag CR
				if (c == '\r')
					cr = true;
				
				// Read CRLF, line is complete?
				else if (cr && c == '\n')
					break;
				
				// Just another character
				else
				{
					sb.append((char)c);
					cr = false;
				}
			}
			
			// End of header?
			if (sb.length() <= 0)
				break;
			
			// Decode status line?
			String line = sb.toString();
			if (ln == 0)
			{
				/* {@squirreljme.error EC09 Invalid status line. (The line)} */
				if (!line.startsWith("HTTP/1.0 ") &&
					!line.startsWith("HTTP/1.1 "))
					throw new IOException("EC09 " + line);
				
				// Clip off
				line = line.substring(9);
				
				try
				{
					// Code and response message
					int sp = line.indexOf(' ');
					if (sp >= 0)
					{
						rcode = Integer.parseInt(line.substring(0, sp), 10);
						rmesg = line.substring(sp + 1);
					}
					
					// Just the code
					else
					{
						rcode = Integer.parseInt(line.substring(0, sp), 10);
						rmesg = "";
					}
				}
				
				/* {@squirreljme.error EC0a Invalid HTTP status code. (Line)} */
				catch (NumberFormatException e)
				{
					throw new IOException("EC0a " + line);
				}
			}
			
			// A header key/value otherwise
			else
			{
				/* {@squirreljme.error EC0b Invalid header pair. (The line)} */
				int lc = line.indexOf(':');
				if (lc < 0)
					throw new IOException("EC0b " + line);
				
				// Put in header
				headers.put(line.substring(0, lc),
					line.substring(lc + 1));
			}
		}
		
		// Build headed
		return new HTTPResponseHeader(rcode, rmesg, headers);
	}
}

