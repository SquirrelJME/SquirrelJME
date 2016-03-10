// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

/**
 * This represents a mutable huffman tree.
 *
 * @since 2016/03/10
 */
public class HuffmanTree
{
	/**
	 * Initializes a basic blank huffman tree.
	 *
	 * @since 2016/03/10
	 */
	public HuffmanTree()
	{
	}
	
	/**
	 * Adds a literal value representation to the tree.
	 *
	 * @param __rep The representation of the value.
	 * @param __bit The mask to use in the literal representation.
	 * @param __lit The literal value it encodes to.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the representation mask has an unset
	 * gap in its bits or a bit is sit in the representation which is not
	 * masked.
	 * @since 2016/03/10
	 */
	public HuffmanTree addLiteralRepresentation(int __rep, int __repmask,
		int __lit)
		throws IllegalArgumentException
	{
		// Check mask and representation
		if ((__rep & (~__repmask)) != 0)
			throw new IllegalArgumentException();
		if (Integer.bitCount(__repmask) !=
			(32 - Integer.numberOfLeadingZeros(__repmask)))
			throw new IllegalArgumentException();
		
		if (true)
			throw new Error("TODO");
		
		// Self
		return this;
	}
}

