// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

/**
 * Encodes to EBCDIC IBM037.
 *
 * @since 2018/09/24
 */
public final class IBM037Encoder
	implements Encoder
{
	/** Mapping of char to byte. */
	private static final byte[] _MAP =
		new byte[]
		{
			0, 1, 2, 3, 55, 45, 46, 47, 22, 5, 37, 11, 12, 13, 14, 15, 16, 17,
			18, 19, 60, 61, 50, 38, 24, 25, 63, 39, 28, 29, 30, 31, 64, 90,
			127, 123, 91, 108, 80, 125, 77, 93, 92, 78, 107, 96, 75, 97, -16,
			-15, -14, -13, -12, -11, -10, -9, -8, -7, 122, 94, 76, 126, 110,
			111, 124, -63, -62, -61, -60, -59, -58, -57, -56, -55, -47, -46,
			-45, -44, -43, -42, -41, -40, -39, -30, -29, -28, -27, -26, -25,
			-24, -23, -70, -32, -69, -80, 109, 121, -127, -126, -125, -124,
			-123, -122, -121, -120, -119, -111, -110, -109, -108, -107, -106,
			-105, -104, -103, -94, -93, -92, -91, -90, -89, -88, -87, -64, 79,
			-48, -95, 7, 32, 33, 34, 35, 36, 0, 6, 23, 40, 41, 42, 43, 44, 9,
			10, 27, 48, 49, 26, 51, 52, 53, 54, 8, 56, 57, 58, 59, 4, 20, 62,
			-1, 65, -86, 74, -79, -97, -78, 106, -75, -67, -76, -102, -118, 95,
			-54, -81, -68, -112, -113, -22, -6, -66, -96, -74, -77, -99, -38,
			-101, -117, -73, -72, -71, -85, 100, 101, 98, 102, 99, 103, -98,
			104, 116, 113, 114, 115, 120, 117, 118, 119, -84, 105, -19, -18,
			-21, -17, -20, -65, -128, -3, -2, -5, -4, -83, -82, 89, 68, 69, 66,
			70, 67, 71, -100, 72, 84, 81, 82, 83, 88, 85, 86, 87, -116, 73,
			-51, -50, -53, -49, -52, -31, 112, -35, -34, -37, -36, -115, -114,
			-33
		};
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/24
	 */
	@Override
	public final int encode(char __c, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Always encodes to one character, so if one character cannot fit in
		// the buffer then fail
		if (__l < 1)
			return -1;
		
		// Invalid characters are turned into question marks
		if (__c >= 0x100)
			__c = '?';
		
		// Map
		__b[__o] = _MAP[__c];
		
		// Only single characters written
		return 1;
	}
	
	/**
	 * {@inheritDc}
	 * @since 2018/10/13
	 */
	@Override
	public final String encodingName()
	{
		return "ibm037";
	}
}

