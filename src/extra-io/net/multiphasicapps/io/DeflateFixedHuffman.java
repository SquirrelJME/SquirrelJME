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
	 * instead uses many if statements
	 *
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
			throw new NullPointerException();
		
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
						if (__in.read())
							if (__in.read())
								if (__in.read())
									return 279;
								else
									return 278;
							else
								if (__in.read())
									return 277;
								else
									return 276;
						else
							if (__in.read())
								if (__in.read())
									return 275;
								else
									return 274;
							else
								if (__in.read())
									return 273;
								else
									return 272;
				else
					if (__in.read())
						if (__in.read())
							if (__in.read())
								if (__in.read())
									return 271;
								else
									return 270;
							else
								if (__in.read())
									return 269;
								else
									return 268;
						else
							if (__in.read())
								if (__in.read())
									return 267;
								else
									return 266;
							else
								if (__in.read())
									return 265;
								else
									return 264;
					else
						if (__in.read())
							if (__in.read())
								if (__in.read())
									return 263;
								else
									return 262;
							else
								if (__in.read())
									return 261;
								else
									return 260;
						else
							if (__in.read())
								if (__in.read())
									return 259;
								else
									return 258;
							else
								if (__in.read())
									return 257;
								else
									return 256;
	}
}

