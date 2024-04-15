// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a HTTP response.
 *
 * @since 2020/06/26
 */
public final class SimpleHTTPResponse
	implements SimpleHTTPData
{
	/** Status. */
	public final SimpleHTTPStatus status;
	
	/** Headers. */
	public final Map<String, String> headers;
	
	/** The response body. */
	private final byte[] _body;
	
	/**
	 * Initializes the response.
	 * 
	 * @param __status The status.
	 * @param __headers The headers.
	 * @param __body The body, is optional.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/26
	 */
	public SimpleHTTPResponse(SimpleHTTPStatus __status,
		Map<String, String> __headers, byte... __body)
		throws NullPointerException
	{
		if (__status == null || __headers == null)
			throw new NullPointerException("NARG");
		
		// Copy everything in the map
		Map<String, String> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		map.putAll(__headers);
		this.headers = Collections.<String, String>unmodifiableMap(map);
		
		this.status = __status;
		this._body = (__body == null ? new byte[0] : __body.clone());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/26
	 */
	@Override
	public String toString()
	{
		return String.format("{status=%s, headers=%s, body=%d bytes}",
			this.status, this.headers, this._body.length);
	}
	
	/**
	 * Writes to the given output.
	 * 
	 * @param __out The stream to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/26
	 */
	public void writeTo(OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		// Write response line
		SimpleHTTPStatus status = this.status;
		__out.write(String.format("HTTP/1.1 %d %s\r\n", status.code,
			status.name()).getBytes(StandardCharsets.UTF_8));
		
		// Write headers
		for (Map.Entry<String, String> header : this.headers.entrySet())
		{
			__out.write(header.getKey().getBytes(StandardCharsets.UTF_8));
			__out.write(':');
			__out.write(' ');
			__out.write(header.getValue().getBytes(StandardCharsets.UTF_8));
			__out.write('\r');
			__out.write('\n');
		}
		
		// Whitespace before there is content
		__out.write('\r');
		__out.write('\n');
		
		// Write the body
		__out.write(this._body);
		
		// Flush everything to ensure it is written
		__out.flush();
	}
}
