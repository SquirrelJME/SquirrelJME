// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zips;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import net.multiphasicapps.squirreljme.cldc.IndexedComparator;
import net.multiphasicapps.squirreljme.cldc.IndexedSort;

/**
 * This provides a cached directory of the ZIP file contents.
 *
 * @since 2016/03/05
 */
public abstract class ZipDirectory
	implements Iterable<ZipEntry>
{
	/** The offsets of all the entry directories. */
	protected final long offsets[];
	
	/** The cache of entries. */
	private final Reference<ZipEntry> _entrycache[];
	
	/** The cache of strings. */
	private final Reference<String> _namecache[];
	
	/** Are entries sorted by name? */
	private volatile int[] _sorteddx;
	
	/**
	 * Initializes the directory.
	 *
	 * @param __ne The number of entries in the ZIP.
	 * @throws IOException On I/O errors.
	 * @since 2016/03/05
	 */
	protected ZipDirectory(int __ne)
		throws IOException
	{
		// {@squirreljme.error AM04 The ZIP directory has a negative
		// number of entries. (The negative count)}
		if (__ne < 0)
			throw new ZipFormatException(String.format("AM04 %d", __ne));
		
		// Initialize offset table
		offsets = new long[__ne];
		Arrays.fill(offsets, -1L);
		
		// Entry cache
		_entrycache = ZipDirectory.<ZipEntry>__makeRefArray(__ne);
		_namecache = ZipDirectory.<String>__makeRefArray(__ne);
	}
	
	/**
	 * Reads an entry in the directory.
	 *
	 * @param __dx The entry to read.
	 * @param __off The offset of the entry data in the central index.
	 * @return The read entry.
	 * @throws IOException On read errors.
	 * @since 2016/03/06
	 */
	protected abstract ZipEntry readEntry(int __dx, long __off)
		throws IOException;
	
	/**
	 * Reads the name of an entry.
	 *
	 * @param __dx The entry to read.
	 * @param __off The offset to the given entry.
	 * @return The read entry.
	 * @throws IOException On read errors.
	 * @since 2016/06/18
	 */
	protected abstract String readEntryName(int __dx, long __off)
		throws IOException;
	
	/**
	 * Obtains the entry with the given name.
	 *
	 * @param __n The entry to get which has this name.
	 * @return The entry with the given name or {@code null} if not found.
	 * @since 2016/03/05
	 */
	public final ZipEntry get(String __n)
		throws IOException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Use binary search if the names are sorted
		int n = size();
		int[] sorteddx = this._sorteddx;
		if (sorteddx != null)
		{
			throw new Error("TODO");
		}
		
		// Use linear search instead
		else
		{
			for (int i = 0; i < n; i++)
				if (getEntryName(i).equals(__n))
					return get(i);
		}
		
		// Not found
		return null;
	}
	
	/**
	 * Obtains the entry at this given position.
	 *
	 * @param __i The index to get the entry at.
	 * @return The entry at the given index.
	 * @throws IndexOutOfBoundsException if the index is not within the bounds
	 * of the entry table.
	 * @since 2016/03/05
	 */
	public final ZipEntry get(int __i)
		throws IndexOutOfBoundsException, IOException
	{
		// If out of bounds, always null
		long[] offsets = this.offsets;
		if (__i < 0 || __i >= offsets.length)
			return null;
		
		// Get the directory offset for this entry
		long off = offsets[__i];
		
		// {@squirreljme.error AM06 The file entry has a negative offset.
		// (The index of the entry)}
		if (off < 0L)
			throw new ZipFormatException(String.format("AM05 %d", __i));
		
		// Lock on the entry cache so it is a sort of volatile
		Reference<ZipEntry>[] entrycache = this._entrycache;
		synchronized (entrycache)
		{
			// Get reference here, which might not exist
			Reference<ZipEntry> ref = entrycache[__i];
			ZipEntry rv;
			
			// Create?
			if (ref == null || null == (rv = ref.get()))
				entrycache[__i] = new WeakReference<>(
					(rv = readEntry(__i, off)));
			
			// {@squirreljme.error AM0h Did not read an entry.}
			return Objects.<ZipEntry>requireNonNull(rv, "AM0h");
		}
	}
	
	/**
	 * This returns the name of the file that is associated with this entry.
	 *
	 * @param __i The index to get the file name of.
	 * @return The file name of the specified entry.
	 * @throws IndexOutOfBoundsException if the index is not within the bounds
	 * of the entry table.
	 * @throws IOException On read errors.
	 * @since 2016/06/18
	 */
	public final String getEntryName(int __i)
		throws IndexOutOfBoundsException, IOException
	{
		// Out of bounds?
		long[] offsets = this.offsets;
		if (__i < 0 || __i >= offsets.length)
			throw new IndexOutOfBoundsException(String.format("IOOB %d", __i));
		
		// Get the directory offset for this entry
		long off = offsets[__i];
		
		// {@squirreljme.error AM0g The file entry has a negative offset.
		// (The index of the entry)}
		if (off < 0L)
			throw new ZipFormatException(String.format("AM0g %d", __i));
		
		// Lock
		Reference<String>[] namecache = this._namecache;
		synchronized (namecache)
		{
			// Get reference here, which might not exist
			Reference<String> ref = namecache[__i];
			String rv;
			
			// Create?
			if (ref == null || null == (rv = ref.get()))
				namecache[__i] = new WeakReference<>(
					(rv = readEntryName(__i, off)));
			
			// {@squirreljme.error AM0j Did not read an entry name.}
			return Objects.<String>requireNonNull(rv, "AM0j");
		}
	}
	
	/**
	 * Sorts all entries by their name so that a name lookup which uses a
	 * binary search algorithm is used for entry lookup. The sorting cost at
	 * the start should offset the long term speed for ZIPs which have been
	 * opened for awhile.
	 *
	 * @throws IOException On read errors.
	 * @since 2016/06/18
	 */
	protected final void internalSortEntries()
		throws IOException
	{
		// ignore if already sorted
		if (this._sorteddx != null)
			return;
		
		// This could be running on low memory systems, so if a sort fails
		// due to being out of memory then just keep it linear.
		try
		{
			// Setup target sort index list
			int n = size();
			int[] sorted = new int[n];
			
			// Read all entry names
			String[] names = new String[n];
			for (int i = 0; i < n; i++)
				names[i] = readEntryName(i, offsets[i]);
			
			// Perform the sort
			/* IndexedSort IndexedComparator */
			if (true)
				throw new Error("TODO");
			
			// Set sorted details
			this._sorteddx = sorted;
		}
		
		// Ran out of memory
		catch (OutOfMemoryError e)
		{
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/05
	 */
	@Override
	public final Iterator<ZipEntry> iterator()
	{
		return new __Iterator__();
	}
	
	/**
	 * Returns the number of entries in the directory.
	 *
	 * @return The number of entries in the directory.
	 * @since 2016/03/05
	 */
	public final int size()
	{
		return offsets.length;
	}
	
	/**
	 * Makes the reference array.
	 *
	 * @param __ne Number of elements,
	 * @return The reference array
	 * @since 2016/03/06
	 */
	@SuppressWarnings({"unchecked"})
	private static final <W> Reference<W>[] __makeRefArray(int __ne)
	{
		return (Reference<W>[])new Reference[__ne];
	}
	
	/**
	 * This is the iterator over entries.
	 *
	 * @since 2016/03/05
	 */
	private final class __Iterator__
		implements Iterator<ZipEntry>
	{
		/** The current index. */
		private volatile int _dx =
			0;
		
		/**
		 * Initializes the iterator.
		 *
		 * @since 2016/03/05
		 */
		private __Iterator__()
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/05
		 */
		@Override
		public boolean hasNext()
		{
			return _dx < offsets.length;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/05
		 */
		@Override
		public ZipEntry next()
		{
			// Ran out?
			if (!hasNext())
				throw new NoSuchElementException("NSEE");
			
			// Might not be able to read it
			try
			{
				return get(_dx++);
			}
			
			// Failed to read
			catch (IOException ioe)
			{
				// {@squirreljme.error AM0d Failed to read the file entry.}
				throw new IllegalStateException("AM0d", ioe);
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/05
		 */
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
}

