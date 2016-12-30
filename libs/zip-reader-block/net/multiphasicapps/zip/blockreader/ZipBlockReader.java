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
	private static final int _END_DIRECTORY_MAGIC_NUMBER =
		0x06054B50;
	
	/** The offset to the field for the number of entries in this disk. */
	private static final int _END_DIRECTORY_DISK_ENTRIES_OFFSET =
		8;
	
	/** The offset to the size of the central directory. */
	private static final int _END_DIRECTORY_CENTRAL_DIR_SIZE_OFFSET =
		_END_DIRECTORY_DISK_ENTRIES_OFFSET + 4;
	
	/** The offset to the offset of the central directory. */
	private static final int _END_DIRECTORY_CENTRAL_DIR_OFFSET_OFFSET =
		_END_DIRECTORY_CENTRAL_DIR_SIZE_OFFSET + 4;
	
	/** The minimum length of the end central directory record. */
	private static final int _END_DIRECTORY_MIN_LENGTH =
		22;
	
	/** The maximum length of the end central directory record. */
	private static final int _END_DIRECTORY_MAX_LENGTH =
		_END_DIRECTORY_MIN_LENGTH + 65535;
	
	/** The accessor to use for ZIP files. */
	protected final BlockAccessor accessor;
	
	/** The number of entries in this ZIP. */
	protected final int numentries;
	
	/** The base address for the central directory. */
	protected final long cdirbase;
	
	/** The actual start position for the ZIP file. */
	protected final long zipbaseaddr;
	
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
		byte[] dirbytes = new byte[_END_DIRECTORY_MIN_LENGTH];
		long endat = __locateCentralDirEnd(__b, dirbytes);
		
		// Get the number of entries which are in this disk and not in the
		// archive as a whole, since multi-archive ZIP files are not supported
		int numentries = __ArrayData__.readUnsignedShort(
			_END_DIRECTORY_DISK_ENTRIES_OFFSET, dirbytes);
		this.numentries = numentries;
		
		// Need the size of the central directory to determine where it
		// actually starts
		long cdirsize = __ArrayData__.readUnsignedInt(
			_END_DIRECTORY_CENTRAL_DIR_SIZE_OFFSET, dirbytes);
		
		// This is the position of the start of the central directory
		long cdirbase = endat - cdirsize;
		this.cdirbase = cdirbase;
		
		// Determine the base address of the ZIP file since all entries
		// are relative from the start point
		long csz = __b.size();
		this.zipbaseaddr = csz - (__ArrayData__.readUnsignedInt(
			_END_DIRECTORY_CENTRAL_DIR_OFFSET_OFFSET, dirbytes) + cdirsize +
			(csz - endat));
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
		if (size < _END_DIRECTORY_MIN_LENGTH)
			throw new IOException(String.format("CJ02 %d", size));
		
		// Constantly search for the end of the central directory
		for (long at = size - _END_DIRECTORY_MIN_LENGTH, end =
			Math.max(0, size - _END_DIRECTORY_MAX_LENGTH); at >= end; at--)
		{
			// Read single byte to determine if it might start a header
			byte b = __b.read(at);
			if (b != 0x50)
				continue;
			
			// Read entire buffer (but not the comment in)
			__b.read(at, __db, 0, _END_DIRECTORY_MIN_LENGTH);
			
			// Need to check the magic number
			if (__ArrayData__.readSignedInt(0, __db) !=
				_END_DIRECTORY_MAGIC_NUMBER)
				continue;
			
			// Length must match the end also
			if (__ArrayData__.readUnsignedShort(_END_DIRECTORY_MIN_LENGTH - 2,
				__db) != (size - (at + _END_DIRECTORY_MIN_LENGTH)))
				continue;
			
			// Central directory is here
			return at;
		}
		
		// {@squirreljme.error CJ05 Could not find the end of the central
		// directory in the ZIP file.}
		throw new IOException("CJ05");
	}
}

