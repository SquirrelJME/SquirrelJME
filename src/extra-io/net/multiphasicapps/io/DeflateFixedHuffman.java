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
					if (__in.read())
						if (__in.read())
							if (__in.read())
								if (__in.read())
									if (__in.read())
										if (__in.read())
											return 255;
										else
											return 254;
									else
										if (__in.read())
											return 253;
										else
											return 252;
								else
									if (__in.read())
										if (__in.read())
											return 251;
										else
											return 250;
									else
										if (__in.read())
											return 249;
										else
											return 248;
							else
								if (__in.read())
									if (__in.read())
										if (__in.read())
											return 247;
										else
											return 246;
									else
										if (__in.read())
											return 245;
										else
											return 244;
								else
									if (__in.read())
										if (__in.read())
											return 243;
										else
											return 242;
									else
										if (__in.read())
											return 241;
										else
											return 240;
						else
							if (__in.read())
								if (__in.read())
									if (__in.read())
										if (__in.read())
											return 239;
										else
											return 238;
									else
										if (__in.read())
											return 237;
										else
											return 236;
								else
									if (__in.read())
										if (__in.read())
											return 235;
										else
											return 234;
									else
										if (__in.read())
											return 233;
										else
											return 232;
							else
								if (__in.read())
									if (__in.read())
										if (__in.read())
											return 231;
										else
											return 230;
									else
										if (__in.read())
											return 229;
										else
											return 228;
								else
									if (__in.read())
										if (__in.read())
											return 227;
										else
											return 226;
									else
										if (__in.read())
											return 225;
										else
											return 224;
					else
						if (__in.read())
							if (__in.read())
								if (__in.read())
									if (__in.read())
										if (__in.read())
											return 223;
										else
											return 222;
									else
										if (__in.read())
											return 221;
										else
											return 220;
								else
									if (__in.read())
										if (__in.read())
											return 219;
										else
											return 218;
									else
										if (__in.read())
											return 217;
										else
											return 216;
							else
								if (__in.read())
									if (__in.read())
										if (__in.read())
											return 215;
										else
											return 214;
									else
										if (__in.read())
											return 213;
										else
											return 212;
								else
									if (__in.read())
										if (__in.read())
											return 211;
										else
											return 210;
									else
										if (__in.read())
											return 209;
										else
											return 208;
						else
							if (__in.read())
								if (__in.read())
									if (__in.read())
										if (__in.read())
											return 207;
										else
											return 206;
									else
										if (__in.read())
											return 205;
										else
											return 204;
								else
									if (__in.read())
										if (__in.read())
											return 203;
										else
											return 202;
									else
										if (__in.read())
											return 201;
										else
											return 200;
							else
								if (__in.read())
									if (__in.read())
										if (__in.read())
											return 199;
										else
											return 198;
									else
										if (__in.read())
											return 197;
										else
											return 196;
								else
									if (__in.read())
										if (__in.read())
											return 195;
										else
											return 194;
									else
										if (__in.read())
											return 193;
										else
											return 192;
				else
					if (__in.read())
						if (__in.read())
							if (__in.read())
								if (__in.read())
									if (__in.read())
										if (__in.read())
											return 191;
										else
											return 190;
									else
										if (__in.read())
											return 189;
										else
											return 188;
								else
									if (__in.read())
										if (__in.read())
											return 187;
										else
											return 186;
									else
										if (__in.read())
											return 185;
										else
											return 184;
							else
								if (__in.read())
									if (__in.read())
										if (__in.read())
											return 183;
										else
											return 182;
									else
										if (__in.read())
											return 181;
										else
											return 180;
								else
									if (__in.read())
										if (__in.read())
											return 179;
										else
											return 178;
									else
										if (__in.read())
											return 177;
										else
											return 176;
						else
							if (__in.read())
								if (__in.read())
									if (__in.read())
										if (__in.read())
											return 175;
										else
											return 174;
									else
										if (__in.read())
											return 173;
										else
											return 172;
								else
									if (__in.read())
										if (__in.read())
											return 171;
										else
											return 170;
									else
										if (__in.read())
											return 169;
										else
											return 168;
							else
								if (__in.read())
									if (__in.read())
										if (__in.read())
											return 167;
										else
											return 166;
									else
										if (__in.read())
											return 165;
										else
											return 164;
								else
									if (__in.read())
										if (__in.read())
											return 163;
										else
											return 162;
									else
										if (__in.read())
											return 161;
										else
											return 160;
					else
						if (__in.read())
							if (__in.read())
								if (__in.read())
									if (__in.read())
										if (__in.read())
											return 159;
										else
											return 158;
									else
										if (__in.read())
											return 157;
										else
											return 156;
								else
									if (__in.read())
										if (__in.read())
											return 155;
										else
											return 154;
									else
										if (__in.read())
											return 153;
										else
											return 152;
							else
								if (__in.read())
									if (__in.read())
										if (__in.read())
											return 151;
										else
											return 150;
									else
										if (__in.read())
											return 149;
										else
											return 148;
								else
									if (__in.read())
										if (__in.read())
											return 147;
										else
											return 146;
									else
										if (__in.read())
											return 145;
										else
											return 144;
						else
							if (__in.read())
								if (__in.read())
									if (__in.read())
										return 287;
									else
										return 286;
								else
									if (__in.read())
										return 285;
									else
										return 284;
							else
								if (__in.read())
									if (__in.read())
										return 283;
									else
										return 282;
								else
									if (__in.read())
										return 281;
									else
										return 280;
			else
				if (__in.read())
					if (__in.read())
						if (__in.read())
							if (__in.read())
								if (__in.read())
									if (__in.read())
										return 143;
									else
										return 142;
								else
									if (__in.read())
										return 141;
									else
										return 140;
							else
								if (__in.read())
									if (__in.read())
										return 139;
									else
										return 138;
								else
									if (__in.read())
										return 137;
									else
										return 136;
						else
							if (__in.read())
								if (__in.read())
									if (__in.read())
										return 135;
									else
										return 134;
								else
									if (__in.read())
										return 133;
									else
										return 132;
							else
								if (__in.read())
									if (__in.read())
										return 131;
									else
										return 130;
								else
									if (__in.read())
										return 129;
									else
										return 128;
					else
						if (__in.read())
							if (__in.read())
								if (__in.read())
									if (__in.read())
										return 127;
									else
										return 126;
								else
									if (__in.read())
										return 125;
									else
										return 124;
							else
								if (__in.read())
									if (__in.read())
										return 123;
									else
										return 122;
								else
									if (__in.read())
										return 121;
									else
										return 120;
						else
							if (__in.read())
								if (__in.read())
									if (__in.read())
										return 119;
									else
										return 118;
								else
									if (__in.read())
										return 117;
									else
										return 116;
							else
								if (__in.read())
									if (__in.read())
										return 115;
									else
										return 114;
								else
									if (__in.read())
										return 113;
									else
										return 112;
				else
					if (__in.read())
						if (__in.read())
							if (__in.read())
								if (__in.read())
									if (__in.read())
										return 111;
									else
										return 110;
								else
									if (__in.read())
										return 109;
									else
										return 108;
							else
								if (__in.read())
									if (__in.read())
										return 107;
									else
										return 106;
								else
									if (__in.read())
										return 105;
									else
										return 104;
						else
							if (__in.read())
								if (__in.read())
									if (__in.read())
										return 103;
									else
										return 102;
								else
									if (__in.read())
										return 101;
									else
										return 100;
							else
								if (__in.read())
									if (__in.read())
										return 99;
									else
										return 98;
								else
									if (__in.read())
										return 97;
									else
										return 96;
					else
						if (__in.read())
							if (__in.read())
								if (__in.read())
									if (__in.read())
										return 95;
									else
										return 94;
								else
									if (__in.read())
										return 93;
									else
										return 92;
							else
								if (__in.read())
									if (__in.read())
										return 91;
									else
										return 90;
								else
									if (__in.read())
										return 89;
									else
										return 88;
						else
							if (__in.read())
								if (__in.read())
									if (__in.read())
										return 87;
									else
										return 86;
								else
									if (__in.read())
										return 85;
									else
										return 84;
							else
								if (__in.read())
									if (__in.read())
										return 83;
									else
										return 82;
								else
									if (__in.read())
										return 81;
									else
										return 80;
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

