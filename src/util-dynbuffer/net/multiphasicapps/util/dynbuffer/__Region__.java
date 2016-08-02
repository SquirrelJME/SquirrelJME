// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.dynbuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a region which contains multiple data partitions.
 *
 * @since 2016/08/02
 */
final class __Region__
{
	/**
	 * {@squirreljme.property net.multiphasicapps.util.dynbuffer.regionsize=n
	 * This specifies the number of bytes that a single region in a dynamic
	 * byte buffer should consume.}
	 */
	private static final int _REGION_SIZE =
		Math.max(Integer.getInteger(
			"net.multiphasicapps.util.dynbuffer.regionsize", 128), 8);
	
	/** The owning buffer. */
	protected final DynamicByteBuffer owner;
	
	/** Physical partitions in this region. */
	final List<__Partition__> _physical =
		new ArrayList<>();
	
	/** The region data. */
	final byte[] _data =
		new byte[_REGION_SIZE];
	
	/** The region index. */
	volatile int _index;
	
	/**
	 * Initializes the region.
	 *
	 * @param __owner The owning byte buffer.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/02
	 */
	__Region__(DynamicByteBuffer __owner)
		throws NullPointerException
	{
		// Check
		if (__owner == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __owner;
	}
}

