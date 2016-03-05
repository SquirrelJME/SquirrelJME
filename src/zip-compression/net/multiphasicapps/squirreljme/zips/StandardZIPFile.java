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
	extends AbstractMap<String, StandardZIPFile.FileEntry>
{
	/** Internal lock. */
	protected final Object lock =
		new Object();
	
	/** The base channel to read from. */
	protected final SeekableByteChannel channel;
	
	/** File channel if this is one. */
	protected final FileChannel filechannel;
	
	/** Read buffer to prevent a thousand allocations at the cost of speed. */
	protected final ByteBuffer readbuffer =
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
	 * Returns the number of entries in this ZIP file.
	 *
	 * @return The ZIP entry count.
	 * @since 2016/03/05
	 */
	public abstract int size();
	
	/**
	 * {@inheritDoc}
	 * @throws IllegalStateException If the directory could not be read.
	 * @since 2016/03/05
	 */
	@Override
	public Set<Map.Entry<String, FileEntry>> entrySet()
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
		synchronized (readbuffer)
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
		synchronized (readbuffer)
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
		synchronized (readbuffer)
		{
			return readRaw(__pos, 2).getShort();
		}
	}
	
	/**
	 * Reads raw data from the ZIP.
	 *
	 * @param __pos The position to read from.
	 * @param __len The number of bytes to read.
	 * @return The field {@code readbuffer} automatically position to the
	 * started and limited to the length.
	 * @throws IllegalArgumentException If the length exceeds the read buffer
	 * size, or the read length is zero or negative.
	 * @throws IOException On read errors or if the input buffer was not
	 * correctly handled.
	 * @since 2016/03/02
	 */
	protected final ByteBuffer readRaw(long __pos, int __len)
		throws IOException
	{
		// Check
		if (__len <= 0 || __len > readbuffer.capacity())
			throw new IllegalArgumentException();
		
		// Lock on the read buffer
		synchronized (readbuffer)
		{
			// Setup buffer for read
			ByteBuffer rv = readbuffer;
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
	 * This provides a cached directory of the ZIP file contents.
	 *
	 * @since 2016/03/05
	 */
	protected abstract class Directory
		extends AbstractSet<Map.Entry<String, FileEntry>>
	{
		/** The offsets of all the entry directories. */
		protected final long offsets[];
		
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
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/05
		 */
		@Override
		public final Iterator<Map.Entry<String, FileEntry>> iterator()
		{
			return new __Iterator__();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/05
		 */
		@Override
		public final int size()
		{
			throw new Error("TODO");
		}
		
		/**
		 * This is the iterator over entries.
		 *
		 * @since 2016/03/05
		 */
		private final class __Iterator__
			implements Iterator<Map.Entry<String, FileEntry>>
		{
			/** The current index. */
			private volatile int _dx =
				0;
			
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
			public Map.Entry<String, FileEntry> next()
			{
				return new FileEntry()
					{
					};
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
		implements Map.Entry<String, FileEntry>
	{
		/**
		 * Initializes the file entry.
		 *
		 * @since 2016/03/05
		 */
		protected FileEntry()
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/05
		 */
		@Override
		public final boolean equals(Object __o)
		{
			// Must be this
			if (!(__o instanceof FileEntry))
				return false;
			
			// Due to the infinite recursion potential, it is only equal if
			// this is equal
			return __o == this;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/05
		 */
		@Override
		public final String getKey()
		{
			return this.toString();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/05
		 */
		@Override
		public final FileEntry getValue()
		{
			return this;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/05
		 */
		@Override
		public final int hashCode()
		{
			// This violates the map hash code because this recursively uses
			// itself
			return getKey().hashCode();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/05
		 */
		@Override
		public final FileEntry setValue(FileEntry __v)
		{
			// Cannot modify entries at all
			throw new UnsupportedOperationException();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/05
		 */
		@Override
		public String toString()
		{
			return "TODO";
		}
	}
}

