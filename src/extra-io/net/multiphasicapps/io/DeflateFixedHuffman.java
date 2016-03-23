// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.IOException;
import net.multiphasicapps.buffers.CircularBooleanBuffer;
import net.multiphasicapps.buffers.CircularByteBuffer;

/**
 * This contains the fixed huffman tree.
 *
 * @since 2016/03/11
 */
public class DeflateFixedHuffman
{
	/**
	 * Not initialized.
	 *
	 * @since 2016/03/11
	 */
	private DeflateFixedHuffman()
	{
	}
	
	/**
	 * Reads a fixed huffman code for use by the {@code TYPE_FIXED_HUFFMAN}
	 * state. This method does not traverse a huffman tree so to speak, but it
	 * instead uses many if statements. Initially every consideration was made
	 * but now instead it uses ranges once it keeps deep enough into the tree.
	 * This method is faster and provides a built-in huffman tree while not
	 * taking up too many bytes in the byte code.
	 *
	 * This is for the deprecated {@link InflaterInputStream}.
	 *
	 * @param __in The input stream to source bytes from.
	 * @return The read value.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/10
	 */
	public static int read(BitInputStream __in)
		throws IOException, NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// The long if statement block
		if (__in.read())
			if (__in.read())
				if (__in.read())
					return 192 + __in.readBitsInt(6);
				else
					if (__in.read())
						return 160 + __in.readBitsInt(5);
					else
						if (__in.read())
							return 144 + __in.readBitsInt(4);
						else
							return 280 + __in.readBitsInt(3);
			else
				return 80 + __in.readBitsInt(6);
		else
			if (__in.read())
				return 16 + __in.readBitsInt(6);
			else
				if (__in.read())
					if (__in.read())
						return 0 + __in.readBitsInt(4);
					else
						return 272 + __in.readBitsInt(3);
				else
					return 256 + __in.readBitsInt(4);
	}
	
	/**
	 * Reads a fixed huffman code for use by the {@code TYPE_FIXED_HUFFMAN}
	 * state. This method does not traverse a huffman tree so to speak, but it
	 * instead uses many if statements. Initially every consideration was made
	 * but now instead it uses ranges once it keeps deep enough into the tree.
	 * This method is faster and provides a built-in huffman tree while not
	 * taking up too many bytes in the byte code.
	 *
	 * This is for the data decoder version.
	 *
	 * @param __in The circular boolean buffer to get bits from.
	 * @return The read value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/10
	 */
	public static int read(CircularBooleanBuffer __in)
		throws NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// The long if statement block
		if (__in.removeFirstPrimitive())
			if (__in.removeFirstPrimitive())
				if (__in.removeFirstPrimitive())
					return 192 + __in.removeFirstInt(6, true);
				else
					if (__in.removeFirstPrimitive())
						return 160 + __in.removeFirstInt(5, true);
					else
						if (__in.removeFirstPrimitive())
							return 144 + __in.removeFirstInt(4, true);
						else
							return 280 + __in.removeFirstInt(3, true);
			else
				return 80 + __in.removeFirstInt(6, true);
		else
			if (__in.removeFirstPrimitive())
				return 16 + __in.removeFirstInt(6, true);
			else
				if (__in.removeFirstPrimitive())
					if (__in.removeFirstPrimitive())
						return 0 + __in.removeFirstInt(4, true);
					else
						return 272 + __in.removeFirstInt(3, true);
				else
					return 256 + __in.removeFirstInt(4, true);
	}
}

