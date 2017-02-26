// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.rms;

/**
 * This is thrown when the record store is missing.
 *
 * @since 2017/02/26
 */
public class RecordStoreNotFoundException
	extends RecordStoreException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2017/02/26
	 */
	public RecordStoreNotFoundException()
	{
	}
	
	/**
	 * Initializes the exception with a message except without a cause.
	 *
	 * @param __m The exception message.
	 * @since 2017/02/26
	 */
	public RecordStoreNotFoundException(String __m)
	{
		super(__m);
	}
}

