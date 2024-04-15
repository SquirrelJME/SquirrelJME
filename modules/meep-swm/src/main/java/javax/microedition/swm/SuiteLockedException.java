// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;

/**
 * This is thrown when a suite cannot be removed, either because it is not
 * permitted or it is currently in use.
 *
 * @since 2016/06/24
 */
@Api
public class SuiteLockedException
	extends IOException
{
	/**
	 * Initializes the exception with no message.
	 *
	 * @since 2016/06/24
	 */
	@Api
	public SuiteLockedException()
	{
	}
	
	/**
	 * Initializes the exception with the given message.
	 *
	 * @param __s The message to use.
	 * @since 2016/06/24
	 */
	@Api
	public SuiteLockedException(String __s)
	{
		super(__s);
	}
}

