// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.io;

import java.io.IOException;

/**
 * This is thrown when there is an error with a connection.
 *
 * @since 2021/11/30
 */
public class ConnectionException
	extends IOException
{
	public static final int HTTP_ERROR =
		10;
		
	public static final int ILLEGAL_STATE =
		1;
		
	public static final int IMODE_LOCKED =
		6;
		
	public static final int NO_RESOURCE =
		2;
		
	public static final int NO_USE =
		4;
		
	public static final int OUT_OF_SERVICE =
		5;
		
	public static final int RESOURCE_BUSY =
		3;
		
	public static final int SCRATCHPAD_OVERSIZE =
		11;
		
	public static final int STATUS_FIRST =
		0;
		
	public static final int STATUS_LAST =
		32;
		
	public static final int SYSTEM_ABORT =
		9;
		
	public static final int TIMEOUT =
		7;
		
	public static final int UNDEFINED =
		0;
		
	public static final int USER_ABORT =
		8;
	
	/** The error status. */
	private final int _status;
	
	public ConnectionException(int __status)
	{
		this._status = __status;
	}
	
	public int getStatus()
	{
		return this._status;
	}
}
