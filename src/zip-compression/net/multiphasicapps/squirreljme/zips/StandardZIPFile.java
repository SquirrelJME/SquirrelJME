// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.zips;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 * This provides abstract access to a ZIP file.
 *
 * If the used {@link SeekableByteChannel} is to be used by other threads which
 * are not treating it as a ZIP file then locking must be performed on it,
 * otherwise the position may get invalidated and undefined behavior shall
 * occur.
 *
 * @since 2016/02/26
 */
public abstract class StandardZIPFile
	implements Iterable<StandardZIPFile.FileEntry>
{
	/** Internal lock. */
	protected final Object lock =
		new Object();
	
	/** The base channel to read from. */
	protected final SeekableByteChannel channel;
	
	/** File channel if this is one. */
	protected final FileChannel filechannel;
	
	/** Read buffer to prevent a thousand allocations at the cost of speed. */
	private final ByteBuffer _readbuffer =
		ByteBuffer.allocateDirect(8);
	
	/** The directory cache. */
	private volatile Reference<Directory> _directory;
	
	/**
	 * Initializes the zip file using the given byte channel which contains
	 * the ZIP file data.
	 *
	 * @param __sbc The source channel to read from.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @throws ZIPFormatException If this is not a valid ZIP file.
	 * @since 2016/02/26
	 */
	public StandardZIPFile(SeekableByteChannel __sbc)
		throws IOException, NullPointerException, ZIPFormatException
	{
		// Check
		if (__sbc == null)
			throw new NullPointerException();
		
		// Set
		channel = __sbc;
		
		// If a file channel, some speed could be gained by not requiring a
		// channel lock
		if (channel instanceof FileChannel)
			filechannel = (FileChannel)channel;
		else
			filechannel = null;
	}
	
	/**
	 * Reads the directory of the ZIP file.
	 *
	 * @return The ZIP directory.
	 * @throws IOException On read errors.
	 * @since 2016/03/05
	 */
	protected abstract Directory readDirectory()
		throws IOException;
	
	/**
	 * Obtains an entry by its name.
	 *
	 * @param __n The name of the entry to get.
	 * @return The entry by the given name or {@code null} if it does not
	 * exist in the ZIP.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/05
	 */
	public final FileEntry get(String __n)
		throws IOException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException();
		
		// Use the directory
		return getDirectory().get(__n);
	}
	
	/**
	 * Obtains the entry at the given index.
	 *
	 * @param __dx The entry to get the index at.
	 * @return The index at this entry or {@code null} if it is not a valid
	 * index.
	 * @throws IOException On read errors.
	 * @since 2016/03/05
	 */
	public final FileEntry get(int __dx)
		throws IOException
	{
		// Negative is always null
		if (__dx < 0)
			return null;
		
		// Use the directory
		return getDirectory().get(__dx);
	}
	
	/**
	 * Obtains the directory which is potentially cached.
	 *
	 * @throws IllegalStateException If the directory could not be read.
	 * @since 2016/03/05
	 */
	public final Directory getDirectory()
		throws IllegalStateException
	{
		// Lock so that if multiple threads are accessing the ZIP file they do
		// not have to both create a directory when only one is needed at a
		// time.
		synchronized (lock)
		{
			// Check cache
			Reference<Directory> ref = _directory;
			Directory rv = null;
		
			// In the reference?
			if (ref != null)
				rv = ref.get();
			
			// Cache it
			if (rv == null)
				try
				{
					_directory = new WeakReference<>(
						Objects.<Directory>requireNonNull((
						rv = readDirectory())));
				}
				
				// Could not read the directory
				catch (IOException ioe)
				{
					throw new IllegalStateException(ioe);
				}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @throws IllegalStateException If the directory could not be read.
	 * @since 2016/03/05
	 */
	@Override
	public final Iterator<FileEntry> iterator()
		throws IllegalStateException
	{
		return getDirectory().iterator();
	}
	
	/**
	 * Reads a byte value at the given position.
	 *
	 * @param __pos Position to read the byte from.
	 * @return The read byte value.
	 * @throws IOException On read errors.
	 * @since 2016/03/07
	 */
	protected final byte readByte(long __pos)
		throws IOException
	{
		synchronized (_readbuffer)
		{
			return readRaw(__pos, 1).get();
		}
	}
	
	/**
	 * Reads a range of bytes into the given byte array.
	 *
	 * @param __pos Position to read values from.
	 * @param __arr The array to write into.
	 * @param __off The starting offset in the array.
	 * @param __len The length of the array.
	 * @return The input array.
	 * @throws IllegalArgumentException If the offset or length are negative,
	 * or the offset and the length exceeds the array size.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/07
	 */
	protected final byte[] readByteArray(long __pos, byte[] __arr,
		int __off, int __len)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		// Check
		if (__arr == null)
			throw new NullPointerException();
		if (__off < 0 || __len < 0 || (__off + __len) < 0 ||
			(__off + __len) > __arr.length)
			throw new IllegalArgumentException();
		
		// If not reading any bytes, ignore
		if (__len <= 0)
			return __arr;
		
		// Lock on read
		synchronized (_readbuffer)
		{
			// Get the read buffer capacity, that is the amount of bytes to
			// read at once
			int cap = _readbuffer.capacity();
			
			// Read in capacity sized blocks
			for (int i = 0, left = __len; i < __len; i += cap, left -= cap)
			{
				int want = Math.min(left, cap);
				readRaw(__pos + (long)i, want).get(__arr, __off + i, want);
			}
		}
		
		// Return the array
		return __arr;
	}
	
	/**
	 * Reads a little endian integer at the given position.
	 *
	 * @param __pos Position to read from.
	 * @return The read value.
	 * @throws IOException On read errors.
	 * @since 2016/03/02
	 */
	protected final int readInt(long __pos)
		throws IOException
	{
		synchronized (_readbuffer)
		{
			return readRaw(__pos, 4).getInt();
		}
	}
	
	/**
	 * Reads a little endian long value at the given position.
	 *
	 * @param __pos Position to read from.
	 * @return The read value.
	 * @throws IOException On read errors.
	 * @since 2016/03/02
	 */
	protected final long readLong(long __pos)
		throws IOException
	{
		synchronized (_readbuffer)
		{
			// ByteBuffer does not support reading long values sadly
			ByteBuffer val = readRaw(__pos, 8);
			
			// Build values
			int lo = val.getInt();
			return ((long)lo) | (((long)val.getInt()) << 32L);
		}
	}
	
	/**
	 * Reads a little endian short value at the given position.
	 *
	 * @param __pos Position to read from.
	 * @return The read value.
	 * @throws IOException On read errors.
	 * @since 2016/03/02
	 */
	protected final short readShort(long __pos)
		throws IOException
	{
		synchronized (_readbuffer)
		{
			return readRaw(__pos, 2).getShort();
		}
	}
	
	/**
	 * Reads structured information at the given position.
	 *
	 * @param __pos Position to read from.
	 * @param __se The structure element to read data for.
	 * @return The data at this location.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/08
	 */
	protected final long readStruct(long __pos, ZIPStructureElement __se)
		throws IOException, NullPointerException
	{
		return readStruct(__pos, __se, 0);
	}
	
	/**
	 * Reads structured information at the given position.
	 *
	 * @param __pos Position to read from.
	 * @param __se The structure element to read data for.
	 * @param __ai The array index if this is either variable or a fixed size
	 * array, if this is neither then this must be zero.
	 * @return The data at this location.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/08
	 */
	protected final long readStruct(long __pos, ZIPStructureElement __se,
		int __ai)
		throws IOException, NullPointerException
	{
		// Check
		if (__se == null)
			throw new NullPointerException();
		
		// Lock on the read buffer
		synchronized (_readbuffer)
		{
			// Get the type that the element is
			ZIPStructureElement.Type type = __se.type();
			
			// Get the variable field info
			ZIPStructureElement varf = __se.variableField();
			if (__ai != 0 && varf == null)
				throw new ZIPFormatException.NonZeroArrayRead();
			
			// Otherwise check the bounds of the read
			else if (varf != null)
			{
				// Read the count
				long ec = readStruct(__pos, varf);
				
				// Out of bounds?
				if (__ai < 0 || (long)__ai >= ec)
					throw new ZIPFormatException.ArrayOutOfBounds(__ai, ec);
			}
			
			// Calculate the type based relative size based on the array index
			long relpos = type.size() * (long)__ai;
			
			// Add the offset to the current element
			relpos += __se.offset();
			
			// Go through all entries before this to determine if any more
			// variable offsets are needed.
			for (ZIPStructureElement b = __se.before(); b != null;
				b = b.before())
			{
				// If this is not variable then stop
				ZIPStructureElement bvf = b.variableField();
				if (bvf == null)
					break;
				
				// Read the variable field specified
				long numelems = readStruct(__pos, b);
				
				// Get the type and add the element count along with the size
				ZIPStructureElement.Type bt = b.type();
				relpos += numelems * (long)bt.size();
			}
			
			// Read the data at the given position
			return type.read(readRaw(__pos + relpos, type.size()));
		}
	}
	
	/**
	 * Reads raw data from the ZIP.
	 *
	 * @param __pos The position to read from.
	 * @param __len The number of bytes to read.
	 * @return The field {@code _readbuffer} automatically position to the
	 * started and limited to the length.
	 * @throws IllegalArgumentException If the length exceeds the read buffer
	 * size, or the read length is zero or negative.
	 * @throws IOException On read errors or if the input buffer was not
	 * correctly handled.
	 * @since 2016/03/02
	 */
	protected final ByteBuffer readRaw(long __pos, int __len)
		throws IllegalArgumentException, IOException
	{
		// Check
		if (__len <= 0 || __len > _readbuffer.capacity())
			throw new IllegalArgumentException();
		
		// Lock on the read buffer
		synchronized (_readbuffer)
		{
			// Setup buffer for read
			ByteBuffer rv = _readbuffer;
			rv.order(ByteOrder.LITTLE_ENDIAN);
			rv.clear();
			rv.limit(__len);
			
			// Read count
			int rc;
			
			// FileChannel has its own internal locking so it can be directly
			// read.
			if (filechannel != null)
				rc = filechannel.read(rv, __pos);
			
			// Otherwise lock on the channel since it may be used elsewhere or
			// shared between multiple ZIPs and threads potentially
			else
				synchronized (channel)
				{
					// Seek
					channel.position(__pos);
			
					// Perform the read
					rc = channel.read(rv);
				}
			
			// Check to make sure all the data was read
			if (rc < __len)
				throw new ZIPFormatException.ShortRead(__len, rc);
		
			// Flip the buffer
			rv.flip();
		
			// Return the input buffer
			return rv;
		}
	}
	
	/**
	 * Reads a byte value as an unsigned value.
	 *
	 * @param __pos Position to read from.
	 * @return The read unsigned byte value.
	 * @throws IOException On read errors.
	 * @since 2016/03/07
	 */
	protected final int readUnsignedByte(long __pos)
		throws IOException
	{
		return (int)readByte(__pos) & 0xFF;
	}
	
	/**
	 * Reads an int value as an unsigned value.
	 *
	 * @param __pos The position to read from.
	 * @return The unsigned integer value as a long.
	 * @throws IOException On read errors.
	 * @since 2016/03/03
	 */
	protected final long readUnsignedInt(long __pos)
		throws IOException
	{
		return (long)readInt(__pos) & 0xFFFF_FFFFL;
	}
	
	/**
	 * Reads a short value as an unsigned value.
	 *
	 * @param __pos The position to read from.
	 * @return The unsigned short as an int.
	 * @throws IOException On read errors.
	 * @since 2016/03/03
	 */
	protected final int readUnsignedShort(long __pos)
		throws IOException
	{
		return (int)readShort(__pos) & 0xFFFF;
	}
	
	/**
	 * Returns the number of entries in this ZIP file.
	 *
	 * @return The number of entries in the ZIP.
	 * @since 2016/03/05
	 */
	public final int size()
		throws IOException
	{
		return getDirectory().size();
	}
	
	/**
	 * Attempts to open this ZIP file using ZIP64 extensions first, then if
	 * that fails it will fall back to using ZIP32.
	 *
	 * @param __sbc The channel to attempt an open as a ZIP with.
	 * @throws IOException If the channel could not be read from.
	 * @throws NullPointerException On null arguments.
	 * @throws ZIPFormatException If the ZIP was not valid.
	 * @since 2016/03/02
	 */
	public static StandardZIPFile open(SeekableByteChannel __sbc)
		throws IOException, NullPointerException, ZIPFormatException
	{
		// Check
		if (__sbc == null)
			throw new NullPointerException();
		
		// Try opening as a 64-bit ZIP
		try
		{
			return new StandardZIP64File(__sbc);
		}
		
		// Not a ZIP64
		catch (ZIPFormatException zfe)
		{
			// Try treating it as a 32-bit ZIP
			try
			{
				return new StandardZIP32File(__sbc);
			}
			
			// Not a ZIP32 either
			catch (ZIPFormatException zfeb)
			{
				zfeb.addSuppressed(zfe);
				throw zfeb;
			}
		}
	}
	
	/**
	 * Makes the reference array.
	 *
	 * @param __ne Number of elements,
	 * @return The file entry reference array
	 * @since 2016/03/06
	 */
	@SuppressWarnings({"unchecked"})
	private static final Reference<FileEntry>[] __makeRefArray(int __ne)
	{
		return (Reference<FileEntry>[])new Reference[__ne];
	}
	
	/**
	 * This is a stream which provides an input stream over the contents of
	 * a portion of the ZIP file.
	 *
	 * @since 2016/03/09
	 */
	protected final class DataStream
		extends InputStream
	{
		/** Stream lock. */
		protected final Object lock =
			new Object();
		
		/** Start position. */
		protected final long start;
		
		/** End position. */
		protected final long end;
		
		/** Current position. */
		private volatile long _at;
		
		/**
		 * Initializes the data stream.
		 *
		 * @param __start The inclusive start position.
		 * @param __end The exclusive end position.
		 * @since 2016/03/09
		 */
		protected DataStream(long __start, long __end)
		{
			// Set
			start = __start;
			end = __end;
			
			// Start at the start
			_at = start;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/09
		 */
		@Override
		public int available()
		{
			// Lock
			synchronized (lock)
			{
				return (int)Math.min(Integer.MAX_VALUE,
					Math.max(end - _at, 0L));
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/09
		 */
		@Override
		public int read()
			throws IOException
		{
			// Lock
			synchronized (lock)
			{
				// Get current position
				long now = _at;
				
				// EOF?
				if (now >= end)
					return -1;
				
				// Otherwise read character here
				int rv = readUnsignedByte(now);
				
				// Increment position
				_at = now + 1L;
				
				// Return it
				return rv;
			}
		}
	}
	
	/**
	 * This provides a cached directory of the ZIP file contents.
	 *
	 * @since 2016/03/05
	 */
	protected abstract class Directory
		implements Iterable<FileEntry>
	{
		/** The offsets of all the entry directories. */
		protected final long offsets[];
		
		/** The cache of entries. */
		private final Reference<FileEntry> _entrycache[];
		
		/**
		 * Initializes the directory.
		 *
		 * @param __ne The number of entries in the ZIP.
		 * @throws IOException On I/O errors.
		 * @since 2016/03/05
		 */
		protected Directory(int __ne)
			throws IOException
		{
			// Check
			if (__ne < 0)
				throw new ZIPFormatException.NegativeEntryCount(__ne);
			
			// Initialize offset table
			offsets = new long[__ne];
			Arrays.fill(offsets, -1L);
			
			// Entry cache
			_entrycache = __makeRefArray(__ne);
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
		protected abstract FileEntry readEntry(int __dx, long __off)
			throws IOException;
		
		/**
		 * Obtains the entry with the given name.
		 *
		 * @param __n The entry to get which has this name.
		 * @return The entry with the given name or {@code null} if not found.
		 * @since 2016/03/05
		 */
		public final FileEntry get(String __n)
			throws IOException, NullPointerException
		{
			// Check
			if (__n == null)
				throw new NullPointerException();
			
			// Go through self
			for (FileEntry fe : this)
				if (__n.equals(fe.toString()))
					return fe;
			
			// Not found
			return null;
		}
		
		/**
		 * Obtains the entry at this given position.
		 *
		 * @param __i The index to get the entry at.
		 * @return The entry at the given index.
		 * @since 2016/03/05
		 */
		public final FileEntry get(int __i)
			throws IOException
		{
			// If out of bounds, always null
			if (__i < 0 || __i >= offsets.length)
				return null;
			
			// Get the directory offset for this entry
			long off = offsets[__i];
			
			// If the offset is invalid then the entry cannot be determined
			// for this.
			if (off < 0L)
				throw new ZIPFormatException.NoOffsetSpecified(__i);
			
			// Lock on the entry cache so it is a sort of volatile
			synchronized (_entrycache)
			{
				// Get reference here, which might not exist
				Reference<FileEntry> ref = _entrycache[__i];
				FileEntry rv = null;
				
				// In reference?
				if (ref != null)
					rv = ref.get();
				
				// Needs creation?
				if (rv == null)
					ref = new WeakReference<>((rv = readEntry(__i, off)));
				
				// Return it
				return Objects.<FileEntry>requireNonNull(rv);
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/05
		 */
		@Override
		public final Iterator<FileEntry> iterator()
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
		 * This is the iterator over entries.
		 *
		 * @since 2016/03/05
		 */
		private final class __Iterator__
			implements Iterator<FileEntry>
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
			public FileEntry next()
			{
				// Ran out?
				if (!hasNext())
					throw new NoSuchElementException();
				
				// Might not be able to read it
				try
				{
					return get(_dx++);
				}
				
				// Failed to read
				catch (IOException ioe)
				{
					throw new IllegalStateException(ioe);
				}
			}
			
			/**
			 * {@inheritDoc}
			 * @since 2016/03/05
			 */
			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		}
	}
	
	/**
	 * This represents an entry within a standard ZIP file.
	 *
	 * @since 2016/02/03
	 */
	public abstract class FileEntry
	{
		/**
		 * Initializes the file entry.
		 *
		 * @throws IOException On read errors.
		 * @since 2016/03/05
		 */
		protected FileEntry()
			throws IOException
		{
		}
		
		/**
		 * Returns the index of this entry.
		 *
		 * @return The ZIP file entry index.
		 * @since 2016/03/06
		 */
		public abstract int index();
		
		/**
		 * Opens an input stream of the ZIP file data.
		 *
		 * @return A stream which reads the deflated or stored data.
		 * @throws IOException On read errors.
		 * @since 2016/03/06
		 */
		public abstract InputStream open()
			throws IOException;
		
		/**
		 * Returns the name of the file.
		 *
		 * @return The file name.
		 * @throws IOException On read errors.
		 * @since 2016/03/06
		 */
		public abstract String name()
			throws IOException;
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/06
		 */
		@Override
		public final String toString()
		{
			// Possible that the name could not be read
			try
			{
				return name();
			}
			
			// Could not read the name
			catch (IOException ioe)
			{
				return "<IOException>";
			}
		}
	}
}

