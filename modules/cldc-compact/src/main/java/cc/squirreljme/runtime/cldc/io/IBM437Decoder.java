// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

/**
 * Decodes the IBM047 codepage.
 *
 * @since 2025/02/10
 */
public class IBM437Decoder
	implements Decoder
{
	/**
	 * {@inheritDoc}
	 * @since 2025/02/10
	 */
	@Override
	public double averageSequenceLength()
	{
		return 1.0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/02/10
	 */
	@Override
	public int decode(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Not enough characters for decoding
		if (__l == 0)
			return -1;
		
		// Lower ASCII is the same
		char c = (char)(__b[0] & 0xFF);
		if (c <= 127)
			return c;
		
		// Otherwise specific translation is used
		switch (c)
		{
			case 128: return 0x00C7;
			case 129: return 0x00FC;
			case 130: return 0x00E9;
			case 131: return 0x00E2;
			case 132: return 0x00E4;
			case 133: return 0x00E0;
			case 134: return 0x00E5;
			case 135: return 0x00E7;
			case 136: return 0x00EA;
			case 137: return 0x00EB;
			case 138: return 0x00E8;
			case 139: return 0x00EF;
			case 140: return 0x00EE;
			case 141: return 0x00EC;
			case 142: return 0x00C4;
			case 143: return 0x00C5;
			case 144: return 0x00C9;
			case 145: return 0x00E6;
			case 146: return 0x00C6;
			case 147: return 0x00F4;
			case 148: return 0x00F6;
			case 149: return 0x00F2;
			case 150: return 0x00FB;
			case 151: return 0x00F9;
			case 152: return 0x00FF;
			case 153: return 0x00D6;
			case 154: return 0x00DC;
			case 155: return 0x00A2;
			case 156: return 0x00A3;
			case 157: return 0x00A5;
			case 158: return 0x20A7;
			case 159: return 0x0192;
			case 160: return 0x00E1;
			case 161: return 0x00ED;
			case 162: return 0x00F3;
			case 163: return 0x00FA;
			case 164: return 0x00F1;
			case 165: return 0x00D1;
			case 166: return 0x00AA;
			case 167: return 0x00BA;
			case 168: return 0x00BF;
			case 169: return 0x2310;
			case 170: return 0x00AC;
			case 171: return 0x00BD;
			case 172: return 0x00BC;
			case 173: return 0x00A1;
			case 174: return 0x00AB;
			case 175: return 0x00BB;
			case 176: return 0x2591;
			case 177: return 0x2592;
			case 178: return 0x2593;
			case 179: return 0x2502;
			case 180: return 0x2524;
			case 181: return 0x2561;
			case 182: return 0x2562;
			case 183: return 0x2556;
			case 184: return 0x2555;
			case 185: return 0x2563;
			case 186: return 0x2551;
			case 187: return 0x2557;
			case 188: return 0x255D;
			case 189: return 0x255C;
			case 190: return 0x255B;
			case 191: return 0x2510;
			case 192: return 0x2514;
			case 193: return 0x2534;
			case 194: return 0x252C;
			case 195: return 0x251C;
			case 196: return 0x2500;
			case 197: return 0x253C;
			case 198: return 0x255E;
			case 199: return 0x255F;
			case 200: return 0x255A;
			case 201: return 0x2554;
			case 202: return 0x2569;
			case 203: return 0x2566;
			case 204: return 0x2560;
			case 205: return 0x2550;
			case 206: return 0x256C;
			case 207: return 0x2567;
			case 208: return 0x2568;
			case 209: return 0x2564;
			case 210: return 0x2565;
			case 211: return 0x2559;
			case 212: return 0x2558;
			case 213: return 0x2552;
			case 214: return 0x2553;
			case 215: return 0x256B;
			case 216: return 0x256A;
			case 217: return 0x2518;
			case 218: return 0x250C;
			case 219: return 0x2588;
			case 220: return 0x2584;
			case 221: return 0x258C;
			case 222: return 0x2590;
			case 223: return 0x2580;
			case 224: return 0x03B1;
			case 225: return 0x00DF;
			case 226: return 0x0393;
			case 227: return 0x03C0;
			case 228: return 0x03A3;
			case 229: return 0x03C3;
			case 230: return 0x00B5;
			case 231: return 0x03C4;
			case 232: return 0x03A6;
			case 233: return 0x0398;
			case 234: return 0x03A9;
			case 235: return 0x03B4;
			case 236: return 0x221E;
			case 237: return 0x03C6;
			case 238: return 0x03B5;
			case 239: return 0x2229;
			case 240: return 0x2261;
			case 241: return 0x00B1;
			case 242: return 0x2265;
			case 243: return 0x2264;
			case 244: return 0x2320;
			case 245: return 0x2321;
			case 246: return 0x00F7;
			case 247: return 0x2248;
			case 248: return 0x00B0;
			case 249: return 0x2219;
			case 250: return 0x00B7;
			case 251: return 0x221A;
			case 252: return 0x207F;
			case 253: return 0x00B2;
			case 254: return 0x25A0;
			case 255: return 0x00A0;
			
				// Invalid
			default:
				return 0xFFFD;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/02/10
	 */
	@Override
	public String encodingName()
	{
		return "ibm437";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/02/10
	 */
	@Override
	public int maximumSequenceLength()
	{
		return 1;
	}
}
