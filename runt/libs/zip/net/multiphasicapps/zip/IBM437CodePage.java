// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip;

/**
 * This contains methods for converting from the IBM 437 codepage to the UTF-16
 * which Java uses.
 *
 * @since 2016/03/07
 */
public final class IBM437CodePage
{
	/**
	 * Not initializes.
	 *
	 * @since 2016/03/07
	 */
	private IBM437CodePage()
	{
	}
	
	/**
	 * Translates the given byte to a character.
	 *
	 * @param __b Byte to translate.
	 * @return The translated value.
	 * @since 2016/03/07
	 */
	public static char byteToChar(byte __b)
	{
		return byteToChar(((int)__b) & 0xFF);
	}
	
	/**
	 * Translates the given integer to a character.
	 *
	 * @param __b Integer to translate.
	 * @return The translated value.
	 * @throws IllegalArgumentException If the input byte is not in the range
	 * of [0, 255].
	 * @since 2016/03/07
	 */
	public static char byteToChar(int __b)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BF01 Byte index is not within bounds.
		// (The byte)}
		if (__b < 0 || __b > 255)
			throw new IllegalArgumentException(String.format("BF01 %d", __b));
		
		// Lower ASCII is the same
		if (__b <= 127)
			return (char)__b;
		
		// Otherwise specific translation is used
		switch (__b)
		{
			case 128: return (char)0x00C7;
			case 129: return (char)0x00FC;
			case 130: return (char)0x00E9;
			case 131: return (char)0x00E2;
			case 132: return (char)0x00E4;
			case 133: return (char)0x00E0;
			case 134: return (char)0x00E5;
			case 135: return (char)0x00E7;
			case 136: return (char)0x00EA;
			case 137: return (char)0x00EB;
			case 138: return (char)0x00E8;
			case 139: return (char)0x00EF;
			case 140: return (char)0x00EE;
			case 141: return (char)0x00EC;
			case 142: return (char)0x00C4;
			case 143: return (char)0x00C5;
			case 144: return (char)0x00C9;
			case 145: return (char)0x00E6;
			case 146: return (char)0x00C6;
			case 147: return (char)0x00F4;
			case 148: return (char)0x00F6;
			case 149: return (char)0x00F2;
			case 150: return (char)0x00FB;
			case 151: return (char)0x00F9;
			case 152: return (char)0x00FF;
			case 153: return (char)0x00D6;
			case 154: return (char)0x00DC;
			case 155: return (char)0x00A2;
			case 156: return (char)0x00A3;
			case 157: return (char)0x00A5;
			case 158: return (char)0x20A7;
			case 159: return (char)0x0192;
			case 160: return (char)0x00E1;
			case 161: return (char)0x00ED;
			case 162: return (char)0x00F3;
			case 163: return (char)0x00FA;
			case 164: return (char)0x00F1;
			case 165: return (char)0x00D1;
			case 166: return (char)0x00AA;
			case 167: return (char)0x00BA;
			case 168: return (char)0x00BF;
			case 169: return (char)0x2310;
			case 170: return (char)0x00AC;
			case 171: return (char)0x00BD;
			case 172: return (char)0x00BC;
			case 173: return (char)0x00A1;
			case 174: return (char)0x00AB;
			case 175: return (char)0x00BB;
			case 176: return (char)0x2591;
			case 177: return (char)0x2592;
			case 178: return (char)0x2593;
			case 179: return (char)0x2502;
			case 180: return (char)0x2524;
			case 181: return (char)0x2561;
			case 182: return (char)0x2562;
			case 183: return (char)0x2556;
			case 184: return (char)0x2555;
			case 185: return (char)0x2563;
			case 186: return (char)0x2551;
			case 187: return (char)0x2557;
			case 188: return (char)0x255D;
			case 189: return (char)0x255C;
			case 190: return (char)0x255B;
			case 191: return (char)0x2510;
			case 192: return (char)0x2514;
			case 193: return (char)0x2534;
			case 194: return (char)0x252C;
			case 195: return (char)0x251C;
			case 196: return (char)0x2500;
			case 197: return (char)0x253C;
			case 198: return (char)0x255E;
			case 199: return (char)0x255F;
			case 200: return (char)0x255A;
			case 201: return (char)0x2554;
			case 202: return (char)0x2569;
			case 203: return (char)0x2566;
			case 204: return (char)0x2560;
			case 205: return (char)0x2550;
			case 206: return (char)0x256C;
			case 207: return (char)0x2567;
			case 208: return (char)0x2568;
			case 209: return (char)0x2564;
			case 210: return (char)0x2565;
			case 211: return (char)0x2559;
			case 212: return (char)0x2558;
			case 213: return (char)0x2552;
			case 214: return (char)0x2553;
			case 215: return (char)0x256B;
			case 216: return (char)0x256A;
			case 217: return (char)0x2518;
			case 218: return (char)0x250C;
			case 219: return (char)0x2588;
			case 220: return (char)0x2584;
			case 221: return (char)0x258C;
			case 222: return (char)0x2590;
			case 223: return (char)0x2580;
			case 224: return (char)0x03B1;
			case 225: return (char)0x00DF;
			case 226: return (char)0x0393;
			case 227: return (char)0x03C0;
			case 228: return (char)0x03A3;
			case 229: return (char)0x03C3;
			case 230: return (char)0x00B5;
			case 231: return (char)0x03C4;
			case 232: return (char)0x03A6;
			case 233: return (char)0x0398;
			case 234: return (char)0x03A9;
			case 235: return (char)0x03B4;
			case 236: return (char)0x221E;
			case 237: return (char)0x03C6;
			case 238: return (char)0x03B5;
			case 239: return (char)0x2229;
			case 240: return (char)0x2261;
			case 241: return (char)0x00B1;
			case 242: return (char)0x2265;
			case 243: return (char)0x2264;
			case 244: return (char)0x2320;
			case 245: return (char)0x2321;
			case 246: return (char)0x00F7;
			case 247: return (char)0x2248;
			case 248: return (char)0x00B0;
			case 249: return (char)0x2219;
			case 250: return (char)0x00B7;
			case 251: return (char)0x221A;
			case 252: return (char)0x207F;
			case 253: return (char)0x00B2;
			case 254: return (char)0x25A0;
			case 255: return (char)0x00A0;
			
				// Should not occur but in case it does
			default:
				throw new todo.OOPS();
		}
	}
	
	/**
	 * Converts the input byte array to a string.
	 *
	 * @param __arr Input byte array containing IBM 437 characters.
	 * @param __off Offset from the start of the buffer to read from.
	 * @param __len The number of bytes to read.
	 * @throws IllegalArgumentException If the offset or length are negative,
	 * or the offset and the length exceeds the array size.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/07
	 */
	public static String toString(byte[] __arr, int __off, int __len)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__arr == null)
			throw new NullPointerException("NARG");
		if (__off < 0 || __len < 0 || (__off + __len) < 0 ||
			(__off + __len) > __arr.length)
			throw new IllegalArgumentException("BAOB");
		
		// Result is of the same size
		char rv[] = new char[__len];
		
		// Go through it
		for (int i = 0; i < __len; i++)
			rv[i] = byteToChar(__arr[__off + i]);
		
		// Build it
		return new String(rv);
	}
}

