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
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
	/** This is the size of the basic end directory of a ZIP file. */
	protected static final long BASE_END_DIRECTORY_SIZE =
		22;
	
	/** The maximum end directory size (includes comment). */
	protected static final long MAX_END_DIRECTORY_SIZE =
		BASE_END_DIRECTORY_SIZE + 65535L;
	
	/** Offset to the disk number in the end directory. */
	protected static final long EDO_DISK_NUMBER =
		4;
	
	/** Offset to the disk containing the start of the directory. */
	protected static final long EDO_DISK_START =
		6;
	
	/** Offset to the total number of entries in this disk. */
	protected static final long EDO_DISK_ENTRY_COUNT =
		8;
	
	/** Offset to the total number of entries in the entire disk collection. */
	protected static final long EDO_ALL_ENTRY_COUNT =
		10;
	
	/** The size of the central directory. */
	protected static final long EDO_CENTRAL_DIR_SIZE =
		12;
	
	/** The offset to the start of the central directory. */
	protected static final long EDO_CENTRAL_DIR_OFFSET =
		16;
	
	/** The offset to the comment size. */
	protected static final long EDO_COMMENT_LENGTH =
		20;
	
	/** The magic number of the end directory. */
	protected static final int END_DIRECTORY_MAGIC =
		0x06054B50;
	
	/** The byte offset of the central directory. */
	protected final long cdirbase;
	
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
			int maybe = readInt(i);
			
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
		long dirsize = readUnsignedInt(idi + EDO_CENTRAL_DIR_SIZE);
		
		// Set the position to read the central directory from
		cdirbase = (idi) - dirsize;
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
	 * {@inheritDoc}
	 * @since 2016/03/05
	 */
	@Override
	public int size()
	{
		return numentries;
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
		}
	}
}

