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
 * This is thrown when the record store is missing.
 *
 * @since 2017/02/26
 */
@Api
public class RecordStoreNotFoundException
	extends RecordStoreException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2017/02/26
	 */
	@Api
	public RecordStoreNotFoundException()
	{
	}
	
	/**
	 * Initializes the exception with a message except without a cause.
	 *
	 * @param __m The exception message.
	 * @since 2017/02/26
	 */
	@Api
	public RecordStoreNotFoundException(String __m)
	{
		super(__m);
	}
}

