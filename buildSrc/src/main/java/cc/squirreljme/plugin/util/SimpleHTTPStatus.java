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
 * HTTP status.
 *
 * @since 2020/06/26
 */
public enum SimpleHTTPStatus
{
	/** Okay. */
	OK(200),
	
	/** Not found. */
	NOT_FOUND(404),
	
	/* End. */
	;
	
	/** The status code. */
	public final int code;
	
	/**
	 * Initializes the status.
	 * 
	 * @param __code The code.
	 * @since 2020/06/26
	 */
	SimpleHTTPStatus(int __code)
	{
		this.code = __code;
	}
}
