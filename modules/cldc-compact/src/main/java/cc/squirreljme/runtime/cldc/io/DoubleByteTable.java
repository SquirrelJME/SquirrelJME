// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Table for use with double byte characters.
 * 
 * The table is in the following format:
 *  - For every valid A byte:
 *    - {@code uint8_t} The actual A byte.
 *    - {@code uint8_t} The number of B characters for the A byte.
 *    - Then for every B byte:
 *      - {@code uint8_t} The B byte.
 *      - {@code uint16_t} The 16-bit unicode value of the character.
 *
 * @since 2022/02/14
 */
public final class DoubleByteTable
{
	/** The "A" byte Table. */
	private final __BTable__[] _aTable;
	
	/**
	 * Initializes the double byte table.
	 * 
	 * @param __aTable The "A" byte Table.
	 * @throws IndexOutOfBoundsException If the table size is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/14
	 */
	private DoubleByteTable(__BTable__[] __aTable)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__aTable == null)
			throw new NullPointerException("NARG");
		if (__aTable.length != 256)
			throw new IndexOutOfBoundsException("IOOB");
		
		this._aTable = __aTable;
	}
	
	/**
	 * Decodes a double byte shift-jis character.
	 * 
	 * @param __a The first byte.
	 * @param __b The second byte.
	 * @return The decoded character, will return a negative value if unknown.
	 * @since 2022/02/14
	 */
	public int decode(byte __a, byte __b)
	{
		// Find the B table for the A character
		__BTable__ bTable = this._aTable[__a & 0xFF];
		
		// Is there no BTable?
		if (bTable == null)
			return -1;
		
		// Find the B character
		return bTable.decode(__b);
	}
	
	/**
	 * Loads the double byte table.
	 * 
	 * @return The double byte table.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/14
	 */
	public static DoubleByteTable loadTable(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// We use a specific format for this
		DataInputStream in = new DataInputStream(__in);
		
		// The resultant A Table
		__BTable__[] aTable = new __BTable__[256];
		
		// Load in the various B Tables
		for (;;)
		{
			// Read the A byte this belongs to
			int aByte = in.read();
			
			// EOF?
			if (aByte < 0)
				break;
			
			// Read the number of characters in the "B" tread
			int numChars = in.readUnsignedByte();
			
			// Setup arrays for the B tread
			short[] bBytes = new short[numChars];
			char[] bChars = new char[numChars];
			
			// Read the characters available
			for (int i = 0; i < numChars; i++)
			{
				bBytes[i] = (short)in.readUnsignedByte();
				bChars[i] = in.readChar();
			}
			
			// Add this tread
			aTable[aByte] = new __BTable__(bBytes, bChars);
		}
		
		// Setup final table
		return new DoubleByteTable(aTable);
	}
	
	/**
	 * Represents an A-table within the Shift-JIS Table.
	 * 
	 * @since 2022/02/14
	 */
	private static final class __BTable__
	{
		/** The B bytes. */
		private final short[] _bBytes;
		
		/** The B characters. */
		private final char[] _bChars;
		
		/**
		 * Initializes the individual B table.
		 * 
		 * @param __bBytes The B bytes.
		 * @param __bChars The B characters.
		 * @throws IllegalArgumentException If the arrays are of different
		 * length.
		 * @throws NullPointerException On null arguments.
		 * @since 2022/02/14
		 */
		__BTable__(short[] __bBytes, char[] __bChars)
			throws IllegalArgumentException, NullPointerException
		{
			if (__bBytes == null || __bChars == null)
				throw new NullPointerException("NARG");
			if (__bBytes.length != __bChars.length)
				throw new IllegalArgumentException("IAEE");
			
			this._bBytes = __bBytes;
			this._bChars = __bChars;
		}
		
		/**
		 * Decodes the B value.
		 * 
		 * @param __b The B character.
		 * @return The decoded character or a negative value if not valid.
		 * @since 2022/02/14
		 */
		public int decode(byte __b)
		{
			// Find the index of the character
			int index = Arrays.binarySearch(this._bBytes, (short)(__b & 0xFF));
			if (index < 0)
				return -1;
			
			// Return the character it is
			return this._bChars[index];
		}
	}
}
