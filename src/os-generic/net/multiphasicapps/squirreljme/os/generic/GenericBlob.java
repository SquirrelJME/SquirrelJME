// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.os.generic;

import net.multiphasicapps.io.data.RandomAccessData;

/**
 * This class supports reading the executable blob format.
 *
 * Blobs are laid out similarly to ZIP files.
 * {@code
 * [entries]
 * [central index]
 * [end header]
 * }
 *
 * The entries are laid out as the following. All entries start on an aligned
 * address of 4.
 * {@code
 * [int           : start entry magic number]
 * [byte          : The type of entry this is (1 = class, 2 = resource)]
 * [byte          : The number of bytes used to represent the entry name]
 * [short         : The length of the entry name]
 * [byte[]/short[]: The name of the entry, chars encoded as 0-255 or 0-65535]
 * [... Align to 4 bytes ...]
 * [byte[]        : data...]
 * [int           : end entry magic number]
 * [int           : The data size]
 * [int           : CRC32 of the data]
 * }
 *
 * The central index is laid out in the following manner.
 * {@code
 * [... Align to 4 bytes ...]
 * [int: Central directory magic number]
 * [???: Central index entries]
 * }
 *
 * Each central index entry is of the following.
 * {@code
 * [int: The number of bytes to subtract from the CD to reach the entry MN]
 * [int: The size of the data for the given entry]
 * }
 *
 * The end header is laid out in the following manner.
 * {@code
 * [... Align to 4 bytes ...]
 * [int: End header magic number]
 * [int: The number of entries in the central index]
 * [int: Bytes to subtract from the magic position to reach the central index]
 * }
 *
 * @since 2016/08/11
 */
public class GenericBlob
{
	/** The magic number identifying entry start. */
	public static final int START_ENTRY_MAGIC_NUMBER =
		0xD3CECDCA;
	
	/** The magic number identifying entry end. */
	public static final int END_ENTRY_MAGIC_NUMBER =
		0xCCEAF8AE;
	
	/** Central directory magic number. */
	public static final int CENTRAL_DIRECTORY_MAGIC_NUMBER =
		0xC1CCD2C1;
	
	/** The magic number for the end header. */
	public static final int END_HEADER_MAGIC_NUMBER =
		0xC2F6E5AE;
	
	/** Reflect the data in the CRC? */
	public static final boolean CRC_REFLECT_DATA =
		true;
	
	/** Reflect the remainder in the CRC? */
	public static final boolean CRC_REFLECT_REMAINDER =
		true;
	
	/** The CRC magic number. */
	public static final int CRC_MAGIC_NUMBER =
		0xC6FAF2FD;
	
	/** The initial remainder. */
	public static final int CRC_INITIAL_REMAINDER =
		0xF1F2F3F4;
	
	/** The final remainder XOR value. */
	public static final int CRC_FINAL_XOR =
		0xFFFFFFFF;
	
	/** The blob data. */
	protected final RandomAccessData data;
	
	/**
	 * Initializes the blob and uses the given random data.
	 *
	 * @param __d The data to the blob.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/11
	 */
	public GenericBlob(RandomAccessData __d)
		throws NullPointerException
	{
		// Check
		if (__d == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.data = __d;
	}
}

