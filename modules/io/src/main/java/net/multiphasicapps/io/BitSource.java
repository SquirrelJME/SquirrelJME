// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.IOException;

/**
 * This is used for huffman tree traversal.
 *
 * @since 2016/08/16
 */
public interface BitSource
{
	/**
	 * Returns the next bit.
	 *
	 * @return The next bit.
	 * @throws IOException On read errors.
	 * @since 2016/08/16
	 */
	public abstract boolean nextBit()
		throws IOException;
}

