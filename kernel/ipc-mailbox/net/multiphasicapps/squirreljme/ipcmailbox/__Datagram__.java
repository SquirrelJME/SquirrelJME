// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ipcmailbox;

/**
 * This contains a single datagram.
 *
 * @since 2016/10/13
 */
final class __Datagram__
{
	/** The channel sent on. */
	final int _channel;
	
	/** The datagram data. */
	final byte[] _data;
	
	/**
	 * Initializes the datagram.
	 *
	 * @param __chan The channel to send on.
	 * @param __b The bytes to send.
	 * @param __o The offset to the data.
	 * @param __l The number of bytes to send.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/13
	 */
	__Datagram__(int __chan, byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("AIOB");
		
		// Set
		this._channel = __chan;
		byte[] data = new byte[__l];
		System.arraycopy(__b, __o, data, 0, __l);
		this._data = data;
	}
}

