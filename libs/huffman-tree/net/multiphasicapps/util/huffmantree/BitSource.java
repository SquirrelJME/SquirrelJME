// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.huffmantree;

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
	 * @since 2016/08/16
	 */
	public abstract boolean nextBit();
}

