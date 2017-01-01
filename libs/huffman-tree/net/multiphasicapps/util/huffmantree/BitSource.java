// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
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

