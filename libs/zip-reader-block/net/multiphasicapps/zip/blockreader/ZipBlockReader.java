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
		throw new Error("TODO");
	}
}

