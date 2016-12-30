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

/**
 * This class contains methods for reading data from arrays, all read values
 * are little endian.
 *
 * @since 2016/12/28
 */
final class __ArrayData__
{
	/**
	 * Reads a signed integer from the array.
	 *
	 * @param __off The offset to read from.
	 * @param __b The array to read from.
	 * @return The read value.
	 * @since 2016/12/28
	 */
	static int readSignedInt(int __off, byte[] __b)
	{
		return (__b[__off + 3] << 24) |
			((__b[__off + 2] & 0xFF) << 16) |
			((__b[__off + 1] & 0xFF) << 8) |
			(__b[__off] & 0xFF);
	}
	
	/**
	 * Reads a signed short from the array.
	 *
	 * @param __off The offset to read from.
	 * @param __b The array to read from.
	 * @return The read value.
	 * @since 2016/12/28
	 */
	static int readSignedShort(int __off, byte[] __b)
	{
		return (__b[__off + 1] << 8) |
			(__b[__off] & 0xFF);
	}
	
	/**
	 * Reads an unsigned integer from the array.
	 *
	 * @param __off The offset to read from.
	 * @param __b The array to read from.
	 * @return The read value.
	 * @since 2016/12/28
	 */
	static long readUnsignedInt(int __off, byte[] __b)
	{
		return ((__b[__off + 3] & 0xFF) << 24) |
			((__b[__off + 2] & 0xFF) << 16) |
			((__b[__off + 1] & 0xFF) << 8) |
			(__b[__off] & 0xFF);
	}
	
	/**
	 * Reads an unsigned short from the array.
	 *
	 * @param __off The offset to read from.
	 * @param __b The array to read from.
	 * @return The read value.
	 * @since 2016/12/28
	 */
	static int readUnsignedShort(int __off, byte[] __b)
	{
		return ((__b[__off + 1] & 0xFF) << 8) |
			(__b[__off] & 0xFF);
	}
}

