// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.os.generic.BlobContentType;

/**
 * This stores the table of contents temporarily for later output.
 *
 * @since 2016/07/29
 */
final class __Contents__
{
	/** The content types. */
	protected final List<BlobContentType> types =
		new ArrayList<>();
	
	/** The content names. */
	protected final List<String> names =
		new ArrayList<>();
	
	/** The content position. */
	protected final List<Long> positions =
		new ArrayList<>();
	
	/**
	 * Adds a single entry to the table of contents.
	 *
	 * @param __sp The start position.
	 * @param __ep The end position.
	 * @param __ct The type of content this is.
	 * @param __cn The name of the content entry.
	 * @throws JITException If the start and/or end position exceed 2GiB, or
	 * the end is before the start.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/29
	 */
	void __add(long __sp, long __ep, BlobContentType __ct, String __cn)
		throws JITException, NullPointerException
	{
		// Check
		if (__ct == null || __cn == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BA0i Either the end position is before the start
		// position; the start position is negative; or the positions exceed
		// 2GiB. (The start position; The end position)}
		if (__ep < __sp || __sp > Integer.MAX_VALUE || __sp < 0)
			throw new JITException(String.format("BA0i %d %d", __sp, __ep));
		
		// Add
		this.types.add(__ct);
		this.names.add(__cn);
		this.positions.add((__sp << 32L) | (__ep));
	}
	
	/**
	 * Returns the number of contents that exist.
	 *
	 * @return The content count.
	 * @since 2016/07/29
	 */
	public int size()
	{
		return this.types.size();
	}
}

