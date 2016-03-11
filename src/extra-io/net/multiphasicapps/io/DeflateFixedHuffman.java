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
				if (__in.read())
					if (__in.read())
						if (__in.read())
							if (__in.read())
								if (__in.read())
									if (__in.read())
										return 79;
									else
										return 78;
								else
									if (__in.read())
										return 77;
									else
										return 76;
							else
								if (__in.read())
									if (__in.read())
										return 75;
									else
										return 74;
								else
									if (__in.read())
										return 73;
									else
										return 72;
						else
							if (__in.read())
								if (__in.read())
									if (__in.read())
										return 71;
									else
										return 70;
								else
									if (__in.read())
										return 69;
									else
										return 68;
							else
								if (__in.read())
									if (__in.read())
										return 67;
									else
										return 66;
								else
									if (__in.read())
										return 65;
									else
										return 64;
					else
						if (__in.read())
							if (__in.read())
								if (__in.read())
									if (__in.read())
										return 63;
									else
										return 62;
								else
									if (__in.read())
										return 61;
									else
										return 60;
							else
								if (__in.read())
									if (__in.read())
										return 59;
									else
										return 58;
								else
									if (__in.read())
										return 57;
									else
										return 56;
						else
							if (__in.read())
								if (__in.read())
									if (__in.read())
										return 55;
									else
										return 54;
								else
									if (__in.read())
										return 53;
									else
										return 52;
							else
								if (__in.read())
									if (__in.read())
										return 51;
									else
										return 50;
								else
									if (__in.read())
										return 49;
									else
										return 48;
				else
					if (__in.read())
						if (__in.read())
							if (__in.read())
								if (__in.read())
									if (__in.read())
										return 47;
									else
										return 46;
								else
									if (__in.read())
										return 45;
									else
										return 44;
							else
								if (__in.read())
									if (__in.read())
										return 43;
									else
										return 42;
								else
									if (__in.read())
										return 41;
									else
										return 40;
						else
							if (__in.read())
								if (__in.read())
									if (__in.read())
										return 39;
									else
										return 38;
								else
									if (__in.read())
										return 37;
									else
										return 36;
							else
								if (__in.read())
									if (__in.read())
										return 35;
									else
										return 34;
								else
									if (__in.read())
										return 33;
									else
										return 32;
					else
						if (__in.read())
							if (__in.read())
								if (__in.read())
									if (__in.read())
										return 31;
									else
										return 30;
								else
									if (__in.read())
										return 29;
									else
										return 28;
							else
								if (__in.read())
									if (__in.read())
										return 27;
									else
										return 26;
								else
									if (__in.read())
										return 25;
									else
										return 24;
						else
							if (__in.read())
								if (__in.read())
									if (__in.read())
										return 23;
									else
										return 22;
								else
									if (__in.read())
										return 21;
									else
										return 20;
							else
								if (__in.read())
									if (__in.read())
										return 19;
									else
										return 18;
								else
									if (__in.read())
										return 17;
									else
										return 16;
			else
				if (__in.read())
					if (__in.read())
						if (__in.read())
							if (__in.read())
								if (__in.read())
									if (__in.read())
										return 15;
									else
										return 14;
								else
									if (__in.read())
										return 13;
									else
										return 12;
							else
								if (__in.read())
									if (__in.read())
										return 11;
									else
										return 10;
								else
									if (__in.read())
										return 9;
									else
										return 8;
						else
							if (__in.read())
								if (__in.read())
									if (__in.read())
										return 7;
									else
										return 6;
								else
									if (__in.read())
										return 5;
									else
										return 4;
							else
								if (__in.read())
									if (__in.read())
										return 3;
									else
										return 2;
								else
									if (__in.read())
										return 1;
									else
										return 0;
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

