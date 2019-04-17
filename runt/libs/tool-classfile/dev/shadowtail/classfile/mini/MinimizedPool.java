// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.mini;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.multiphasicapps.classfile.InvalidClassFormatException;

/**
 * This represents the minimized constant pool.
 *
 * @since 2019/04/16
 */
public final class MinimizedPool
{
	
	/**
	 * Decodes the minimized constant pool.
	 *
	 * @param __n Number of entries in the pool.
	 * @param __is The bytes to decode from.
	 * @param __o The offset into the array.
	 * @param __l The length of the array.
	 * @return The resulting minimized pool.
	 * @throws InvalidClassFormatException If the class is not formatted
	 * correctly.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/16
	 */
	public static final MinimizedPool decode(int __n, byte[] __is, int __o,
		int __l)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		if (__is == null)
			throw new NullPointerException("NARG");
		
		DataInputStream dis = new DataInputStream(
			new ByteArrayInputStream(__is, __o, __l));
		
		// Debug
		todo.DEBUG.note("Decode %d (%d bytes)", __n, __l);
		
		// Read type table
		byte[] types = new byte[__n];
		dis.readFully(types);
		
		// Skip padding?
		if ((__n & 1) != 0)
			dis.read();
		
		// Read offsets into the structure
		int[] offsets = new int[__n];
		for (int i = 0; i < __n; i++)
			offsets[i] = dis.readUnsignedShort();
		
		// Read of all the various entries
		for (int i = 0; i < __n; i++)
			todo.DEBUG.note("%3d: %02x (@%d)", i,
				(types[i] & 0xFF), offsets[i]);
		
		// Output pool entry types, values, and parts
		int[][] parts = new int[__n][];
		Object[] values = new Object[__n];
		
		// Zero int for empty parts
		int[] nopart = new int[0];
		
		// Re-build individual pool entries
		Object[] entries = new Object[__n];
		for (int i = 0; i < __n; i++)
		{
			// Get type and pointer
			int rawtype = types[i],
				ptr = offsets[i];
			
			// Is this wide?
			boolean iswide;
			if ((iswide = ((rawtype & 0x80) != 0)))
			{
				rawtype &= 0x7F;
				
				// Re-adjust type array since we use this for the type list
				types[i] = (byte)rawtype;
			}
			
			// Read info
			int[] part = null;
			Object v = null;
			
			// Depends on the type
			MinimizedPoolEntryType type = MinimizedPoolEntryType.of(rawtype);
			switch (type)
			{
					// Null is nothing, so do not bother
				case NULL:
					break;
				
				default:
					throw new todo.OOPS(type.name());
			}
			
			// Set data
			parts[i] = (part == null ? nopart : part);
			values[i] = v;
		}
		
		throw new todo.TODO();
	}
}

