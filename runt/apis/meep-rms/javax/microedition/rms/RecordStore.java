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

import cc.squirreljme.kernel.suiteinfo.SuiteName;
import cc.squirreljme.kernel.suiteinfo.SuiteVendor;
import cc.squirreljme.runtime.midlet.ActiveMidlet;
import cc.squirreljme.runtime.rms.RecordCluster;
import cc.squirreljme.runtime.rms.RecordClusterManager;
import cc.squirreljme.runtime.rms.RecordStoreOwner;
import javax.microedition.midlet.MIDlet;

/**
 * This is a record store which may be used by an application to store
 * information about it in an implementation defined manner.
 *
 * Opened record stores have an open count, as such for every open operation
 * there must be a close operation.
 *
 * Whenever the record store is modified, the version number will be
 * incremented.
 *
 * Record stores may optionally permit other suites to access and potentially
 * write their records, otherwise only the current suite may modify its own
 * records.
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
	
	/** The record store manager. */
	private static final RecordClusterManager _MANAGER;
	
	/** Lock on the cluster. */
	private static final Object _THIS_CLUSTER_LOCK =
		new Object();
	
	/** The cluster for this midlet suite. */
	private static volatile RecordCluster _THIS_CLUSTER;
	
	/**
	 * Initializes the record store manager.
	 *
	 * @since 2017/02/27
	 */
	static
	{
		if (true)
			throw new todo.TODO();
		/*
		// {@squirreljme.error DC04 No record store manager exists.}
		_MANAGER = SystemEnvironment.<RecordClusterManager>systemService(
			RecordClusterManager.class);
		if (_MANAGER == null)
			throw new RuntimeException("DC04");
		*/
	}
	
	/**
	 * Internally used.
	 *
	 * @since 2017/02/26
	 */
	private RecordStore()
	{
	}
	
	/**
	 * Adds the specified record to the record store and returns the record
	 * id for the newly added record.
	 *
	 * @param __b The data to store.
	 * @param __o The offset into the array.
	 * @param __l The length of the array.
	 * @param __tag The tag to identify the given record with.
	 * @return The record ID of the newly created record.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @throws RecordStoreException If some other error occurs.
	 * @throws RecordStoreFullException If there is not enough space to store
	 * the data.
	 * @since 2017/02/26
	 */
	public int addRecord(byte[] __b, int __o, int __l, int __tag)
		throws ArrayIndexOutOfBoundsException, NullPointerException,
			RecordStoreNotOpenException, RecordStoreException,
			RecordStoreFullException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Calls {@code addRecord(__b, __o, __l, 0)}.
	 *
	 * @param __b As forwarded.
	 * @param __o As forwarded.
	 * @param __l As forwarded.
	 * @return As forwarded.
	 * @throws ArrayIndexOutOfBoundsException As forwarded.
	 * @throws NullPointerException As forwarded.
	 * @throws RecordStoreNotOpenException As forwarded.
	 * @throws RecordStoreException As forwarded.
	 * @throws RecordStoreFullException As forwarded.
	 * @since 2017/02/26
	 */
	public int addRecord(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException,
			RecordStoreNotOpenException, RecordStoreException,
			RecordStoreFullException
	{
		return addRecord(__b, __o, __l, 0);
	}
	
	/**
	 * Adds a record listener to the given store to notify of when changes
	 * are made to records.
	 *
	 * If the record store is closed then this has no effect.
	 *
	 * @param __l The listener to call for changes, a listener which has
	 * already been added will not be added a second time.
	 * @since 2017/02/26
	 */
	public void addRecordListener(RecordListener __l)
	{
		throw new todo.TODO();
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
	 * When the store is fully closed all listeners will be removed.
	 *
	 * @throws RecordStoreNotOpenException If the record is not open.
	 * @throws RecordStoreException If there was an issue closing it.
	 * @since 2017/02/26
	 */
	public void closeRecordStore()
		throws RecordStoreNotOpenException, RecordStoreException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Deletes the specified record.
	 *
	 * @param __id The record to delete.
	 * @throws InvalidRecordIDException If the record ID is not valid.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @throws RecordStoreException If there was an issue deleting the record.
	 * @throws SecurityException If the record cannot be deleted.
	 * @since 2017/02/26
	 */
	public void deleteRecord(int __id)
		throws InvalidRecordIDException, RecordStoreNotOpenException,
			RecordStoreException, SecurityException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Enumerates through the records that exist within this store.
	 *
	 * If a comparator is not specified then the traversal order is not
	 * defined.
	 *
	 * @param __f An optional filter used to filter records, may be
	 * {@code null}.
	 * @param __c An optional comparator used to modify the sort order, may
	 * be {@code null}.
	 * @param __ku If {@code true} then the enumeration is kept up to date.
	 * @param __tags The tags to use for basic filtering, if this is empty then
	 * an empty enumeration will be returned, if this is {@code null} then all
	 * tags will be selected.
	 * @return The enumeration over the records.
	 * @throws RecordStoreNotOpenException If this record store is not open.
	 * @since 2017/02/26
	 */
	public RecordEnumeration enumerateRecords(RecordFilter __f,
		RecordComparator __c, boolean __ku, int[] __tags)
		throws RecordStoreNotOpenException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Calls {@code enumerateRecords(__f, __c, __ku, null)}.
	 *
	 * @param __f As forwarded.
	 * @param __c As forwarded.
	 * @param __ku As forwarded.
	 * @return As forwarded.
	 * @throws RecordStoreNotOpenException As forwarded.
	 * @since 2017/02/26
	 */
	public RecordEnumeration enumerateRecords(RecordFilter __f,
		RecordComparator __c, boolean __ku)
		throws RecordStoreNotOpenException
	{
		return enumerateRecords(__f, __c, __ku, null);
	}
	
	/**
	 * Returns the last modification date of the record store.
	 *
	 * @return The last modification date of the record store.
	 * @throws RecordStoreNotOpenException If this record store is not open.
	 * @since 2017/02/26
	 */
	public long getLastModified()
		throws RecordStoreNotOpenException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the name of the record store.
	 *
	 * @return The name of the record store.
	 * @throws RecordStoreNotOpenException If this record store is not open.
	 * @since 2017/02/26
	 */
	public String getName()
		throws RecordStoreNotOpenException
	{
		throw new todo.TODO();
	}
	
	/**
	 * This returns the next record ID which would be used if a new record
	 * were to be added to this record store.
	 *
	 * The returned ID is only valid while the store remains open and before
	 * {@code addRecord()} is called.
	 *
	 * @return The next record ID.
	 * @throws RecordStoreException If there was another issue with the
	 * record store.
	 * @throws RecordStoreNotOpenException If this record store is not open.
	 * @since 2017/02/26
	 */
	public int getNextRecordID()
		throws RecordStoreException, RecordStoreNotOpenException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns a copy of the data which is stored in the given record.
	 *
	 * @param __id The ID of the record to get.
	 * @return A copy of the data stored in this record, if there is no data
	 * then this will return {@code null}.
	 * @throws InvalidRecordIDException If the ID is not valid.
	 * @throws RecordStoreException If there is a problem with the record
	 * store.
	 * @throws RecordStoreNotOpenException If this record store is not open.
	 * @since 2017/02/26
	 */
	public byte[] getRecord(int __id)
		throws InvalidRecordIDException, RecordStoreException,
			RecordStoreNotOpenException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Fills the specified array with a copy of the data within the given
	 * record.
	 *
	 * @param __id The ID of the record to get.
	 * @param __b The array to write data to.
	 * @param __o The offset into the array.
	 * @return The number of bytes copied into the array.
	 * @throws ArrayIndexOutOfBoundsException If the offset is negative or
	 * the record data exceeds the size of the output array.
	 * @throws InvalidRecordIDException If the record ID is not valid.
	 * @throws NullPointerException On null arguments.
	 * @throws RecordStoreException If another problem occurs with the record
	 * store.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2017/02/26
	 */
	public int getRecord(int __id, byte[] __b, int __o)
		throws ArrayIndexOutOfBoundsException, InvalidRecordIDException,
			NullPointerException, RecordStoreException,
			RecordStoreNotOpenException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the size of the given record.
	 *
	 * @param __id The record ID to get the size for.
	 * @return The size of the given record.
	 * @throws InvalidRecordIDException If the record ID is not valid.
	 * @throws RecordStoreException If another problem occurs with the record
	 * store.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2016/02/26
	 */
	public int getRecordSize(int __id)
		throws InvalidRecordIDException, RecordStoreException,
			RecordStoreNotOpenException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the record store information.
	 *
	 * @return The record store information.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2016/02/26
	 */
	public RecordStoreInfo getRecordStoreInfo()
		throws RecordStoreNotOpenException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the size of the record store.
	 *
	 * @return The record store size, not to exceed {@link Integer#MAX_VALUE}.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2016/02/26
	 */
	@Deprecated
	public int getSize()
		throws RecordStoreNotOpenException
	{
		return (int)Math.min(Integer.MAX_VALUE,
			getRecordStoreInfo().getSize());
	}
	
	/**
	 * Returns the available size of the record store.
	 *
	 * @return The available record store size, not to exceed
	 * {@link Integer#MAX_VALUE}.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2016/02/26
	 */
	@Deprecated
	public int getSizeAvailable()
		throws RecordStoreNotOpenException
	{
		return (int)Math.min(Integer.MAX_VALUE,
			getRecordStoreInfo().getSizeAvailable());
	}
	
	/**
	 * Returns the tag of the given record.
	 *
	 * @param __id The record ID to get the tag for.
	 * @return The tag of the given record.
	 * @throws InvalidRecordIDException If the record ID is not valid.
	 * @throws RecordStoreException If another problem occurs with the record
	 * store.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2016/02/26
	 */
	public int getTag(int __id)
		throws InvalidRecordIDException, RecordStoreException,
			RecordStoreNotOpenException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the version of the record store, this may be used to quickly
	 * determine if a store has been modified.
	 *
	 * @return The version of this record store.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2016/02/26
	 */
	public int getVersion()
		throws RecordStoreNotOpenException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Removes the specified record listener, this has no effect if it has
	 * already been removed or was never added.
	 *
	 * @param __l The record listener to remove.
	 * @since 2017/02/26
	 */
	public void removeRecordListener(RecordListener __l)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the mode of the record store which permits or denies other suites
	 * access to this record store.
	 *
	 * This may only operate on fully closed record stores and no other
	 * suites must have this record store open when this is called.
	 *
	 * @param __auth The authorization mode to use.
	 * @param __write Whether writing should be permitted.
	 * @throws IllegalArgumentException If the authorization mode is not
	 * valid.
	 * @throws IllegalStateException If the record store is opened by any
	 * application.
	 * @throws RecordStoreException If some other problem occurs with the
	 * record store.
	 * @throws SecurityException If changing the mode is not permitted.
	 * @since 2017/02/26
	 */
	public void setMode(int __auth, boolean __write)
		throws IllegalArgumentException, IllegalStateException,
			RecordStoreException, SecurityException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the data for a record.
	 *
	 * @param __id The record ID to set.
	 * @param __b The input data.
	 * @param __o The offset into the array
	 * @param __l The number of bytes to write.
	 * @param __tag The new tag to set for the record, this replaces the
	 * old tag.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws InvalidRecordIDException If the record ID is not valid.
	 * @throws NullPointerException On null arguments.
	 * @throws RecordStoreException If another unspecified error happens.
	 * @throws RecordStoreFullException If there is not enough space to store
	 * the data.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2017/02/26
	 */
	public void setRecord(int __id, byte[] __b, int __o, int __l, int __tag)
		throws ArrayIndexOutOfBoundsException, InvalidRecordIDException,
			NullPointerException, RecordStoreException,
			RecordStoreFullException, RecordStoreNotOpenException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Calls {@code setRecord(__id, __b, __o, __l, getTag(__id))}.
	 *
	 * @param __id As forwarded.
	 * @param __b As forwarded.
	 * @param __o As forwarded.
	 * @param __l As forwarded.
	 * @throws ArrayIndexOutOfBoundsException As forwarded.
	 * @throws InvalidRecordIDException As forwarded.
	 * @throws NullPointerException As forwarded.
	 * @throws RecordStoreException As forwarded.
	 * @throws RecordStoreFullException As forwarded.
	 * @throws RecordStoreNotOpenException As forwarded.
	 * @since 2017/02/26
	 */
	public void setRecord(int __id, byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, InvalidRecordIDException,
			NullPointerException, RecordStoreException,
			RecordStoreFullException, RecordStoreNotOpenException
	{
		setRecord(__id, __b, __o, __l, getTag(__id));
	}
	
	/**
	 * Deletes the specified record store.
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
		throw new todo.TODO();
	}
	
	/**
	 * Returns the list of record stores that are available and owned by
	 * this suite.
	 *
	 * @return The list of available record stores, the order is unspecified
	 * and implementation dependent. If there are no records then {@code null}
	 * will be returned.
	 * @since 2017/02/26
	 */
	public static String[] listRecordStores()
	{
		// Internally all operations throw an exception
		try
		{
			return __thisCluster().listRecordStores();
		}
		
		// But if they fail then say there are no records
		catch (RecordStoreException e)
		{
			return null;
		}
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
		throw new todo.TODO();
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
		throw new todo.TODO();
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
	
	/**
	 * Returns the cluster for this MIDlet.
	 *
	 * @return The cluster for this MIDlet.
	 * @throws RecordStoreException If there was a problem opening this
	 * cluster.
	 * @since 2017/02/27
	 */
	private static RecordCluster __thisCluster()
		throws RecordStoreException
	{
		// Lock
		synchronized (_THIS_CLUSTER_LOCK)
		{
			// Open cluster connection?
			RecordCluster rv = _THIS_CLUSTER;
			if (rv == null)
				_THIS_CLUSTER = (rv =
					_MANAGER.open(RecordClusterManager.thisOwner()));
			
			return rv;
		}
	}
}

