// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.blockreader;

import java.io.Closeable;
import java.io.IOException;

/**
 * This class is used to read ZIP files in a random access fashion.
 *
 * @since 2016/12/27
 */
public class ZipBlockReader
	implements Closeable
{
	/** The magic number for the end directory. */
	private static final int END_DIRECTORY_MAGIC_NUMBER =
		0x06054B50;
	
	/** The minimum length of the end central directory record. */
	private static final int END_DIRECTORY_MIN_LENGTH =
		22;
	
	/** The maximum length of the end central directory record. */
	private static final int END_DIRECTORY_MAX_LENGTH =
		END_DIRECTORY_MIN_LENGTH + 65535;
	
	/** The accessor to use for ZIP files. */
	protected final BlockAccessor accessor;
	
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
	 * @since 2016/12/27
	 */
	public ZipBlockReader(BlockAccessor __b)
		throws IOException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.accessor = __b;
		
		// Locate the end of the central directory
		byte[] dirbytes = new byte[END_DIRECTORY_MIN_LENGTH];
		long endat = __locateCentralDirEnd(__b, dirbytes);
		
		throw new Error("TODO");
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
	private static final long __locateCentralDirEnd(BlockAccessor __b,
		byte[] __db)
		throws IOException, NullPointerException
	{
		// Check
		if (__b == null || __db == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CJ02 The file is too small to be a ZIP file.
		// (The size of file)}
		long size = __b.size();
		if (size < END_DIRECTORY_MIN_LENGTH)
			throw new IOException(String.format("CJ02 %d", size));
		
		// Constantly search for the end of the central directory
		for (long at = size - END_DIRECTORY_MIN_LENGTH, end =
			Math.max(0, size - END_DIRECTORY_MAX_LENGTH); at >= end; at--)
		{
			// Read single byte to determine if it might start a header
			byte b = __b.read(at);
			if (b != 0x50)
				continue;
			
			// Read entire buffer (but not the comment in)
			__b.read(at, __db, 0, END_DIRECTORY_MIN_LENGTH);
			
			// Need to check the magic number
			if (__ArrayData__.readSignedInt(0, __db) !=
				END_DIRECTORY_MAGIC_NUMBER)
				continue;
			
			// Length must match the end also
			if (__ArrayData__.readUnsignedShort(END_DIRECTORY_MIN_LENGTH - 2,
				__db) != (size - (at + END_DIRECTORY_MIN_LENGTH)))
				continue;
			
			// Central directory is here
			return at;
		}
		
		// {@squirreljme.error CJ05 Could not find the end of the central
		// directory in the ZIP file.}
		throw new IOException("CJ05");
	}
}

