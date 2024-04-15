// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.io;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;

/**
 * This is thrown when there is an error with a connection.
 *
 * @since 2021/11/30
 */
@Api
public class ConnectionException
	extends IOException
{
	@Api
	public static final int HTTP_ERROR = 10;
	
	@Api
	public static final int ILLEGAL_STATE = 1;
	
	@Api
	public static final int IMODE_LOCKED = 6;
	
	@Api
	public static final int NO_RESOURCE = 2;
	
	@Api
	public static final int NO_USE = 4;
	
	@Api
	public static final int OUT_OF_SERVICE = 5;
	
	@Api
	public static final int RESOURCE_BUSY = 3;
	
	@Api
	public static final int SCRATCHPAD_OVERSIZE = 11;
	
	@Api
	public static final int STATUS_FIRST = 0;
	
	@Api
	public static final int STATUS_LAST = 32;
	
	@Api
	public static final int SYSTEM_ABORT = 9;
	
	@Api
	public static final int TIMEOUT = 7;
	
	@Api
	public static final int UNDEFINED = 0;
	
	@Api
	public static final int USER_ABORT = 8;
	
	/** The error status. */
	private final int _status;
	
	@Api
	public ConnectionException(int __status)
	{
		this._status = __status;
	}
	
	@Api
	public int getStatus()
	{
		return this._status;
	}
}
