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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.rms.VinylLock;
import cc.squirreljme.runtime.rms.VinylRecord;

/**
 * This stores information on a record store.
 *
 * @since 2017/02/26
 */
@Api
public final class RecordStoreInfo
{
	/** The volume ID. */
	private final int _vid;
	
	/**
	 * Used internally.
	 *
	 * @param __vid The volume ID.
	 * @since 2017/02/26
	 */
	RecordStoreInfo(int __vid)
	{
		this._vid = __vid;
	}
	
	/**
	 * Gets the authorization mode of the associated {@link RecordStore}.
	 *
	 * @return The authorization mode, one of
	 * {@link RecordStore#AUTHMODE_PRIVATE} or
	 * {@link RecordStore#AUTHMODE_ANY}.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2017/02/26
	 */
	@Api
	public int getAuthMode()
		throws RecordStoreNotOpenException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the size of the record store including any overhead it may
	 * have.
	 *
	 * @return The number of bytes the record store consumes, not to exceed
	 * {@link Long#MAX_VALUE}.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2017/02/26
	 */
	@Api
	public long getSize()
		throws RecordStoreNotOpenException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the number of bytes which may be available for storage. This
	 * is only an estimate and the actual number may be higher or lower
	 * depending on overhead and storage requirements.
	 *
	 * @return The estimated number of bytes available for storage, not to
	 * exceed {@link Long#MAX_VALUE}.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2017/02/26
	 */
	@Api
	public long getSizeAvailable()
		throws RecordStoreNotOpenException
	{
		// Lock
		VinylRecord vinyl = RecordStore._VINYL;
		try (VinylLock lock = vinyl.lock())
		{
			return vinyl.vinylSizeAvailable();
		}
	}
	
	/**
	 * Returns {@code true} if the record store is encrypted.
	 *
	 * @return {@code true} if the record store is encrypted.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2017/02/26
	 */
	@Api
	public boolean isEncrypted()
		throws RecordStoreNotOpenException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns {@code true} if the record store can be written to by other
	 * suites. A value of {@code false} still specifies that the record may
	 * be written to by the current application.
	 *
	 * @return {@code true} if the record store can be written to by other
	 * suites.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2017/02/26
	 */
	@Api
	public boolean isWriteable()
		throws RecordStoreNotOpenException
	{
		throw Debugging.todo();
	}
}

