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
 * ZIP Structure:
 * {@code
 * end of central dir signature    4 bytes  (0x06054b50) [22]
 * number of this disk             2 bytes [18]
 * number of the disk with the
 * start of the central directory  2 bytes [16]
 * total number of entries in the
 * central directory on this disk  2 bytes [14]
 * total number of entries in
 * the central directory           2 bytes [12]
 * size of the central directory   4 bytes [10]
 * offset of start of central
 * directory with respect to
 * the starting disk number        4 bytes [6]
 * .ZIP file comment length        2 bytes [2]
 * .ZIP file comment       (variable size)
 * }
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
	
	/** The magic number of the end directory. */
	protected static final int END_DIRECTORY_MAGIC =
		0x06054B50;
	
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
		long maxsearch = Math.max(0L, channel.size() - MAX_END_DIRECTORY_SIZE);
		for (long i = channel.size() - BASE_END_DIRECTORY_SIZE; i >= 0; i--)
		{
			// Read magic number here
			int maybe = readInt(i);
			
			// If this is the magic, then stop
			if (maybe == END_DIRECTORY_MAGIC)
			{
				idi = i;
				break;
			}
		}
		
		// Not a 32-bit ZIP?
		if (idi < 0L)
			throw new ZIPFormatException("Could not find a the end " +
				"directory of a 32-bit ZIP file.");
		
		// Read the disk number and the disk containing the root of the central
		// directory
		int thisdisk = (int)readShort(idi + EDO_DISK_NUMBER) & 0xFFFF;
		int diskstar = (int)readShort(idi + EDO_DISK_START) & 0xFFFF;
		int diskents = (int)readShort(idi + EDO_DISK_ENTRY_COUNT) & 0xFFFF;
		int totlents = (int)readShort(idi + EDO_ALL_ENTRY_COUNT) & 0xFFFF;
		long dirsize = (long)readInt(idi + EDO_CENTRAL_DIR_SIZE) &
			0xFFFF_FFFFL;
		long diroffs = readInt(idi + EDO_CENTRAL_DIR_OFFSET);
		

		throw new Error("TODO");
	}
}

