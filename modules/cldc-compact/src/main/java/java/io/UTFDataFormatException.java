// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.io;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This is thrown when the format of a UTF string is not correct.
 *
 * @since 2018/12/03
 */
@Api
public class UTFDataFormatException
	extends IOException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2018/12/03
	 */
	@Api
	public UTFDataFormatException()
	{
	}
	
	/**
	 * Initializes the exception with the given cause.
	 *
	 * @param __m The message to use.
	 * @since 2018/12/03
	 */
	@Api
	public UTFDataFormatException(String __m)
	{
		super(__m);
	}
}

