// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

/**
 * This represents the HTTP method.
 *
 * @since 2020/06/26
 */
public enum SimpleHTTPMethod
{
	/** Get. */
	GET(false, true),
	
	/** Put. */
	PUT(true, false),
	
	/** Post. */
	POST(true, true),
	
	/* End. */
	;
	
	/** Has request body? */
	public final boolean hasRequestBody;
	
	/** Has response body? */
	public final boolean hasResponseBody;
	
	/**
	 * Represents the HTTP method.
	 * 
	 * @param __hasRequestBody Does this have a request body?
	 * @param __hasResponseBody Does this have a response body?
	 * @since 2020/06/26
	 */
	SimpleHTTPMethod(boolean __hasRequestBody, boolean __hasResponseBody)
	{
		this.hasRequestBody = __hasRequestBody;
		this.hasResponseBody = __hasResponseBody;
	}
}
