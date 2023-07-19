// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.rms;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the internal enumeration over records.
 *
 * @since 2019/05/13
 */
final class __VolumeEnumeration__
	implements RecordEnumeration
{
	/** The used record store. */
	protected final RecordStore store;
	
	/** The filter. */
	protected final RecordFilter filter;
	
	/** The comparator. */
	protected final RecordComparator comparator;
	
	/** Keep this updated? */
	private boolean _keepupdated;
	
	/** Tags to match. */
	private int[] _tags;
	
	/** Is this destroyed? */
	private boolean _destroyed;
	
	/** Last modification count. */
	private int _lastmod =
		-1;
	
	/** Built page list. */
	private List<__Page__> _pages;
	
	/**
	 * Initializes the record store enumeration.
	 *
	 * @param __store The store to use.
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
	 * @throws NullPointerException If no backing store was used.
	 * @since 2019/05/13
	 */
	__VolumeEnumeration__(RecordStore __store, RecordFilter __f,
		RecordComparator __c, boolean __ku, int[] __tags)
		throws NullPointerException
	{
		if (__store == null)
			throw new NullPointerException("NARG");
		
		this.store = __store;
		this.filter = __f;
		this.comparator = __c;
		this._keepupdated = __ku;
		this._tags = (__tags == null ? null : __tags.clone());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final void destroy()
	{
		if (this._destroyed)
			return;
		
		this._destroyed = true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final int getRecordId(int __i)
		throws IllegalArgumentException
	{
		// Check for destruction
		this.__checkDestroy();
		
		// Check for rebuild
		this.__checkRebuild();
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final boolean hasNextElement()
	{
		// Check for destruction
		this.__checkDestroy();
		
		// Check for rebuild
		this.__checkRebuild();
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final boolean hasPreviousElement()
	{
		// Check for destruction
		this.__checkDestroy();
		
		// Check for rebuild
		this.__checkRebuild();
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final boolean isKeptUpdated()
	{
		return this._keepupdated;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final void keepUpdated(boolean __u)
	{
		// Check for destruction
		this.__checkDestroy();
		
		this._keepupdated = __u;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final byte[] nextRecord()
		throws InvalidRecordIDException, RecordStoreException,
			RecordStoreNotOpenException
	{
		// Check for destruction
		this.__checkDestroy();
		
		// Check for rebuild
		this.__checkRebuild();
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final int nextRecordId()
		throws InvalidRecordIDException
	{
		// Check for destruction
		this.__checkDestroy();
		
		// Check for rebuild
		this.__checkRebuild();
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/16
	 */
	@Override
	public final int numRecords()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final byte[] previousRecord()
		throws InvalidRecordIDException, RecordStoreException,
			RecordStoreNotOpenException
	{
		// Check for destruction
		this.__checkDestroy();
		
		// Check for rebuild
		this.__checkRebuild();
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final int previousRecordId()
		throws InvalidRecordIDException
	{
		// Check for destruction
		this.__checkDestroy();
		
		// Check for rebuild
		this.__checkRebuild();
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final void rebuild()
		throws IllegalStateException
	{
		// Check for destruction
		this.__checkDestroy();
		
		// Used in the rebuild
		RecordStore store = this.store;
		RecordFilter filter = this.filter;
		RecordComparator comparator = this.comparator;
		int[] tags = this._tags;
		
		// Could fail
		try
		{
			// Record store has not been modified, ignore
			int nowmod = store.getVersion();
			if (this._pages != null && nowmod == this._lastmod)
				return;
			
			// Get all the page IDs
			int[] pageids = store.__listPages();
			
			// Build an initial page table
			List<__Page__> pages = new ArrayList<>(pageids.length);
			for (int pid : pageids)
			{
				// Quick filter by tag?
				if (tags != null)
				{
					// Get page tag
					int ptag = store.getTag(pid);
					
					// Only accept tags in this array
					boolean got = false;
					for (int mt : tags)
						if (ptag == mt)
						{
							got = true;
							break;
						}
					
					// Completely missed?
					if (!got)
						continue;
				}
				
				// Filter this?
				if (filter != null)
					throw Debugging.todo();
			}
			
			// Sort the input pages by their data?
			if (comparator != null)
				throw Debugging.todo();
			
			// Set new version
			this._pages = pages;
			this._lastmod = nowmod;
		}
		
		/* {@squirreljme.error DC0g Could not rebuild the enumeration.} */
		catch (RecordStoreException e)
		{
			throw new RuntimeException("DC0g", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final void reset()
	{
		// Check for destruction
		this.__checkDestroy();
		
		// Check for rebuild
		this.__checkRebuild();
		
		throw Debugging.todo();
	}
	
	/**
	 * Checks if this has been destroyed.
	 *
	 * @throws IllegalStateException If this was destroyed.
	 * @since 2019/05/13
	 */
	private void __checkDestroy()
		throws IllegalStateException
	{
		/* {@squirreljme.error DC0h Record store enumeration has been
		destoryed.} */
		if (this._destroyed)
			throw new IllegalStateException("DC0h");
	}
	
	/**
	 * Check if a rebuild needs to be done.
	 *
	 * @throws IllegalStateException If the update check could not be made.
	 * @since 2019/05/13
	 */
	private void __checkRebuild()
		throws IllegalStateException
	{
		// Could fail
		try
		{
			if (this._pages == null || (this._keepupdated &&
				this.store.getVersion() != this._lastmod))
				this.rebuild();
		}
		catch (RecordStoreException e)
		{
			/* {@squirreljme.error DC0i Could not check for updates.} */
			throw new IllegalStateException("DC0i", e);
		}
	}
	
	/**
	 * Represents a raw page ID.
	 *
	 * @since 2019/05/13
	 */
	private static final class __Page__
	{
		/** The ID. */
		public final int id;
		
		/**
		 * Initializes the page reference.
		 *
		 * @param __id The page ID.
		 * @since 2019/05/13
		 */
		public __Page__(int __id)
		{
			this.id = __id;
		}
	}
}

