// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.rms;

import cc.squirreljme.jvm.suite.SuiteIdentifier;
import cc.squirreljme.jvm.suite.SuiteName;
import cc.squirreljme.jvm.suite.SuiteVendor;
import cc.squirreljme.jvm.suite.SuiteVersion;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.midlet.ApplicationHandler;
import cc.squirreljme.runtime.rms.RecordSession;
import cc.squirreljme.runtime.rms.RecordStoreSession;
import cc.squirreljme.runtime.rms.RecordUtils;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.collections.IdentityLinkedHashSet;

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
@Api
@SuppressWarnings("DuplicateThrows")
public class RecordStore
	implements AutoCloseable
{
	/** Specifies that any suite may access the records. */
	@Api
	public static final int AUTHMODE_ANY =
		1;
	
	/** Specifies that only record store creator may access the records. */
	@Api
	public static final int AUTHMODE_PRIVATE =
		0;
	
	/** Record stores which have been opened. */
	private static final List<Reference<RecordStore>> _existing =
		new ArrayList<>();
	
	/** Identity map for listeners */
	private final Set<RecordListener> _listeners =
		new IdentityLinkedHashSet<>();
	
	/** Internal synchronization lock. */
	final Object _lock =
		new Object();
	
	/** The owner of this record. */
	private final SuiteIdentifier _owner;
	
	/** The name of this record. */
	private final String _name;
	
	/** Is this our own record? */
	private final boolean _isSelf;
	
	/** Cached meta information accessor. */
	private volatile RecordStoreInfo _metaRef;
	
	/** The number of times this has been opened. */
	private volatile int _openCount;
	
	/**
	 * Initializes the record store handler.
	 *
	 * @param __owner The owning suite name and vendor.
	 * @param __name The name of this record.
	 * @param __self Is this a record we own? 
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/15
	 */
	RecordStore(SuiteIdentifier __owner, String __name, boolean __self)
		throws NullPointerException
	{
		if (__owner == null || __name == null)
			throw new NullPointerException("NARG");
		
		this._owner = __owner;
		this._name = __name;
		this._isSelf = __self;
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
	@Api
	public int addRecord(byte[] __b, int __o, int __l, int __tag)
		throws ArrayIndexOutOfBoundsException, NullPointerException,
			RecordStoreNotOpenException, RecordStoreException,
			RecordStoreFullException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 ||
			(__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		int id;
		RecordListener[] listeners;
		
		synchronized (this._lock)
		{
			// Check open
			this.__checkOpen();
			
			/* {@squirreljme.error DC01 Cannot write record to read-only
			store.} */
			if (!this.__info().__isSelfWritable())
				throw new RecordStoreException("DC01");
			
			// Used for later broadcasting
			listeners = this.__listeners();
			
			// Open new session with the given tag
			try (RecordStoreSession session = this.__info().__meta())
			{
				// Allocate a new ID
				id = session.nextId(true);
				
				// Set tag for the ID
				session.setTag(id, __tag);
				
				// Open sub-session
				try (RecordSession sub = session.open(id))
				{
					// Write all bytes into it
					sub.writeAll(__b, __o, __l);
				}
			}
		}
		
		// Broadcast to listeners
		if (listeners != null)
			for (RecordListener listener : listeners)
				listener.recordAdded(this, id);
		
		// Return resultant ID
		return id;
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
	@Api
	public int addRecord(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException,
			RecordStoreNotOpenException, RecordStoreException,
			RecordStoreFullException
	{
		return this.addRecord(__b, __o, __l, 0);
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
	@Api
	public void addRecordListener(RecordListener __l)
	{
		// Ignore
		if (__l == null)
			return;
		
		// Add listener
		Set<RecordListener> listeners = this._listeners;
		synchronized (this._lock)
		{
			listeners.add(__l);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/26
	 */
	@Override
	public void close()
		throws RecordStoreNotOpenException, RecordStoreException
	{
		this.closeRecordStore();
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
	@Api
	public void closeRecordStore()
		throws RecordStoreNotOpenException, RecordStoreException
	{
		synchronized (this._lock)
		{
			// Fail if already closed
			if (this._openCount <= 0)
				throw new RecordStoreNotOpenException("CLSD");
			
			// Otherwise reduce
			this._openCount -= 1;
		}
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
	@Api
	public void deleteRecord(int __id)
		throws InvalidRecordIDException, RecordStoreNotOpenException,
			RecordStoreException, SecurityException
	{
		RecordListener[] listeners;
		synchronized (this)
		{
			// Check open
			this.__checkOpen();
			
			// Used for later broadcasting
			listeners = this.__listeners();
			
			// Delete the given record
			try (RecordStoreSession session = this.__info().__meta())
			{
				session.delete(__id);
			}
		}
		
		// Report to the listeners
		for (RecordListener l : listeners)
			l.recordDeleted(this, __id);
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
	@Api
	public RecordEnumeration enumerateRecords(RecordFilter __f,
		RecordComparator __c, boolean __ku, int[] __tags)
		throws RecordStoreNotOpenException
	{
		synchronized (this._lock)
		{
			// Check open
			this.__checkOpen();
			
			throw Debugging.todo();
		}
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
	@Api
	public RecordEnumeration enumerateRecords(RecordFilter __f,
		RecordComparator __c, boolean __ku)
		throws RecordStoreNotOpenException
	{
		return this.enumerateRecords(__f, __c, __ku, null);
	}
	
	/**
	 * Returns the last modification date of the record store.
	 *
	 * @return The last modification date of the record store.
	 * @throws RecordStoreNotOpenException If this record store is not open.
	 * @since 2017/02/26
	 */
	@Api
	public long getLastModified()
		throws RecordStoreNotOpenException
	{
		synchronized (this)
		{
			// Check open
			this.__checkOpen();
			
			try (RecordStoreSession session = this.__info().__meta())
			{
				return session.lastModified();
			}
			catch (RecordStoreException __e)
			{
				throw RecordUtils.wrap(
					new RecordStoreNotOpenException(__e.getMessage()), __e);
			}
		}
	}
	
	/**
	 * Returns the name of the record store.
	 *
	 * @return The name of the record store.
	 * @throws RecordStoreNotOpenException If this record store is not open.
	 * @since 2017/02/26
	 */
	@Api
	public String getName()
		throws RecordStoreNotOpenException
	{
		synchronized (this._lock)
		{
			// Check open
			this.__checkOpen();
			
			return this._name;
		}
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
	@Api
	public int getNextRecordID()
		throws RecordStoreException, RecordStoreNotOpenException
	{
		synchronized (this)
		{
			try (RecordStoreSession session = this.__info().__meta())
			{
				// Check open
				this.__checkOpen();
				
				// Get the next available ID without allocating
				return session.nextId(false);
			}
			catch (RecordStoreException __e)
			{
				throw RecordUtils.wrap(
					new RecordStoreNotOpenException(__e.getMessage()), __e);
			}
		}
	}
	
	/**
	 * Returns the number of records in this store.
	 *
	 * @return The number of records in this store.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2019/05/09
	 */
	@Api
	public int getNumRecords()
		throws RecordStoreNotOpenException
	{
		synchronized (this)
		{
			// The ID count is the record count
			try (RecordStoreSession session = this.__info().__meta())
			{
				// Check open
				this.__checkOpen();
				
				return session.ids().length;
			}
			catch (RecordStoreException __e)
			{
				throw RecordUtils.wrap(
					new RecordStoreNotOpenException(__e.getMessage()), __e);
			}
		}
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
	@Api
	public byte[] getRecord(int __id)
		throws InvalidRecordIDException, RecordStoreException,
			RecordStoreNotOpenException
	{
		synchronized (this)
		{
			// Check open
			this.__checkOpen();
			
			try (RecordStoreSession session = this.__info().__meta())
			{
				// Open existing session
				try (RecordSession sub = session.open(__id))
				{
					// No data?
					int length = sub.length();
					if (length <= 0)
						return null;
					
					// Read in data chunk
					byte[] result = new byte[length];
					sub.read(result, 0, length);
					return result;
				}
			}
		}
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
	@Api
	public int getRecord(int __id, byte[] __b, int __o)
		throws ArrayIndexOutOfBoundsException, InvalidRecordIDException,
			NullPointerException, RecordStoreException,
			RecordStoreNotOpenException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __o > __b.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		synchronized (this)
		{
			// Check open
			this.__checkOpen();
			
			try (RecordStoreSession session = this.__info().__meta())
			{
				// Open existing session
				try (RecordSession sub = session.open(__id))
				{
					// No data?
					int length = sub.length();
					if (length <= 0)
						return 0;
					
					// How much data can actually be read?
					int limit = Math.min(__b.length - __o, length);
					
					// Read in data chunk
					sub.read(__b, __o, limit);
					return limit;
				}
			}
		}
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
	@Api
	public int getRecordSize(int __id)
		throws InvalidRecordIDException, RecordStoreException,
			RecordStoreNotOpenException
	{
		synchronized (this._lock)
		{
			// Check open
			this.__checkOpen();
			
			try (RecordStoreSession session = this.__info().__meta())
			{
				try (RecordSession sub = session.open(__id))
				{
					return sub.length();
				}
			}
		}
	}
	
	/**
	 * Returns the record store information.
	 *
	 * @return The record store information.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2016/02/26
	 */
	@Api
	public RecordStoreInfo getRecordStoreInfo()
		throws RecordStoreNotOpenException
	{
		synchronized (this._lock)
		{
			// Check open
			this.__checkOpen();
			
			// Load info
			try
			{
				return this.__info();
			}
			catch (RecordStoreException __e)
			{
				throw RecordUtils.wrap(
					new RecordStoreNotOpenException(__e.getMessage()), __e);
			}
		}
	}
	
	/**
	 * Returns the size of the record store.
	 *
	 * @return The record store size, not to exceed {@link Integer#MAX_VALUE}.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2016/02/26
	 */
	@Api
	@ApiDefinedDeprecated
	public int getSize()
		throws RecordStoreNotOpenException
	{
		return (int)Math.min(Integer.MAX_VALUE,
			this.getRecordStoreInfo().getSize());
	}
	
	/**
	 * Returns the available size of the record store.
	 *
	 * @return The available record store size, not to exceed
	 * {@link Integer#MAX_VALUE}.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2016/02/26
	 */
	@Api
	@ApiDefinedDeprecated
	public int getSizeAvailable()
		throws RecordStoreNotOpenException
	{
		return (int)Math.min(Integer.MAX_VALUE,
			this.getRecordStoreInfo().getSizeAvailable());
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
	@Api
	public int getTag(int __id)
		throws InvalidRecordIDException, RecordStoreException,
			RecordStoreNotOpenException
	{
		synchronized (this._lock)
		{
			// Check open
			this.__checkOpen();
			
			// Get tag
			try (RecordStoreSession session = this.__info().__meta())
			{
				return session.getTag(__id);
			}
		}
	}
	
	/**
	 * Returns the version of the record store, this may be used to quickly
	 * determine if a store has been modified.
	 *
	 * @return The version of this record store.
	 * @throws RecordStoreNotOpenException If the record store is not open.
	 * @since 2016/02/26
	 */
	@Api
	public int getVersion()
		throws RecordStoreNotOpenException
	{
		synchronized (this._lock)
		{
			// Check open
			this.__checkOpen();
			
			// This would be in the meta-info
			try (RecordStoreSession session = this.__info().__meta())
			{
				return session.getInteger(
					RecordStoreSession.MODIFICATION_COUNT, 0);
			}
			catch (RecordStoreException __e)
			{
				throw RecordUtils.wrap(
					new RecordStoreNotOpenException(__e.getMessage()), __e);
			}
		}
	}
	
	/**
	 * Removes the specified record listener, this has no effect if it has
	 * already been removed or was never added.
	 *
	 * @param __l The record listener to remove.
	 * @since 2017/02/26
	 */
	@Api
	public void removeRecordListener(RecordListener __l)
	{
		// Ignore
		if (__l == null)
			return;
		
		// Remove listener
		Set<RecordListener> listeners = this._listeners;
		synchronized (this._lock)
		{
			listeners.remove(__l);
		}
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
	@Api
	public void setMode(int __auth, boolean __write)
		throws IllegalArgumentException, IllegalStateException,
			RecordStoreException, SecurityException
	{
		throw Debugging.todo();
		/*
		// Lock
		VinylRecord vinyl = RecordStore._VINYL;
		try (VinylLock lock = vinyl.lock())
		{
			throw Debugging.todo();
		}
		
		 */
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
	@Api
	public void setRecord(int __id, byte[] __b, int __o, int __l, int __tag)
		throws ArrayIndexOutOfBoundsException, InvalidRecordIDException,
			NullPointerException, RecordStoreException,
			RecordStoreFullException, RecordStoreNotOpenException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 ||
			(__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		RecordListener[] listeners;
		
		synchronized (this._lock)
		{
			// Check open
			this.__checkOpen();
			
			/* {@squirreljme.error DC01 Cannot write record to read-only
			store.} */
			if (!this.__info().__isSelfWritable())
				throw new RecordStoreException("DC01");
			
			// Used for later broadcasting
			listeners = this.__listeners();
			
			// Open existing session, set a new tag
			try (RecordStoreSession session = this.__info().__meta())
			{
				// Set tag for the ID
				session.setTag(__id, __tag);
				
				// Open sub-session
				try (RecordSession sub = session.open(__id))
				{
					// Write all bytes into it
					sub.writeAll(__b, __o, __l);
				}
			}
		}
		
		// Broadcast to listeners
		if (listeners != null)
			for (RecordListener listener : listeners)
				listener.recordChanged(this, __id);
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
	@Api
	public void setRecord(int __id, byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, InvalidRecordIDException,
			NullPointerException, RecordStoreException,
			RecordStoreFullException, RecordStoreNotOpenException
	{
		this.setRecord(__id, __b, __o, __l, this.getTag(__id));
	}
	
	/**
	 * Checks that this record store is open.
	 *
	 * @throws RecordStoreNotOpenException If it is not open.
	 * @since 2019/04/15
	 */
	private void __checkOpen()
		throws RecordStoreNotOpenException
	{
		synchronized (this._lock)
		{
			/* {@squirreljme.error DC07 This record store is not open.} */
			if (this._openCount <= 0)
				throw new RecordStoreNotOpenException("DC07");
		}
	}
	
	/**
	 * Returns all the listeners for this record store.
	 *
	 * @return The listeners, or {@code null if there are none}.
	 * @since 2019/04/15
	 */
	private RecordListener[] __listeners()
	{
		synchronized (this._lock)
		{
			Set<RecordListener> listeners = this._listeners;
			if (listeners.isEmpty())
				return null;
			return listeners.toArray(new RecordListener[listeners.size()]);
		}
	}
	
	/**
	 * Returns the meta information accessor for this record store.
	 *
	 * @return The meta info accessor.
	 * @since 2025/04/16
	 */
	final RecordStoreInfo __info()
		throws RecordStoreException
	{
		RecordStoreInfo result = this._metaRef;
		if (result == null)
		{
			result = new RecordStoreInfo(this._owner, this._name, this._isSelf,
				this._lock);
			this._metaRef = result;
		}
		
		return result;
	}
	
	/**
	 * Deletes the specified record store.
	 *
	 * Suites may only delete their own record store.
	 *
	 * This will not call
	 * {@link RecordListener#recordDeleted(RecordStore, int)}
	 * listeners associated with the given record store.
	 *
	 * @param __n The name of the record store to delete.
	 * @throws RecordStoreException If the record store cannot be deleted due
	 * to being owned by another suite or deletion is not possible.
	 * @throws RecordStoreNotFoundException If the given record store was not
	 * found.
	 * @since 2017/02/26
	 */
	@Api
	public static void deleteRecordStore(String __n)
		throws NullPointerException, RecordStoreException,
			RecordStoreNotFoundException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
		/*
		// Our suite identifier to find our own records
		long mysid = SuiteHash.currentIdentifier();
		
		// Lock
		VinylRecord vinyl = RecordStore._VINYL;
		try (VinylLock lock = vinyl.lock())
		{
			// Try to locate our record
			int got = -1;
			for (int rid : vinyl.volumeList())
			{
				// Another suite's volume
				if (mysid != vinyl.volumeSuiteIdentifier(rid))
					continue;
				
				// Found the record?
				if (__n.equals(vinyl.volumeName(rid)))
				{
					got = -1;
					break;
				}
			}
			
			/* {@squirreljme.error DC08 Cannot delete the specified record
			store because it does not exist. (The name of the store)} * /
			if (got == -1)
				throw new RecordStoreNotFoundException("DC08 " + __n);
			
			throw Debugging.todo();
		}
		
		 */
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
	@Api
	public static String[] listRecordStores()
	{
		throw Debugging.todo();
		/*
		// Our suite identifier to find our own records
		long mysid = SuiteHash.currentIdentifier();
		
		// Lock
		VinylRecord vinyl = RecordStore._VINYL;
		try (VinylLock lock = vinyl.lock())
		{
			List<String> rv = new ArrayList<>();
			
			// Go through all IDs and locate record store info
			for (int rid : vinyl.volumeList())
			{
				// Do not add records which belong to another suite
				if (mysid != vinyl.volumeSuiteIdentifier(rid))
					continue;
				
				rv.add(vinyl.volumeName(rid));
			}
			
			return rv.<String>toArray(new String[rv.size()]);
		}
		
		 */
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
	 * @param __pass The password.
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
	@Api
	public static RecordStore openRecordStore(String __n, boolean __create,
		int __auth, boolean __write, String __pass)
		throws IllegalArgumentException, RecordStoreException,
			RecordStoreFullException, RecordStoreNotFoundException,
			SecureRecordStoreException, SecurityException
	{
		return RecordStore.__openRecordStore(__n,
			ApplicationHandler.currentVendor(),
			ApplicationHandler.currentName(),
			__create, __auth, __write, __pass);
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
	@Api
	public static RecordStore openRecordStore(String __n, boolean __create,
		int __auth, boolean __write)
		throws IllegalArgumentException, RecordStoreException,
			RecordStoreFullException, RecordStoreNotFoundException,
			SecureRecordStoreException, SecurityException
	{
		return RecordStore.openRecordStore(__n, __create, __auth,
			__write, "");
	}
	
	/**
	 * Forwards to {@code openRecordStore(__n, __create, AUTHMODE_PRIVATE,
	 * true, "")}.
	 *
	 * @param __n As forwarded.
	 * @param __create As forwarded.
	 * @return As forwarded.
	 * @throws IllegalArgumentException As forwarded.
	 * @throws RecordStoreException As forwarded.
	 * @throws RecordStoreFullException As forwarded.
	 * @throws RecordStoreNotFoundException As forwarded.
	 * @throws SecureRecordStoreException As forwarded.
	 * @throws SecurityException As forwarded.
	 * @since 2017/02/26
	 */
	@Api
	public static RecordStore openRecordStore(String __n, boolean __create)
		throws IllegalArgumentException, RecordStoreException,
			RecordStoreFullException, RecordStoreNotFoundException,
			SecureRecordStoreException, SecurityException
	{
		return RecordStore.openRecordStore(__n, __create,
			RecordStore.AUTHMODE_PRIVATE,
			true, "");
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
	 * @param __suite The suite name.
	 * @param __pass The password to the record store.
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
	@Api
	public static RecordStore openRecordStore(String __n, String __vend,
		String __suite, String __pass)
		throws IllegalArgumentException, RecordStoreException,
			RecordStoreNotFoundException, SecureRecordStoreException,
			SecurityException
	{
		return RecordStore.__openRecordStore(__n, __vend, __suite,
			false, RecordStore.AUTHMODE_ANY, false, __pass);
	}
	
	/**
	 * Calls {@code openRecordStore(__n, __vend, __suite, "")}.
	 *
	 * @param __n As forwarded.
	 * @param __vend As forwarded.
	 * @param __suite As forwarded.
	 * @return As forwarded.
	 * @throws IllegalArgumentException As forwarded.
	 * @throws RecordStoreException As forwarded.
	 * @throws RecordStoreNotFoundException As forwarded.
	 * @throws SecureRecordStoreException As forwarded.
	 * @throws SecurityException As forwarded.
	 * @since 2017/02/26
	 */
	@Api
	public static RecordStore openRecordStore(String __n, String __vend,
		String __suite)
		throws IllegalArgumentException, RecordStoreException,
			RecordStoreNotFoundException, SecureRecordStoreException,
			SecurityException
	{
		return RecordStore.openRecordStore(__n, __vend, __suite, "");
	}
	
	/**
	 * Attempts to open and optionally create the record store for the
	 * specified MIDlet.
	 *
	 * If a password is specified then the record store will be encrypted to
	 * prevent tampering.
	 *
	 * If the record store has already been opened then it will return a
	 * previously opened record store.
	 *
	 * @param __name The name of the record store, must consist of 1 to 32
	 * Unicode characters.
	 * @param __vend The vendor of the other suite.
	 * @param __suite The suite name.
	 * @param __create If {@code true} then if the record store does not
	 * exist it will be created.
	 * @param __auth The authorization mode of the record which may permit
	 * other applications to access this record. If the record already exists
	 * then this argument will be ignored.
	 * @param __write If {@code true} then the record store may be written to
	 * by other suites. If the record already exists then this argument will be
	 * ignored.
	 * @param __pass The password to use for the record, this is optional.
	 * @return The newly opened or created record store, if the record store
	 * is already open then it will return the already open one.
	 * @throws IllegalArgumentException If the name is not valid or the
	 * authorization mode is not valid.
	 * @throws NullPointerException On null arguments.
	 * @throws RecordStoreException If it could not be opened for another
	 * reason.
	 * @throws RecordStoreFullException If there is no space remaining.
	 * @throws RecordStoreNotFoundException If the record store could not be
	 * located.
	 * @throws SecureRecordStoreException The secured record could not be
	 * initialized.
	 * @throws SecurityException If the encryption password does not
	 * match an existing password.
	 * @since 2018/12/15
	 */
	private static RecordStore __openRecordStore(String __name, String __vend,
		String __suite, boolean __create, int __auth, boolean __write,
		String __pass)
		throws IllegalArgumentException, NullPointerException,
			RecordStoreException, RecordStoreFullException,
			RecordStoreNotFoundException, SecureRecordStoreException,
			SecurityException
	{
		if (__name == null || __vend == null || __suite == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error DC0d The name is not valid.} */
		int namelen = __name.length();
		if (namelen < 1 || namelen > 32)
			throw new IllegalArgumentException("DC0d " + __name);
		
		// Determine the owner of the suite
		SuiteIdentifier owner = new SuiteIdentifier(new SuiteName(__suite),
			new SuiteVendor(__vend), SuiteVersion.MIN_VERSION);
		
		// Determine if this is our own suite's record store
		SuiteIdentifier self = ApplicationHandler.suiteIdentifier();
		boolean isSelf = owner.equals(self);
		
		// Check to see if this suite is already in memory
		RecordStore result = null;
		synchronized (RecordStore.class)
		{
			// Look through record stores we know about already
			List<Reference<RecordStore>> existing = RecordStore._existing;
			int freeSlot = -1;
			for (int i = 0, n = existing.size(); i < n; i++)
			{
				// Ignore blank slots
				Reference<RecordStore> ref = existing.get(i);
				if (ref == null)
				{
					freeSlot = i;
					continue;
				}
				
				// If this slot was GCed, clear it
				RecordStore check = ref.get();
				if (check == null)
				{
					existing.set(i, null);
					freeSlot = i;
					continue;
				}
				
				// Is this the record store we want?
				if (owner.equals(check._owner) && __name.equals(check._name))
				{
					result = check;
					break;
				}
			}
			
			// Need to set up a new store cache?
			if (result == null)
			{
				// Setup new accessor
				result = new RecordStore(owner, __name, isSelf);
				
				// Store cache at the free slot we found
				Reference<RecordStore> ref = new WeakReference<>(result);
				if (freeSlot < 0)
					existing.add(ref);
				else
					existing.set(freeSlot, ref);
			}
		}
		
		// We need to lock on the store's lock
		synchronized (result._lock)
		{
			try (RecordStoreSession session = result.__info().__meta())
			{
				// If this does not exist, we may need to initialize it
				if (!session.valid())
				{
					/* {@squirreljme.error DC0e Could not find the specified record
					store. (The name; The vendor; The suite)} */
					if (!__create || !isSelf)
						throw new RecordStoreNotFoundException(
							String.format("DC0e %s %s %s", __name, __vend,
								__suite));
					
					// Set the access mode
					session.setAccess(__auth, __write, __pass);
				}
				
				// Not isSelf and is not other writable?
				/* {@squirreljme.error DC0f Could not open record store of
				another suite as it is not marked as other writable.} */
				if (!isSelf && session.valid() &&
					!result.__info().isWriteable())
					throw new RecordStoreException(
						String.format("DC0f %s %s %s", __name, __vend,
							__suite));
				
				// Return the resultant store, after bumping the count
				result._openCount += 1;
				return result;
			}
		}
	}
}

