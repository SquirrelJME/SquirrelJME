// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.rms;

/**
 * Indicates that comparison failed.
 *
 * @since 2025/06/24
 */
class __FailedCompare__
	extends RuntimeException
{
	/** The cause of the failure. */
	final RecordStoreException _cause;
	
	/**
	 * Initializes the exception.
	 *
	 * @param __e The cause.
	 * @since 2025/06/24
	 */
	__FailedCompare__(RecordStoreException __e)
	{
		this._cause = __e;
	}
}
