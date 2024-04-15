// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;

/**
 * This is thrown when the connection was not available.
 *
 * @since 2019/05/06
 */
@Api
public class ConnectionNotFoundException
	extends IOException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2019/05/06
	 */
	@Api
	public ConnectionNotFoundException()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2019/05/06
	 */
	@Api
	public ConnectionNotFoundException(String __m)
	{
		super(__m);
	}
}


