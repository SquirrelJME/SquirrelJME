// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This contains the response of the HTTP request.
 *
 * @since 2019/05/13
 */
public final class HTTPResponse
{
	/** The header. */
	public final HTTPResponseHeader header;
	
	/** The data bytes. */
	private final byte[] _data;
	
	/**
	 * Initializes the response.
	 *
	 * @param __h The header.
	 * @param __d The data.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/13
	 */
	public HTTPResponse(HTTPResponseHeader __h, byte[] __d)
		throws NullPointerException
	{
		if (__h == null || __d == null)
			throw new NullPointerException("NARG");
		
		this.header = __h;
		this._data = __d.clone();
	}
	
	/**
	 * Returns the input stream of the body data.
	 *
	 * @return The input stream for the body.
	 * @since 2019/05/13
	 */
	public final InputStream inputStream()
	{
		return new ByteArrayInputStream(this._data);
	}
	
	/**
	 * Parses the HTTP response.
	 *
	 * @param __b The input bytes.
	 * @return The parsed response.
	 * @throws IOException If the response is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/13
	 */
	public static final HTTPResponse parse(byte[] __b)
		throws IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		return HTTPResponse.parse(new ByteArrayInputStream(__b));
	}
	
	/**
	 * Parses the HTTP response.
	 *
	 * @param __in The input bytes.
	 * @return The parsed response.
	 * @throws IOException If the response is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/13
	 */
	public static final HTTPResponse parse(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Parse the header first
		HTTPResponseHeader header = HTTPResponseHeader.parse(__in);
		
		// Decode content length
		int length;
		try
		{
			// Decode length
			String rl = header.header("content-length");
			if (rl != null)
				length = Integer.parseInt(rl, 10);
			
			// Variable length...
			else
				length = -1;
		}
		
		/* {@squirreljme.error EC07 Invalid content length.} */
		catch (NumberFormatException e)
		{
			throw new IOException("EC07", e);
		}
		
		// Length of specific size
		byte[] bytes;
		if (length >= 0)
		{
			bytes = new byte[length];
			for (int at = 0; at < length;)
			{
				int rc = __in.read(bytes, at, length - at);
				
				/* {@squirreljme.error EC08 The HTTP body was too small. (The
				read length; The expected size)} */
				if (rc < 0)
					throw new IOException("EC08 " + at + " " + length);
				
				// Move at up
				at += rc;
			}
		}
		
		// Variable length
		else
		{
			// Read in all the data
			byte[] buf = new byte[512];
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream(1024))
			{
				for (;;)
				{
					int rc = __in.read(buf);
					
					if (rc < 0)
						break;
					
					baos.write(buf, 0, rc);
				}
				
				// Done
				bytes = baos.toByteArray();
			}
		}
		
		// Debug
		Debugging.debugNote(" <- %d", bytes.length);
		
		// Build response
		return new HTTPResponse(header, bytes);
	}
}

