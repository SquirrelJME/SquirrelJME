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
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.multiphasicapps.collections.MissingCollections;

/**
 * This represents a standard ZIP file.
 *
 * 32-bit ZIPs may only have 65k entries max and are limited to 4GiB in size,
 * individual files with their header information cannot exceed 4GiB also.
 *
 * Multi-part ZIPs are treated as single part ZIP files, attempting to read
 * entries which are split between archives will result in
 * {@link IOExceptions}.
 *
 * @since 2016/03/02
 */
public class StandardZIP32File
	extends StandardZIPFile
{
	/** Magic number offset. */
	protected static final long EDO_MAGIC_NUMBER =
		0;
	
	/** Offset to the disk number in the end directory. */
	protected static final long EDO_DISK_NUMBER =
		EDO_MAGIC_NUMBER + 4;
	
	/** Offset to the disk containing the start of the directory. */
	protected static final long EDO_DISK_START =
		EDO_DISK_NUMBER + 2;
	
	/** Offset to the total number of entries in this disk. */
	protected static final long EDO_DISK_ENTRY_COUNT =
		EDO_DISK_START + 2;
	
	/** Offset to the total number of entries in the entire disk collection. */
	protected static final long EDO_ALL_ENTRY_COUNT =
		EDO_DISK_ENTRY_COUNT + 2;
	
	/** The size of the central directory. */
	protected static final long EDO_CENTRAL_DIR_SIZE =
		EDO_ALL_ENTRY_COUNT + 2;
	
	/** The offset to the start of the central directory. */
	protected static final long EDO_CENTRAL_DIR_OFFSET =
		EDO_CENTRAL_DIR_SIZE + 4;
	
	/** The offset to the comment size. */
	protected static final long EDO_COMMENT_LENGTH =
		EDO_CENTRAL_DIR_OFFSET + 4;
		
	/** This is the size of the basic end directory of a ZIP file. */
	protected static final long BASE_END_DIRECTORY_SIZE =
		EDO_COMMENT_LENGTH + 2;
	
	/** The maximum end directory size (includes comment). */
	protected static final long MAX_END_DIRECTORY_SIZE =
		BASE_END_DIRECTORY_SIZE + 65535L;
	
	/** The magic number of the end directory. */
	protected static final int END_DIRECTORY_MAGIC =
		0x06054B50;
	
	/** Offset to central directory magic number. */
	protected static final long CDO_MAGIC_NUMBER =
		0;
	
	/** Version the entry was made by. */
	protected static final long CDO_BY_VERSION =
		CDO_MAGIC_NUMBER + 4;
	
	/** Version needed to extract. */
	protected static final long CDO_EXTRACT_VERSION =
		CDO_BY_VERSION + 2;
	
	/** General purpose bit flag. */
	protected static final long CDO_GENERAL_PURPOSE_FLAGS =
		CDO_EXTRACT_VERSION + 2;
	
	/** Compression Method. */
	protected static final long CDO_COMPRESSION_METHOD =
		CDO_GENERAL_PURPOSE_FLAGS + 2;
	
	/** Last modification time. */
	protected static final long CDO_LAST_MODIFIED_TIME =
		CDO_COMPRESSION_METHOD + 2;
	
	/** Last modification date. */
	protected static final long CDO_LAST_MODIFIED_DATE =
		CDO_LAST_MODIFIED_TIME + 2;
	
	/** CRC-32. */
	protected static final long CDO_CRC =
		CDO_LAST_MODIFIED_DATE + 2;
	
	/** Compressed Size. */
	protected static final long CDO_COMPRESSED_SIZE =
		CDO_CRC + 4;
	
	/** Uncompressed size. */
	protected static final long CDO_UNCOMPRESSED_SIZE =
		CDO_COMPRESSED_SIZE + 4;
	
	/** File name length. */
	protected static final long CDO_FILE_NAME_LENGTH =
		CDO_UNCOMPRESSED_SIZE + 4;
	
	/** Extra field length. */
	protected static final long CDO_EXTRA_FIELD_LENGTH =
		CDO_FILE_NAME_LENGTH + 2;
	
	/** Comment Length. */
	protected static final long CDO_COMMENT_LENGTH =
		CDO_EXTRA_FIELD_LENGTH + 2;
	
	/** Disk number start. */
	protected static final long CDO_DISK_NUMBER_START =
		CDO_COMMENT_LENGTH + 2;
	
	/** Internal file attributes. */
	protected static final long CDO_INTERNAL_ATTRIBUTES =
		CDO_DISK_NUMBER_START + 2;
	
	/** External file attributes. */
	protected static final long CDO_EXTERNAL_ATTRIBUTES =
		CDO_INTERNAL_ATTRIBUTES + 2;
	
	/** Relative offset to local header. */
	protected static final long CDO_LOCAL_HEADER_OFFSET =
		CDO_EXTERNAL_ATTRIBUTES + 4;
	
	/** The base size of the central directory. */
	protected static final long BASE_CENTRAL_DIRECTORY_SIZE =
		CDO_LOCAL_HEADER_OFFSET + 4;
	
	/** The file directory magic number. */
	protected static final int CENTRAL_DIRECTORY_MAGIC =
		0x02014B50;
	
	/** The byte offset of the central directory. */
	protected final long cdirbase;
	
	/** The size of the central directory. */
	protected final long cdirsize;
	
	/** The number of entries in this ZIP. */
	protected final int numentries;
	
	/**
	 * Initializes a 32-bit ZIP file.
	 *
	 * @param __sbc The source channel to read from.
	 * @throws IOException On read errors.
	 * @throws ZIPFormatException If this is not a valid ZIP file.
	 * @since 2016/03/02
	 */
	public StandardZIP32File(SeekableByteChannel __sbc)
		throws IOException, ZIPFormatException
	{
		super(__sbc);
		
		// Try to locate the end of the central index
		long idi = Long.MIN_VALUE;
		long csz = channel.size();
		long maxsearch = Math.max(0L, csz - MAX_END_DIRECTORY_SIZE);
		for (long i = csz - BASE_END_DIRECTORY_SIZE; i >= 0; i--)
		{
			// Read magic number here
			int maybe = readInt(i + EDO_MAGIC_NUMBER);
			
			// If this is the magic, then check the comment length
			if (maybe == END_DIRECTORY_MAGIC)
			{
				// Read length
				int comlen = readUnsignedShort(i + EDO_COMMENT_LENGTH);
				
				// The comment must end at the the end of the ZIP
				if ((i + BASE_END_DIRECTORY_SIZE + comlen) == csz)
				{
					idi = i;
					break;
				}
			}
		}
		
		// Not a 32-bit ZIP?
		if (idi < 0L)
			throw new ZIPFormatException.NoCentralDirectory();
		
		// The number of entries in this ZIP
		numentries = readUnsignedShort(idi + EDO_DISK_ENTRY_COUNT);
		cdirsize = readUnsignedInt(idi + EDO_CENTRAL_DIR_SIZE);
		
		// Set the position to read the central directory from
		cdirbase = idi - cdirsize;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/05
	 */
	@Override
	protected Directory readDirectory()
		throws IOException
	{
		return new Directory32();
	}
	
	/**
	 * This represents the 32-bit ZIP directory.
	 *
	 * @since 2016/03/05
	 */
	protected class Directory32
		extends Directory
	{
		/**
		 * Initializes the 32-bit directory.
		 *
		 * @throws IOException On read/write errors.
		 * @since 2016/03/05
		 */
		protected Directory32()
			throws IOException
		{
			super(numentries);
			
			// Read the directory
			long end = cdirbase + cdirsize;
			int readcount = 0;
			for (long p = cdirbase; p < end; readcount++)
			{
				// Read magic number
				int cdmag = readInt(p);
				
				// Bad magic?
				if (cdmag != CENTRAL_DIRECTORY_MAGIC)
					throw new ZIPFormatException.IllegalMagic(cdmag,
						CENTRAL_DIRECTORY_MAGIC);
				
				// Set offset
				offsets[readcount] = p;
				
				// Read variable length attributes
				long varfn = readUnsignedShort(p + CDO_FILE_NAME_LENGTH);
				long varef = readUnsignedShort(p + CDO_EXTRA_FIELD_LENGTH);
				long varcm = readUnsignedShort(p + CDO_COMMENT_LENGTH);
				
				// Skip ahead
				p += BASE_CENTRAL_DIRECTORY_SIZE + varfn + varef + varcm;
			}
			
			// Short read?
			if (readcount != numentries)
				throw new ZIPFormatException.EntryMiscount(readcount,
					numentries);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/06
		 */
		@Override
		protected FileEntry readEntry(int __dx, long __off)
			throws IOException
		{
			return new FileEntry32(__dx, __off);
		}
	}
	
	/**
	 * This represents a 32-bit ZIP file entry.
	 *
	 * @since 2016/03/06
	 */
	protected class FileEntry32
		extends FileEntry
	{
		/** Index. */
		protected final int index;
		
		/** Central directory position. */
		protected final long centraldirpos;
		
		/**
		 * Initializes the file entry.
		 *
		 * @param __dx Index of this entry.
		 * @param __off The central directory offset of this entry.
		 * @since 2016/03/06
		 */
		protected FileEntry32(int __dx, long __off)
		{
			// Set
			index = __dx;
			centraldirpos = __off;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/06
		 */
		@Override
		public int index()
		{
			throw new Error("TODO");
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/06
		 */
		@Override
		public InputStream open()
			throws IOException
		{
			throw new Error("TODO");
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/06
		 */
		@Override
		public String name()
			throws IOException
		{
			throw new Error("TODO");
		}
	}
}

