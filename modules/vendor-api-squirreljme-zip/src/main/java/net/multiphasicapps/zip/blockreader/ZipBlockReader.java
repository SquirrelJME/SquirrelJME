// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.blockreader;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.NoSuchElementException;
import net.multiphasicapps.zip.ZipException;

/**
 * This class is used to read ZIP files in a random access fashion.
 *
 * @since 2016/12/27
 */
public class ZipBlockReader
	implements Iterable<ZipBlockEntry>, Closeable
{
	/** The magic number for central directory items. */
	private static final int _CENTRAL_DIRECTORY_MAGIC_NUMBER =
		0x02014B50;
	
	/** The offset to the file name length. */
	private static final int _CENTRAL_DIRECTORY_NAME_LENGTH_OFFSET =
		28;
	
	/** The offset to the extra data length. */
	private static final int _CENTRAL_DIRECTORY_EXTRA_LENGTH_OFFSET =
		ZipBlockReader._CENTRAL_DIRECTORY_NAME_LENGTH_OFFSET + 2;
	
	/** The offset to the comment length. */
	private static final int _CENTRAL_DIRECTORY_COMMENT_LENGTH_OFFSET =
		ZipBlockReader._CENTRAL_DIRECTORY_EXTRA_LENGTH_OFFSET + 2;
	
	/** The minimum length of the central directory entry. */
	private static final int _CENTRAL_DIRECTORY_MIN_LENGTH =
		46;
	
	/** The magic number for the end directory. */
	private static final int _END_DIRECTORY_MAGIC_NUMBER =
		0x06054B50;
	
	/** The offset to the field for the number of entries in this disk. */
	private static final int _END_DIRECTORY_DISK_ENTRIES_OFFSET =
		8;
	
	/** The offset to the size of the central directory. */
	private static final int _END_DIRECTORY_CENTRAL_DIR_SIZE_OFFSET =
		ZipBlockReader._END_DIRECTORY_DISK_ENTRIES_OFFSET + 4;
	
	/** The offset to the offset of the central directory. */
	private static final int _END_DIRECTORY_CENTRAL_DIR_OFFSET_OFFSET =
		ZipBlockReader._END_DIRECTORY_CENTRAL_DIR_SIZE_OFFSET + 4;
	
	/** The minimum length of the end central directory record. */
	private static final int _END_DIRECTORY_MIN_LENGTH =
		22;
	
	/** The maximum length of the end central directory record. */
	private static final int _END_DIRECTORY_MAX_LENGTH =
		ZipBlockReader._END_DIRECTORY_MIN_LENGTH + 65535;
	
	/** The accessor to use for ZIP files. */
	protected final BlockAccessor accessor;
	
	/** The number of entries in this ZIP. */
	protected final int numentries;
	
	/** The base address for the central directory. */
	protected final long cdirbase;
	
	/** The actual start position for the ZIP file. */
	final long _zipbaseaddr;
	
	/** The accessor to use for ZIP files. */
	final BlockAccessor _accessor;
	
	/** Central directory entry offsets. */
	final long[] _offsets;
	
	/** Entries within this ZIP file. */
	private final Reference<ZipBlockEntry>[] _entries;
	
	/**
	 * Accesses the given array as a ZIP file.
	 *
	 * @param __b The array to wrap.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/27
	 */
	public ZipBlockReader(byte[] __b)
		throws IOException, NullPointerException
	{
		this(new ArrayBlockAccessor(__b));
	}
	
	/**
	 * Accesses the given range in the array as a ZIP file.
	 *
	 * @param __b The array to wrap.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to make available.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/27
	 */
	public ZipBlockReader(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IOException,
			NullPointerException
	{
		this(new ArrayBlockAccessor(__b, __o, __l));
	}
	
	/**
	 * Accesses the given ZIP file from the block accessor.
	 *
	 * @param __b The accessor to the ZIP data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @throws ZipException If the ZIP is malformed.
	 * @since 2016/12/27
	 */
	public ZipBlockReader(BlockAccessor __b)
		throws IOException, NullPointerException, ZipException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.accessor = __b;
		this._accessor = __b;
		
		// Locate the end of the central directory
		byte[] dirbytes = new byte[ZipBlockReader._END_DIRECTORY_MIN_LENGTH];
		long endat = ZipBlockReader.__locateCentralDirEnd(__b, dirbytes);
		
		// Get the number of entries which are in this disk and not in the
		// archive as a whole, since multi-archive ZIP files are not supported
		int numentries = __ArrayData__.readUnsignedShort(
			ZipBlockReader._END_DIRECTORY_DISK_ENTRIES_OFFSET, dirbytes);
		this.numentries = numentries;
		
		// Need the size of the central directory to determine where it
		// actually starts
		long csz = __b.size();
		long cdirsize = __ArrayData__.readUnsignedInt(
			ZipBlockReader._END_DIRECTORY_CENTRAL_DIR_SIZE_OFFSET, dirbytes);
		
		// This is the position of the start of the central directory
		long cdirbase = endat - cdirsize;
		this.cdirbase = cdirbase;
		
		/* {@squirreljme.error BF0j The central directory is larger than the
		ZIP file, the ZIP is truncated. (The central directory size; The
		size of the ZIP file)} */
		if (cdirsize > csz)
			throw new ZipException(String.format("BF0j %d %d", cdirsize, csz));
		
		// Determine the base address of the ZIP file since all entries
		// are relative from the start point
		long zipbaseaddr = csz - (__ArrayData__.readUnsignedInt(
			ZipBlockReader._END_DIRECTORY_CENTRAL_DIR_OFFSET_OFFSET, dirbytes) + cdirsize +
			(csz - endat));
		this._zipbaseaddr = zipbaseaddr;
		
		/* {@squirreljme.error BF0k The base address of the ZIP file exceeds
		the bound of the ZIP file. (The central directory size; The size of
		the ZIP file)} */
		if (zipbaseaddr < 0 || zipbaseaddr > csz)
			throw new ZipException(String.format("BF0k %d %d", zipbaseaddr,
				csz));
		
		// Setup entry list
		this._entries = ZipBlockReader.__newEntryReferenceList(numentries);
		
		// Initialize entry offsets
		this._offsets = this.__readOffsets();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/27
	 */
	@Override
	public void close()
		throws IOException
	{
		this.accessor.close();
	}
	
	/**
	 * Checks whether the ZIP file contains an entry with the given entry name.
	 *
	 * @param __s The name to check if it is contained within the ZIP.
	 * @return {@code true} If the ZIP contains an entry with this name.
	 * @throws IOException If the ZIP could not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/30
	 */
	public boolean contains(String __s)
		throws IOException, NullPointerException
	{
		try
		{
			return this.get(__s) != null;
		}
		
		// Does not exist
		catch (ZipEntryNotFoundException e)
		{
			return false;
		}
	}
	
	/**
	 * Returns the entry which is associated with the given name.
	 *
	 * @return The entry for the given name.
	 * @throws IOException If there was an error reading the ZIP.
	 * @throws NullPointerException On null arguments.
	 * @throws ZipEntryNotFoundException If the entry does not exist.
	 * @since 2016/12/30
	 */
	public ZipBlockEntry get(String __s)
		throws IOException, NullPointerException, ZipEntryNotFoundException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Use linear search since entries might be in any order
		for (ZipBlockEntry e : this)
			if (e.toString().equals(__s))
				return e;
		
		/* {@squirreljme.error BF0l Could not find the entry with the
		specified name. (The name of the entry)} */
		throw new ZipEntryNotFoundException(String.format("BF0l %s", __s));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/30
	 */
	@Override
	public Iterator<ZipBlockEntry> iterator()
	{
		return new __Iterator__();
	}
	
	/**
	 * Opens the file with the given name from this ZIP file and returns the
	 * input stream for its data.
	 *
	 * @param __s The file to open.
	 * @return The input stream to the file.
	 * @throws IOException If the file could not be opened due to either a
	 * damaged ZIP file, failed read, or if it does not exist.
	 * @throws NullPointerException On null arguments.
	 * @throws ZipEntryNotFoundException If the entry could not be found.
	 * @since 2016/12/30
	 */
	public InputStream open(String __s)
		throws IOException, NullPointerException, ZipEntryNotFoundException
	{
		/* {@squirreljme.error BF0m The specified entry does not exist
		within the ZIP file. (The entry name)} */
		ZipBlockEntry ent = this.get(__s);
		if (ent == null)
			throw new ZipEntryNotFoundException(String.format("BF0m %s", __s));
		
		// Open it
		return ent.open();
	}
	
	/**
	 * Returns the number of entries in this ZIP file.
	 *
	 * @return The number of entries in the ZIP.
	 * @since 2016/12/30
	 */
	public int size()
	{
		return this.numentries;
	}
	
	/**
	 * Reads the offsets within the ZIP file for central directory items.
	 *
	 * @return The array of offsets.
	 * @throws IOException On read errors or the ZIP is not valid.
	 * @since 2016/12/31
	 */
	private long[] __readOffsets()
		throws IOException
	{
		// Setup return value
		int numentries = this.numentries;
		long[] rv = new long[numentries];
		
		// Read in every entry within the ZIP
		BlockAccessor accessor = this.accessor;
		long at = this.cdirbase;
		byte[] cdirent = new byte[ZipBlockReader._CENTRAL_DIRECTORY_MIN_LENGTH];
		for (int i = 0; i < numentries; i++)
		{
			// Entry is placed at this position
			rv[i] = at;
			
			/* {@squirreljme.error BF0n Central directory extends past the end
			of the file. (The current entry; The current read position; The
			size of the file)} */
			if (accessor.read(at, cdirent, 0,
				ZipBlockReader._CENTRAL_DIRECTORY_MIN_LENGTH) != ZipBlockReader._CENTRAL_DIRECTORY_MIN_LENGTH)
				throw new ZipException(String.format("BF0n %d %d %d", i, at,
					accessor.size()));
			
			/* {@squirreljme.error BF0o The entry does not have a valid
			magic number. (The entry index)} */
			if (__ArrayData__.readSignedInt(0, cdirent) != ZipBlockReader._CENTRAL_DIRECTORY_MAGIC_NUMBER)
				throw new ZipException(String.format("BF0o %d", i));
			
			// Read lengths for file name, comment, and extra data
			int fnl = __ArrayData__.readUnsignedShort(
				ZipBlockReader._CENTRAL_DIRECTORY_NAME_LENGTH_OFFSET, cdirent),
				cml = __ArrayData__.readUnsignedShort(
					ZipBlockReader._CENTRAL_DIRECTORY_EXTRA_LENGTH_OFFSET, cdirent),
				edl = __ArrayData__.readUnsignedShort(
					ZipBlockReader._CENTRAL_DIRECTORY_COMMENT_LENGTH_OFFSET, cdirent);
			
			// Next entry is just after this point
			at += fnl + cml + edl + ZipBlockReader._CENTRAL_DIRECTORY_MIN_LENGTH;
		}
		
		// Done
		return rv;
	}
	
	/**
	 * Locates the end of the central directory.
	 *
	 * @param __b The block accessor to search.
	 * @param __db The bytes that make up the end of the central directory.
	 * @return The position of the central directory end.
	 * @throws IOException On read errors or if the central directory could
	 * not be found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/29
	 */
	private static long __locateCentralDirEnd(BlockAccessor __b,
		byte[] __db)
		throws IOException, NullPointerException
	{
		// Check
		if (__b == null || __db == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error BF0p The file is too small to be a ZIP file.
		(The size of file)} */
		long size = __b.size();
		if (size < ZipBlockReader._END_DIRECTORY_MIN_LENGTH)
			throw new ZipException(String.format("BF0p %d", size));
		
		// Constantly search for the end of the central directory
		for (long at = size - ZipBlockReader._END_DIRECTORY_MIN_LENGTH, end =
			 Math.max(0, size - ZipBlockReader._END_DIRECTORY_MAX_LENGTH); at >= end; at--)
		{
			// Read single byte to determine if it might start a header
			byte b = __b.read(at);
			if (b != 0x50)
				continue;
			
			// Read entire buffer (but not the comment in)
			__b.read(at, __db, 0, ZipBlockReader._END_DIRECTORY_MIN_LENGTH);
			
			// Need to check the magic number
			if (__ArrayData__.readSignedInt(0, __db) != ZipBlockReader._END_DIRECTORY_MAGIC_NUMBER)
				continue;
			
			// Length must match the end also
			if (__ArrayData__.readUnsignedShort(
				ZipBlockReader._END_DIRECTORY_MIN_LENGTH - 2,
				__db) != (size - (at + ZipBlockReader._END_DIRECTORY_MIN_LENGTH)))
				continue;
			
			// Central directory is here
			return at;
		}
		
		/* {@squirreljme.error BF0q Could not find the end of the central
		directory in the ZIP file.} */
		throw new ZipException("BF0q");
	}
	
	/**
	 * Creates a list of references.
	 *
	 * @param __n The number of elements in the list.
	 * @return The list.
	 * @since 2016/12/31
	 */
	@SuppressWarnings({"unchecked"})
	private static Reference<ZipBlockEntry>[] __newEntryReferenceList(
		int __n)
	{
		return (Reference<ZipBlockEntry>[])((Object)new Reference[__n]);
	}
	
	/**
	 * Iterates over entries within the ZIP.
	 *
	 * @since 2016/12/31
	 */
	private class __Iterator__
		implements Iterator<ZipBlockEntry>
	{
		/** Entry count. */
		protected final int numentries =
			ZipBlockReader.this.numentries;
		
		/** The next entry. */
		private volatile int _next =
			0;
		
		/**
		 * {@inheritDoc}
		 * @since 2016/12/31
		 */
		@Override
		public boolean hasNext()
		{
			return (this._next < this.numentries);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/12/31
		 */
		@Override
		public ZipBlockEntry next()
			throws NoSuchElementException
		{
			// No more entries?
			int next = this._next;
			if (next >= this.numentries)
				throw new NoSuchElementException("NSEE");
			
			// Next entry to read
			this._next = next + 1;
			
			// Parse and return entry
			Reference<ZipBlockEntry>[] entries = ZipBlockReader.this._entries;
			Reference<ZipBlockEntry> ref = entries[next];
			ZipBlockEntry rv;
			
			// Need to load the entry?
			if (ref == null || null == (rv = ref.get()))
				entries[next] = new WeakReference<>(
					(rv = new ZipBlockEntry(ZipBlockReader.this, next)));
			
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/12/31
		 */
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
}

