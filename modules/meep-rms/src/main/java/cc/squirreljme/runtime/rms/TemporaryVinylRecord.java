// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.rms;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is a vinyl record which stores all of its data within in memory
 * buffers.
 *
 * @since 2018/12/13
 */
public final class TemporaryVinylRecord
	implements VinylRecord
{
	/** The lock for this record. */
	protected final BasicVinylLock lock =
		new BasicVinylLock();
	
	/** Tracks which are available. */
	private final Map<Integer, Volume> _volumes =
		new LinkedHashMap<>();
	
	/** Next ID for storage. */
	private volatile int _nextvid =
		1;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/14
	 */
	@Override
	public final VinylLock lock()
	{
		return this.lock.lock();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final int pageAdd(int __vid, byte[] __b, int __o, int __l,
		int __tag)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Locate the volume
		Volume vol = this._volumes.get(__vid);
		if (vol == null)
			return VinylRecord.ERROR_NO_VOLUME;
		
		// Create new page
		int pid = vol._nextpid++;
		Page page;
		vol._pages.put(pid, (page = new Page(pid)));
		
		// Volume modified
		vol._modcount++;
		vol._modtime = System.currentTimeMillis();
		
		// Store page data, will return the PID or error
		return page.setData(__b, __o, __l, __tag);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/09
	 */
	@Override
	public final int pageDelete(int __vid, int __pid)
	{
		// Locate the volume
		Volume vol = this._volumes.get(__vid);
		if (vol == null)
			return VinylRecord.ERROR_NO_VOLUME;
		
		// Locate the page
		Page page = vol._pages.get(__pid);
		if (page == null)
			return VinylRecord.ERROR_NO_PAGE;
		
		// Just remove it
		vol._pages.remove(__pid);
		
		// Return ID of the deleted page
		return __pid;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final int[] pageList(int __vid)
	{
		// Locate the volume
		Volume vol = this._volumes.get(__vid);
		if (vol == null)
			return new int[]{VinylRecord.ERROR_NO_VOLUME};
		
		// Get page IDs
		Set<Integer> keys = vol._pages.keySet();
		int n = keys.size(),
			i = 0;
		int[] rv = new int[n];
		for (Integer v : keys)
			rv[i++] = v;
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/09
	 */
	@Override
	public final int pageNextId(int __vid)
	{
		// Locate the volume
		Volume vol = this._volumes.get(__vid);
		if (vol == null)
			return VinylRecord.ERROR_NO_VOLUME;
		
		// Return guessed next ID
		return vol._nextpid;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final int pageRead(int __vid, int __pid, byte[] __b, int __o,
		int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Locate the volume
		Volume vol = this._volumes.get(__vid);
		if (vol == null)
			return VinylRecord.ERROR_NO_VOLUME;
		
		// Locate the page
		Page page = vol._pages.get(__pid);
		if (page == null)
			return VinylRecord.ERROR_NO_PAGE;
		
		// Determine read limit
		byte[] data = page._data;
		int pagelen = data.length;
		if (__l > pagelen)
			__l = pagelen;
		
		// Copy data
		for (int i = 0; i < __l; i++, __o++)
			__b[__o] = data[i];
		
		// All would have been read
		return __l;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/09
	 */
	@Override
	public final int pageSet(int __vid, int __pid, byte[] __b, int __o,
		int __l, int __tag)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Locate the volume
		Volume vol = this._volumes.get(__vid);
		if (vol == null)
			return VinylRecord.ERROR_NO_VOLUME;
		
		// Locate the page
		Page page = vol._pages.get(__pid);
		if (page == null)
			return VinylRecord.ERROR_NO_PAGE;
			
		// Volume modified
		vol._modcount++;
		vol._modtime = System.currentTimeMillis();
		
		// Store page data, will return the PID or error
		return page.setData(__b, __o, __l, __tag);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/01
	 */
	@Override
	public final int pageSize(int __vid, int __pid)
	{
		// Locate the volume
		Volume vol = this._volumes.get(__vid);
		if (vol == null)
			return VinylRecord.ERROR_NO_VOLUME;
		
		// Locate the page
		Page page = vol._pages.get(__pid);
		if (page == null)
			return VinylRecord.ERROR_NO_PAGE;
		
		// Return data length
		return page._data.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final int pageTag(int __vid, int __pid)
	{
		// Locate the volume
		Volume vol = this._volumes.get(__vid);
		if (vol == null)
			return VinylRecord.ERROR_NO_VOLUME;
		
		// Locate the page
		Page page = vol._pages.get(__pid);
		if (page == null)
			return VinylRecord.ERROR_NO_PAGE;
		
		// Return page tag
		return page._tag;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final int vinylSizeAvailable()
	{
		// This is technically limited by memory
		return (int)Math.min(Integer.MAX_VALUE,
			Runtime.getRuntime().freeMemory());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/14
	 */
	@Override
	public final int volumeCreate(long __sid, String __n, boolean __wo)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Claim next ID
		int rv = this._nextvid++;
		
		// Make the track and store it
		this._volumes.put(rv, new Volume(rv, __sid, __n, __wo));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/14
	 */
	@Override
	public final int[] volumeList()
	{
		Set<Integer> keys = this._volumes.keySet();
		
		// Setup basic integer array
		int n = keys.size();
		int[] rv = new int[n];
		
		// Fill in keys
		int at = 0;
		for (int v : keys)
			rv[at++] = v;
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final int volumeModCount(int __vid)
	{
		// Locate the volume
		Volume vol = this._volumes.get(__vid);
		if (vol == null)
			return VinylRecord.ERROR_NO_VOLUME;
		
		return vol._modcount;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/09
	 */
	@Override
	public final int volumeModTime(int __vid, long[] __time)
		throws NullPointerException
	{
		if (__time == null)
			throw new NullPointerException("NARG");
		
		// Locate the volume
		Volume vol = this._volumes.get(__vid);
		if (vol == null)
			return VinylRecord.ERROR_NO_VOLUME;
		
		if (__time.length > 0)
			__time[0] = vol._modtime;
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/14
	 */
	@Override
	public final String volumeName(int __vid)
	{
		Volume vol = this._volumes.get(__vid);
		if (vol == null)
			return null;
		return vol.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final boolean volumeOtherWritable(int __vid)
	{
		Volume vol = this._volumes.get(__vid);
		if (vol == null)
			return false;
		return vol.writeother;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/14
	 */
	@Override
	public final long volumeSuiteIdentifier(int __vid)
	{
		Volume vol = this._volumes.get(__vid);
		if (vol == null)
			return 0L;
		return vol.sid;
	}
	
	/**
	 * Represents a single page.
	 *
	 * @since 2019/04/15
	 */
	public static final class Page
	{
		/** The page ID. */
		protected final int pid;
		
		/** The page data. */
		volatile byte[] _data;
		
		/** The tag. */
		volatile int _tag;
		
		/**
		 * Initializes the page.
		 *
		 * @param __pid The page ID.
		 * @since 2019/04/15
		 */
		public Page(int __pid)
		{
			// Setup PID
			this.pid = __pid;
		}
		
		/**
		 * Sets the page data.
		 *
		 * @param __b The byte data.
		 * @param __o Offset into data.
		 * @param __l The length of data.
		 * @param __tag The tag for the page.
		 * @return The page ID or a negative value if an error.
		 * @throws IndexOutOfBoundsException If the offset and/or length
		 * exceed the array bounds.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/04/15
		 */
		public final int setData(byte[] __b, int __o, int __l, int __tag)
			throws IndexOutOfBoundsException, NullPointerException
		{
			if (__b == null)
				throw new NullPointerException("NARG");
			if (__o < 0 || __l < 0 || (__o + __l) < 0 ||
				(__o + __l) > __b.length)
				throw new IndexOutOfBoundsException("IOOB");
			
			// Create copy of the data
			try
			{
				// Allocate and copy data
				byte[] place = new byte[__l];
				for (int i = __o, o = 0; o < __l; i++, o++)
					place[o] = __b[i];
				
				// Store this data
				this._data = place;
			}
			
			// No memory to store this data?
			catch (OutOfMemoryError e)
			{
				return VinylRecord.ERROR_NO_MEMORY;
			}
			
			// Set tag
			this._tag = __tag;
			
			// Return PID
			return this.pid;
		}
	}
	
	/**
	 * Represents a single volume.
	 *
	 * @since 2019/04/14
	 */
	public static final class Volume
	{
		/** The volume ID. */
		protected final int vid;
		
		/** The suite identifier. */
		protected final long sid;
		
		/** The suite name. */
		protected final String name;
		
		/** Allow write by others? */
		protected final boolean writeother;
		
		/** Pages in this volume. */
		final Map<Integer, Page> _pages = 
			new LinkedHashMap<>();
		
		/** The next page ID. */
		volatile int _nextpid =
			1;
		
		/** Modification count. */
		volatile int _modcount =
			0;
		
		/** Modification time. */
		volatile long _modtime =
			System.currentTimeMillis();
		
		/**
		 * Initializes the volume.
		 *
		 * @param __vid The volume ID.
		 * @param __sid The suite identifier.
		 * @param __name The name of the record.
		 * @param __wo Allow write by others?
		 * @throws NullPointerException On null arguments.
		 * @since 2019/04/14
		 */
		public Volume(int __vid, long __sid, String __name, boolean __wo)
			throws NullPointerException
		{
			if (__name == null)
				throw new NullPointerException("NARG");
			
			this.vid = __vid;
			this.sid = __sid;
			this.name = __name;
			this.writeother = __wo;
		}
	}
}

