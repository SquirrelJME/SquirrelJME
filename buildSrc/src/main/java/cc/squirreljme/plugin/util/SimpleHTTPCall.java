// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * Represents the result of a simple HTTP call.
 *
 * @since 2020/06/24
 */
public final class SimpleHTTPCall
{
	/**
	 * Parses the HTTP call.
	 * 
	 * @param __in The stream to parse.
	 * @return The result of the call.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/24
	 */
	public static SimpleHTTPCall parse(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Sends the HTTP request down the stream.
	 * 
	 * @param __out The stream to write to.
	 * @param __uri The URI being requested.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/24
	 */
	public static void request(OutputStream __out, URI __uri)
		throws IOException, NullPointerException
	{
		if (__out == null || __uri == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}
