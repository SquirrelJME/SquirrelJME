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
import java.util.Map;
import java.util.TreeMap;

/**
 * Builder for HTTP responses.
 *
 * @since 2020/06/26
 */
public final class SimpleHTTPResponseBuilder
{
	/** Headers. */
	private final Map<String, String> _headers =
		new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	
	/** The status. */
	public SimpleHTTPStatus status;
	
	/** The body. */
	private byte[] _body;
	
	/**
	 * Sets the header key and value.
	 * 
	 * @param __key The key.
	 * @param __value The value.
	 * @since 2020/06/26
	 */
	public void addHeader(String __key, String __value)
	{
		this._headers.put(__key, __value);
	}
	
	/**
	 * Sets the response body.
	 * 
	 * @param __bytes The bytes to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/26
	 */
	public void body(byte... __bytes)
		throws NullPointerException
	{
		if (__bytes == null)
			throw new NullPointerException("NARG");
		
		this._body = __bytes.clone();
		
		// Ensure that the content length is updated
		this.addHeader("Content-Length",
			Integer.toString(__bytes.length));
	}
	
	/**
	 * Builds the request.
	 * 
	 * @return The request.
	 * @since 2020/06/26
	 */
	public SimpleHTTPResponse build()
	{
		return new SimpleHTTPResponse(this.status, this._headers, this._body);
	}
	
	/**
	 * Splices resources into the body.
	 * 
	 * @param __basis The base class, may be {@code null} if not used.
	 * @param __header The header.
	 * @param __footer The footer.
	 * @param __center The center body data.
	 * @throws NullPointerException If the header/footer were specified and the
	 * basis is {@code null}.
	 * @since 2020/06/27
	 */
	public void spliceResources(Class<?> __basis, String __header,
		String __footer, byte... __center)
		throws NullPointerException
	{
		if (__basis == null && (__header != null || __footer != null))
			throw new NullPointerException("NARG");
		
		// Write the response body
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			// Implant the header
			byte[] buf = new byte[4096];
			if (__header != null)
				try (InputStream in = __basis.getResourceAsStream(__header))
				{
					for (;;)
					{
						int rc = in.read(buf);
						
						if (rc < 0)
							break;
						
						baos.write(buf, 0, rc);
					}
				}
			
			// Write the center content here
			if (__center != null)
				baos.write(__center);
			
			// Implant the footer
			if (__footer != null)
				try (InputStream in = __basis.getResourceAsStream(__footer))
				{
					for (;;)
					{
						int rc = in.read(buf);
						
						if (rc < 0)
							break;
						
						baos.write(buf, 0, rc);
					}
				}
			
			// Send the client the body
			this.body(baos.toByteArray());
		}
		
		// Failed to write the body
		catch (IOException|NullPointerException e)
		{
			throw new RuntimeException("Could not write response body.", e);
		}
	}
	
	/**
	 * Sets the status.
	 * 
	 * @param __status The status to use.
	 * @return {@code this}.
	 * @since 2020/06/26
	 */
	public SimpleHTTPResponseBuilder status(SimpleHTTPStatus __status)
	{
		this.status = __status;
		
		return this;
	}
}
