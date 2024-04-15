// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.rms;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * General exception for record stores.
 *
 * @since 2017/02/26
 */
@Api
public class RecordStoreException
	extends Exception
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2017/02/26
	 */
	@Api
	public RecordStoreException()
	{
	}
	
	/**
	 * Initializes the exception with a message except without a cause.
	 *
	 * @param __m The exception message.
	 * @since 2017/02/26
	 */
	@Api
	public RecordStoreException(String __m)
	{
		super(__m);
	}
}

