// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.rms;

/**
 * This stores information on a record store.
 *
 * @since 2017/02/26
 */
public final class RecordStoreInfo
{
	/**
	 * Used internally.
	 *
	 * @since 2017/02/26
	 */
	RecordStoreInfo()
	{
		throw new todo.TODO();
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
	public int getAuthMode()
		throws RecordStoreNotOpenException
	{
		throw new todo.TODO();
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
	public long getSize()
		throws RecordStoreNotOpenException
	{
		throw new todo.TODO();
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
	public long getSizeAvailable()
		throws RecordStoreNotOpenException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns {@code true} if the record store is encrypted.
	 *
	 * @return {@code true} if the record store is encrypted.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2017/02/26
	 */
	public boolean isEncrypted()
		throws RecordStoreNotOpenException
	{
		throw new todo.TODO();
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
	public boolean isWriteable()
		throws RecordStoreNotOpenException
	{
		throw new todo.TODO();
	}
}

