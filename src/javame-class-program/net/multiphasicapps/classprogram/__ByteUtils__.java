// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

/**
 * This contains methods for reading data from a byte array.
 *
 * {@squirreljme.error CP17 Out of bounds read.}
 *
 * @since 2016/03/30
 */
final class __ByteUtils__
{
	/**
	 * Not used.
	 *
	 * @since 2016/03/30
	 */
	private __ByteUtils__()
	{
		throw new RuntimeException("TODO");
	}
	
	/**
	 * Reads an signed byte from the given buffer.
	 *
	 * @param __code The byte array.
	 * @param __pos The position of the short.
	 * @return The read value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/15
	 */
	static int __readSByte(byte[] __code, int __pos)
		throws NullPointerException
	{
		// Check
		if (__code == null)
			throw new NullPointerException("NARG");
		
		// Read and merge the values (big endian)
		try
		{
			return (((int)__code[__pos]));
		}
		
		// Read out of bounds
		catch (IndexOutOfBoundsException e)
		{
			throw new CPProgramException("CP17", e);
		}
	}
	
	/**
	 * Reads a signed integer from the given buffer.
	 *
	 * @param __code The byte array.
	 * @param __pos The position of the integer.
	 * @return The read value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/29
	 */
	static int __readSInt(byte[] __code, int __pos)
		throws NullPointerException
	{
		// Check
		if (__code == null)
			throw new NullPointerException("NARG");
		
		// Read and merge the values (big endian)
		try
		{
			return ((((int)__code[__pos]) & 0xFF) << 24) |
				((((int)__code[__pos + 1]) & 0xFF) << 16) |
				((((int)__code[__pos + 2]) & 0xFF) << 8) |
				((((int)__code[__pos + 3]) & 0xFF));
		}
		
		// Read out of bounds
		catch (IndexOutOfBoundsException e)
		{
			throw new CPProgramException("CP17", e);
		}
	}
	
	/**
	 * Reads a signed short from the given buffer.
	 *
	 * @param __code The code buffer.
	 * @param __pos The position to read from.
	 * @return The signed short value which was read.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/30
	 */
	static int __readSShort(byte[] __code, int __pos)
		throws NullPointerException
	{
		return ((short)__readUShort(__code, __pos));
	}
	
	/**
	 * Reads an unsigned byte from the given buffer.
	 *
	 * @param __code The byte array.
	 * @param __pos The position of the short.
	 * @return The read value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/30
	 */
	static int __readUByte(byte[] __code, int __pos)
		throws NullPointerException
	{
		// Check
		if (__code == null)
			throw new NullPointerException("NARG");
		
		// Read and merge the values (big endian)
		try
		{
			return (((int)__code[__pos]) & 0xFF);
		}
		
		// Read out of bounds
		catch (IndexOutOfBoundsException e)
		{
			throw new CPProgramException("CP17", e);
		}
	}
	
	/**
	 * Reads an unsigned short from the given buffer.
	 *
	 * @param __code The byte array.
	 * @param __pos The position of the short.
	 * @return The read value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/30
	 */
	static int __readUShort(byte[] __code, int __pos)
		throws NullPointerException
	{
		// Check
		if (__code == null)
			throw new NullPointerException("NARG");
		
		// Read and merge the values (big endian)
		try
		{
			return ((((int)__code[__pos + 0]) & 0xFF) << 8) |
				((((int)__code[__pos + 1]) & 0xFF));
		}
		
		// Read out of bounds
		catch (IndexOutOfBoundsException e)
		{
			throw new CPProgramException("CP17", e);
		}
	}
}

