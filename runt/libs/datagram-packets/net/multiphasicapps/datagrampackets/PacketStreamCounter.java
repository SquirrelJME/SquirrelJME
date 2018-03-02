// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.datagrampackets;

/**
 * This class counts the number of packets read and written to and from the
 * packet stream.
 *
 * @since 2018/01/12
 */
public final class PacketStreamCounter
{
	/** Packets read. */
	private volatile int _rp;
	
	/** Bytes read. */
	private volatile long _rb;
	
	/** Packets written. */
	private volatile int _wp;
	
	/** Bytes written. */
	private volatile long _wb;
	
	/**
	 * Counts a read packet.
	 *
	 * @param __len The packet length.
	 * @since 2018/01/12
	 */
	final void __countRead(int __len)
	{
		this._rp++;
		this._rb += __len;
	}
	
	/**
	 * Counts a written packet.
	 *
	 * @param __len The packet length.
	 * @since 2018/01/12
	 */
	final void __countWrite(int __len)
	{
		this._wp++;
		this._wb += __len;
	}
}

