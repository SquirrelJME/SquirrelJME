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
 * Encodes character data to ISO-8859-1.
 *
 * @since 2018/09/20
 */
public final class ISO88591Encoder
	implements Encoder
{
	/**
	 * {@inheritDoc}
	 * @since 2018/09/21
	 */
	@Override
	public int encode(char __c, byte[] __b, int __o, int __l)
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
		
		// These characters are invalid, so they all become the replacement
		// character
		if (__c >= 0x100)
			__b[__o] = '?';
		
		// Encode as is
		else
			__b[__o] = (byte)__c;
		
		// Only single characters written
		return 1;
	}
}

