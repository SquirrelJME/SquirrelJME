// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

/**
 * Encodes to EBCDIC IBM037.
 *
 * @since 2018/09/24
 */
public final class IBM037Encoder
	extends IBM037Base
	implements Encoder
{
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
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Always encodes to one character, so if one character cannot fit in
		// the buffer then fail
		if (__l < 1)
			return -1;
		
		// Invalid characters are turned into question marks
		if (__c >= 0x100)
			__c = '?';
		
		// Map
		__b[__o] = IBM037Base._MAP[__c];
		
		// Only single characters written
		return 1;
	}
}

