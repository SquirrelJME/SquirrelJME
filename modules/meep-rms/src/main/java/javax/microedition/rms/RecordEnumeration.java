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
 * This is used to enumerate over a record store and may travel in either
 * direction.
 *
 * If the {@link RecordStore} has been closed then
 * {@link RecordStoreNotOpenException} must be thrown even if it was later
 * re-opened. When closed the previous and next methods will return
 * {@code null}.
 *
 * If a {@link RecordStore} is modified during enumeration then some records
 * may become invalid, for this a {@link RecordListener} may be used to be
 * notified when records are added or removed.
 *
 * {@link RecordComparator} may be used to modify the iteration order and the
 * {@link RecordFilter} may be used to remove undesired entries.
 *
 * @see RecordFilter
 * @see RecordComparator
 * @since 2017/02/26
 */
@Api
@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
public interface RecordEnumeration
{
	/**
	 * This should be called when the enumeration is no longer needed and as
	 * such it can free resources.
	 *
	 * @since 2017/02/26
	 */
	@Api
	void destroy();
	
	/**
	 * This is used to quickly obtain the record at the specified index within
	 * this enumeration. When using this behavior it is recommended to not
	 * keep the enumeraton up to date, otherwise it is implementation defined.
	 *
	 * @param __i The index to get.
	 * @return The record ID.
	 * @throws IllegalArgumentException If the index is negative or is at
	 * least {@link #numRecords()}.
	 * @since 2017/02/26
	 */
	@Api
	int getRecordId(int __i)
		throws IllegalArgumentException;
	
	/**
	 * Returns {@code true} if there is a next element.
	 *
	 * @return {@code true} if there is a next element.
	 * @since 2017/02/26
	 */
	@Api
	boolean hasNextElement();
	
	/**
	 * Returns {@code true} if there is a previous element.
	 *
	 * @return {@code true} if there is a previous element.
	 * @since 2017/02/26
	 */
	@Api
	boolean hasPreviousElement();
	
	/**
	 * Returns {@code true} if the enumeration is kept up to date with changes.
	 *
	 * @return {@code true} if the enumeration is kept up to date with changes.
	 * @since 2017/02/26
	 */
	@Api
	boolean isKeptUpdated();
	
	/**
	 * This is used by the enumeration to specify that it should be kept up
	 * to date with any record changes.
	 *
	 * If set to {@code true} this also performs the equivalent call to
	 * {@link #rebuild()}.
	 *
	 * Note that keeping enumerations up to date may cause performance issues.
	 *
	 * @param __u If {@code true} then the enumeration is kept updated.
	 * @since 2017/02/26
	 */
	@Api
	void keepUpdated(boolean __u);
	
	/**
	 * Returns a copy of the data contained in the next record. Changes to the
	 * returned array will not modify the data in the record.
	 *
	 * @return The data for the next record.
	 * @throws InvalidRecordIDException If the record is not valid.
	 * @throws RecordStoreException If another unspecified exception occurs.
	 * @throws RecordStoreNotOpenException This is called when the record store
	 * is no longer open.
	 * @since 2017/02/26
	 */
	@Api
	byte[] nextRecord()
		throws InvalidRecordIDException, RecordStoreException,
			RecordStoreNotOpenException;
	
	/**
	 * Returns the identifier of the next record.
	 *
	 * @return The next record.
	 * @throws InvalidRecordIDException If there is no next record.
	 * @since 2017/02/26
	 */
	@Api
	int nextRecordId()
		throws InvalidRecordIDException;
	
	/**
	 * Returns the number of records.
	 *
	 * @return The record count.
	 * @since 2020/02/16
	 */
	@Api
	int numRecords();
	
	/**
	 * Returns a copy of the data contained in the previous record. Changes to
	 * the returned array will not modify the data in the record.
	 *
	 * @return The data for the previous record.
	 * @throws InvalidRecordIDException If the record is not valid.
	 * @throws RecordStoreException If another unspecified exception occurs.
	 * @throws RecordStoreNotOpenException This is called when the record store
	 * is no longer open.
	 * @since 2017/02/26
	 */
	@Api
	byte[] previousRecord()
		throws InvalidRecordIDException, RecordStoreException,
			RecordStoreNotOpenException;
	
	/**
	 * Returns the identifier of the previous record.
	 *
	 * @return The previous record.
	 * @throws InvalidRecordIDException If there is no next record.
	 * @since 2017/02/26
	 */
	@Api
	int previousRecordId()
		throws InvalidRecordIDException;
	
	/**
	 * Rebuilds the enumeration to reflect the most up to date state.
	 *
	 * @throws IllegalStateException If this enumeration was destroyed.
	 * @see #isKeptUpdated()
	 * @see #keepUpdated(boolean)
	 * @since 2017/02/26
	 */
	@Api
	void rebuild()
		throws IllegalStateException;
	
	/**
	 * Repositions the enumeration so that the next element to be returned
	 * is the first element (and there is no previous element if that is
	 * called). The index will be reset back to zero.
	 *
	 * @since 2017/02/26
	 */
	@Api
	void reset();
}

