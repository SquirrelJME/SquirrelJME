// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp.rr;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a data packet which is recorded and read from an input replay
 * stream.
 *
 * This class is not thread safe and is mutable.
 *
 * @since 2016/06/01
 */
public class RRDataPacket
{
	/** The objects for input and output. */
	protected final List<Object> data =
		new ArrayList<>();
	
	/** The command packet type. */
	private volatile RRDataCommand _command;
	
	/** The number of elements in the packet. */
	private volatile int _count;
	
	/**
	 * Initializes the data packet.
	 *
	 * @since 2016/06/01
	 */
	public RRDataPacket()
	{
	}
	
	/**
	 * Returns the data at the specified index.
	 *
	 * @param __i The index to get data for.
	 * @return The data at the specified index.
	 * @throws IndexOutOfBoundsException If the index it outside of bounds.
	 * @since 2016/06/01
	 */
	public Object get(int __i)
		throws IndexOutOfBoundsException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the amount of data elements in the packet.
	 *
	 * @return The number of elements in the packet.
	 * @since 2016/06/01
	 */
	public int length()
	{
		throw new Error("TODO");
	}
}

