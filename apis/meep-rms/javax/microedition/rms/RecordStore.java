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
 * This is a record store which may be used by an application to store
 * information about it in an implementation defined manner.
 *
 * Opened record stores have an open count, as such for every open operation
 * there must be a close operation.
 *
 * @since 2017/02/26
 */
public class RecordStore
	implements AutoCloseable
{
	/** Specifies that any suite may access the records. */
	public static final int AUTHMODE_ANY =
		1;
	
	/** Specifies that only record store creator may access the records. */
	public static final int AUTHMODE_PRIVATE =
		0;
	
	/**
	 * Internally used.
	 *
	 * @since 2017/02/26
	 */
	private RecordStore()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/26
	 */
	@Override
	public void close()
		throws RecordStoreNotOpenException, RecordStoreException
	{
		closeRecordStore();
	}
	
	/**
	 * Closes the record store.
	 *
	 * Note that due to the ability to have record stores opened multiple times
	 * the open count must reach zero before it is actually closed.
	 *
	 * @throws RecordStoreNotOpenException If the record is not open.
	 * @throws RecordStoreException If there was an issue closing it.
	 * @since 2017/02/26
	 */
	public void closeRecordStore()
		throws RecordStoreNotOpenException, RecordStoreException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Delets the specified record store.
	 *
	 * Suites may only delete their own record store.
	 *
	 * This will not call {@link RecordListener#recordDelete(RecordStore, int)}
	 * listeners associated with the given record store.
	 *
	 * @param __n The name of the record store to delete.
	 * @throws RecordStoreException If the record store cannot be deleted due
	 * to being owned by another suite or deletion is not possible.
	 * @throws RecordStoreNotFoundException If the given record store was not
	 * found.
	 * @since 2017/02/26
	 */
	public static void deleteRecordStore(String __n)
		throws RecordStoreException, RecordStoreNotFoundException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the list of record stores that are available.
	 *
	 * @return The list of available record stores, the order is unspecified
	 * and implementation dependent.
	 * @since 2017/02/26
	 */
	public static String[] listRecordStores()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Attempts to open and optionally create the record store for this midlet
	 * with the specified name.
	 *
	 * If a password is specified then the record store will be encrypted to
	 * prevent tampering.
	 *
	 * If the record store has already been opened then it will return a
	 * previously opened record store.
	 *
	 * @param __n The name of the record store, must consist of 1 to 32
	 * Unicode characters.
	 * @param __create If {@code true} then if the record store does not
	 * exist it will be created.
	 * @param __auth The authorization mode of the record which may permit
	 * other applications to access this record. If the record already exists
	 * then this argument will be ignored.
	 * @param __write If {@code true} then the record store may be written to
	 * by other suites. If the record already exists then this argument will be
	 * ignored.
	 * @return The newly opened or created record store, if the record store
	 * is already open then it will return the already open one.
	 * @throws IllegalArgumentException If the name is not valid or the
	 * authorization mode is not valid.
	 * @throws RecordStoreException If it could not be opened for another
	 * reason.
	 * @throws RecordStoreFullException If there is no space remaining.
	 * @throws RecordStoreNotFoundException If the record store could not be
	 * located.
	 * @throws SecureRecordStoreException The secured record could not be
	 * initialized.
	 * @throws SecurityException If the encryption password does not
	 * match an existing password.
	 * @since 2017/02/26
	 */
	public static RecordStore openRecordStore(String __n, boolean __create,
		int __auth, boolean __write, String __pass)
		throws IllegalArgumentException, RecordStoreException,
			RecordStoreFullException, RecordStoreNotFoundException,
			SecureRecordStoreException, SecurityException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Forwards to {@code openRecordStore(__n, __create, __auth, __write, "")}.
	 *
	 * @param __n As forwarded.
	 * @param __create As forwarded.
	 * @param __auth As forwarded.
	 * @param __write As forwarded.
	 * @return As forwarded.
	 * @throws IllegalArgumentException As forwarded.
	 * @throws RecordStoreException As forwarded.
	 * @throws RecordStoreFullException As forwarded.
	 * @throws RecordStoreNotFoundException As forwarded.
	 * @throws SecureRecordStoreException As forwarded.
	 * @throws SecurityException As forwarded.
	 * @since 2017/02/26
	 */
	public static RecordStore openRecordStore(String __n, boolean __create,
		int __auth, boolean __write)
		throws IllegalArgumentException, RecordStoreException,
			RecordStoreFullException, RecordStoreNotFoundException,
			SecureRecordStoreException, SecurityException
	{
		return openRecordStore(__n, __create, __auth, __write, "");
	}
	
	/**
	 * Forwards to {@code openRecordStore(__n, __create, AUTHMODE_PRIVATE,
	 * true, "")}.
	 *
	 * @param __n As forwarded.
	 * @param __create As forwarded.
	 * @param __auth As forwarded.
	 * @param __write As forwarded.
	 * @return As forwarded.
	 * @throws IllegalArgumentException As forwarded.
	 * @throws RecordStoreException As forwarded.
	 * @throws RecordStoreFullException As forwarded.
	 * @throws RecordStoreNotFoundException As forwarded.
	 * @throws SecureRecordStoreException As forwarded.
	 * @throws SecurityException As forwarded.
	 * @since 2017/02/26
	 */
	public static RecordStore openRecordStore(String __n, boolean __create)
		throws IllegalArgumentException, RecordStoreException,
			RecordStoreFullException, RecordStoreNotFoundException,
			SecureRecordStoreException, SecurityException
	{
		return openRecordStore(__n, __create, AUTHMODE_PRIVATE, true, "");
	}
	
	/**
	 * Attempts to open the record store created by another application.
	 *
	 * The record store must have been created with the {@link #AUTHMODE_ANY}
	 * authorization. If it is encrypted then the same password must be
	 * specified.
	 *
	 * If the vendor and suite is our own then this will be the same as
	 * calling: {@code openRecordStore(__n, false, AUTHMODE_PRIVATE, true,
	 * __pass)}.
	 *
	 * @param __n The name of the record store, must consist of 1 to 32
	 * Unicode characters.
	 * @param __vend The vendor of the other suite.
	 * @param __name The name of the other suite.
	 * @return The opened record store.
	 * @throws IllegalArgumentException If the name, vendor, or suite names
	 * are not valid.
	 * @throws RecordStoreException If it could not be opened for another
	 * reason.
	 * @throws RecordStoreNotFoundException If the record store could not be
	 * located.
	 * @throws SecureRecordStoreException The secured record could not be
	 * initialized.
	 * @throws SecurityException If the encryption password does not
	 * match an existing password.
	 * @since 2017/02/26
	 */
	public static RecordStore openRecordStore(String __n, String __vend,
		String __suite, String __pass)
		throws IllegalArgumentException, RecordStoreException,
			RecordStoreNotFoundException, SecureRecordStoreException,
			SecurityException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Calls {@code openRecordStore(__n, __vend, __suite, "")}.
	 *
	 * @param __n As forwarded.
	 * @param __vend As forwarded.
	 * @param __name As forwarded.
	 * @return As forwarded.
	 * @throws IllegalArgumentException As forwarded.
	 * @throws RecordStoreException As forwarded.
	 * @throws RecordStoreNotFoundException As forwarded.
	 * @throws SecureRecordStoreException As forwarded.
	 * @throws SecurityException As forwarded.
	 * @since 2017/02/26
	 */
	public static RecordStore openRecordStore(String __n, String __vend,
		String __suite)
		throws IllegalArgumentException, RecordStoreException,
			RecordStoreNotFoundException, SecureRecordStoreException,
			SecurityException
	{
		return openRecordStore(__n, __vend, __suite, "");
	}
}

