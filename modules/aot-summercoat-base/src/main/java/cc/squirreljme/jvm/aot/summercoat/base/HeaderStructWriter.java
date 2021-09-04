// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat.base;

import cc.squirreljme.jvm.summercoat.ld.pack.HeaderStruct;
import net.multiphasicapps.io.ChunkForwardedFuture;

/**
 * Writer for standard {@link HeaderStruct}.
 *
 * @since 2021/09/03
 */
public final class HeaderStructWriter
{
	/** Properties to write. */
	private final ChunkForwardedFuture[] _properties;
	
	/**
	 * Initializes the header struct writer.
	 * 
	 * @param __numProperties The number of properties to store.
	 * @throws IllegalArgumentException If the number of properties is
	 * negative.
	 * @since 2021/09/03
	 */
	public HeaderStructWriter(int __numProperties)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AJ02 Invalid number of pack properties.}
		if (__numProperties <= 0)
			throw new IllegalArgumentException("AJ02 " + __numProperties);
		
		this._properties = new ChunkForwardedFuture[__numProperties];
	}
}
