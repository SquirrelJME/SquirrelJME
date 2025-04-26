// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.rms;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.rms.RecordStoreSession;
import java.util.Arrays;

/**
 * Manages enumeration over records.
 *
 * @since 2025/04/25
 */
final class __Enumerator__
	implements RecordEnumeration
{
	/** The record store used. */
	protected final RecordStore store;
	
	/** The filter over records. */
	protected final RecordFilter dataFilter;
	
	/** The comparator to use for records. */
	protected final RecordComparator comparator;
	
	/** The lock used for accessing the store. */
	protected final Object lock;
	
	/** The tags to filter with. */
	private final int[] _tagFilter;
	
	/** Should this be kept updated? */
	private volatile boolean _keepUpdated;
	
	/** The last known modified count. */
	private volatile long _modifiedAt =
		Integer.MIN_VALUE;
	
	/** Records which are currently enumerated. */
	private volatile int[] _records;
	
	/** The currently iterated index value. */
	private volatile int _iteratedAt =
		Integer.MIN_VALUE;
	
	/**
	 * Initializes the enumeration.
	 *
	 * @param __store The record store to use.
	 * @param __lock The lock used.
	 * @param __filter The optional filter.
	 * @param __comparator The comparator to use.
	 * @param __keepUpdated Should this be kept updated?
	 * @param __tags The tags to filter by.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/25
	 */
	__Enumerator__(RecordStore __store, Object __lock, RecordFilter __filter,
		RecordComparator __comparator, boolean __keepUpdated, int[] __tags)
		throws NullPointerException
	{
		if (__store == null || __lock == null)
			throw new NullPointerException("NARG");
		
		this.store = __store;
		this.lock = __lock;
		this.dataFilter = __filter;
		this.comparator = __comparator;
		this._keepUpdated = __keepUpdated;
		
		// Make sure all tags are sorted
		if (__tags == null)
			this._tagFilter = null;
		else
		{
			this._tagFilter = __tags.clone();
			Arrays.sort(this._tagFilter);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/25
	 */
	@Override
	public void destroy()
	{
		synchronized (this.lock)
		{
			// Same as reset internally
			this.reset();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/25
	 */
	@Override
	public int getRecordId(int __i)
		throws IllegalArgumentException
	{
		synchronized (this.lock)
		{
			// Potentially rebuild?
			int[] ids = this.__rebuild(false);
			
			// Not a valid index?
			if (__i < 0 || __i >= ids.length)
				throw new IllegalArgumentException("IOOB");
			
			return ids[__i];
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/25
	 */
	@Override
	public boolean hasNextElement()
	{
		synchronized (this.lock)
		{
			// At the very end?
			int iteratedAt = this._iteratedAt;
			if (iteratedAt == Integer.MAX_VALUE)
				return false;
			
			// Potentially rebuild?
			int[] ids = this.__rebuild(false);
			
			// Determine the position of where our iterator is at
			int index = Arrays.binarySearch(ids, iteratedAt);
			if (index < 0)
				index = -(index + 1);
			
			// Is this still within bounds?
			return (index + 1) < ids.length;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/25
	 */
	@Override
	public boolean hasPreviousElement()
	{
		synchronized (this.lock)
		{
			// At the very start?
			int iteratedAt = this._iteratedAt;
			if (iteratedAt == Integer.MIN_VALUE)
				return false;
			
			// Potentially rebuild?
			int[] ids = this.__rebuild(false);
			
			// Determine the position of where our iterator is at
			int index = Arrays.binarySearch(ids, iteratedAt);
			if (index < 0)
				index = -(index + 1);
			
			// Is this still within bounds?
			return (index - 1) >= 0;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/25
	 */
	@Override
	public boolean isKeptUpdated()
	{
		synchronized (this.lock)
		{
			return this._keepUpdated;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/25
	 */
	@Override
	public void keepUpdated(boolean __u)
	{
		synchronized (this.lock)
		{
			this._keepUpdated = __u;
			
			// Also preform a rebuild?
			if (__u)
				this.__rebuild(true);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/25
	 */
	@Override
	public byte[] nextRecord()
		throws InvalidRecordIDException, RecordStoreException,
		RecordStoreNotOpenException
	{
		synchronized (this.lock)
		{
			// We can bump this through the next record
			return this.store.getRecord(this.nextRecordId());
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/25
	 */
	@Override
	public int nextRecordId()
		throws InvalidRecordIDException
	{
		synchronized (this.lock)
		{
			// At the very end?
			int iteratedAt = this._iteratedAt;
			if (iteratedAt == Integer.MAX_VALUE)
				throw new InvalidRecordIDException("NSEE");
			
			// Potentially rebuild?
			int[] ids = this.__rebuild(false);
			
			// Determine the position of where our iterator is at
			int index = Arrays.binarySearch(ids, iteratedAt);
			if (index < 0)
				index = -(index + 1);
			
			// End reached?
			int check = index + 1;
			if (check >= ids.length)
			{
				this._iteratedAt = Integer.MAX_VALUE;
				throw new InvalidRecordIDException("NSEE");
			}
			
			// Set new position
			int result = ids[check];
			this._iteratedAt = result;
			return result;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/25
	 */
	@Override
	public int numRecords()
	{
		synchronized (this.lock)
		{
			// Potentially rebuild?
			int[] ids = this.__rebuild(false);
			
			// This is just the number of records we know of
			return ids.length;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/25
	 */
	@Override
	public byte[] previousRecord()
		throws InvalidRecordIDException, RecordStoreException,
		RecordStoreNotOpenException
	{
		synchronized (this.lock)
		{
			// We can bump this through the previous record
			return this.store.getRecord(this.previousRecordId());
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/25
	 */
	@Override
	public int previousRecordId()
		throws InvalidRecordIDException
	{
		synchronized (this.lock)
		{
			// At the very start?
			int iteratedAt = this._iteratedAt;
			if (iteratedAt == Integer.MIN_VALUE)
				throw new InvalidRecordIDException("NSEE");
			
			// Potentially rebuild?
			int[] ids = this.__rebuild(false);
			
			// Determine the position of where our iterator is at
			int index = Arrays.binarySearch(ids, iteratedAt);
			if (index < 0)
				index = -(index + 1);
			
			// End reached?
			int check = index - 1;
			if (check < 0)
			{
				this._iteratedAt = Integer.MIN_VALUE;
				throw new InvalidRecordIDException("NSEE");
			}
			
			// Set new position
			int result = ids[check];
			this._iteratedAt = result;
			return result;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/25
	 */
	@Override
	public void rebuild()
		throws IllegalStateException
	{
		synchronized (this.lock)
		{
			this.__rebuild(true);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/25
	 */
	@Override
	public void reset()
	{
		synchronized (this.lock)
		{
			// Clear all internal fields
			this._modifiedAt = Long.MIN_VALUE;
			this._records = null;
			this._iteratedAt = Integer.MIN_VALUE;
		}
	}
	
	/**
	 * Performs a rebuild.
	 *
	 * @param __force Force a rebuild to occur?
	 * @return The enumerated records.
	 * @since 2025/04/25
	 */
	private int[] __rebuild(boolean __force)
	{
		RecordStore store = this.store;
		try
		{
			synchronized (this.lock)
			{
				// No need to update?
				long wasModified = this._modifiedAt;
				long nowModified = store.getLastModified();
				if (!(__force || this._records == null ||
					(this._keepUpdated && wasModified != nowModified)))
					return this._records;
				
				// Set new modification marker
				this._modifiedAt = nowModified;
				
				// Read in all base IDs
				int[] baseIds;
				try (RecordStoreSession session = store.__info().__meta())
				{
					baseIds = session.ids();
				}
				
				// Filter each individual ID
				int[] tagFilter = this._tagFilter;
				RecordFilter dataFilter = this.dataFilter;
				for (int i = 0, n = baseIds.length; i < n; i++)
				{
					// The current ID being filtered, mark invalid to lose
					// candidacy
					int id = baseIds[i];
					baseIds[i] = -1;
					
					// Filter by tag?
					if (tagFilter != null)
					{
						int tag = store.getTag(id);
						if (Arrays.binarySearch(tagFilter, tag) < 0)
							continue;
					}
					
					// Filter by record data?
					if (dataFilter != null)
						if (!dataFilter.matches(store.getRecord(id)))
							continue;
					
					// Is valid
					baseIds[i] = id;
				}
				
				// Sort out any negatives
				Arrays.sort(baseIds);
				int newBase = 0;
				for (int n = baseIds.length; newBase < n; newBase++)
					if (baseIds[newBase] >= 0)
						break;
				
				// Replace the base ID set?
				if (newBase > 0)
				{
					int[] replaced = new int[baseIds.length - newBase];
					System.arraycopy(baseIds, newBase,
						replaced, 0, baseIds.length - newBase);
					baseIds = replaced;
				}
				
				// Changed sort order?
				RecordComparator comparator = this.comparator;
				if (comparator != null)
					throw Debugging.todo();
				
				// Use these records
				this._records = baseIds;
				return baseIds;
			}
		}
		
		// Failed?
		catch (RecordStoreException __e)
		{
			throw new IllegalStateException(__e.getMessage(), __e);
		}
	}
}
