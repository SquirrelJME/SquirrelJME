// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Map;
import java.util.Set;

/**
 * This represents a mutable huffman tree.
 *
 * Note that very large bit numbers will use more memory.
 *
 * @param <T> The type of values to store in the tree.
 * @since 2016/03/10
 */
public class HuffmanTree<T>
	extends AbstractMap<Integer, T>
{
	/**
	 * This is a special object that is represented in the array which
	 * indicates that this is not a leaf node.
	 */
	protected static final Object DEFER =
		new Object();
	
	/**
	 * This is a special object which is used to indicate that the given
	 * position in the tree is not filled.
	 */
	protected static final Object NOT_FILLED =
		new Object();
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The huffman value tree. */
	private volatile Object[] _values;
	
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
	 * @param __lit The literal value the representation encodes to.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the representation mask has an unset
	 * gap in its bits or a bit is sit in the representation which is not
	 * masked.
	 * @since 2016/03/10
	 */
	public HuffmanTree addLiteralRepresentation(int __rep, int __repmask,
		T __lit)
		throws IllegalArgumentException
	{
		// Number of bits in the mask
		int ibm = Integer.bitCount(__repmask);
		
		// Check mask and representation
		if ((__rep & (~__repmask)) != 0)
			throw new IllegalArgumentException();
		if (ibm != (32 - Integer.numberOfLeadingZeros(__repmask)))
			throw new IllegalArgumentException();
		
		// Lock
		synchronized (lock)
		{
			// Ensure that the internal array tree is capable of storing keys
			// which can take up the given number of bits.
			ensureBits(ibm);
			
			if (true)
				throw new Error("TODO");
		}
		
		// Self
		return this;
	}
	
	/**
	 * This ensures that the internal representation of values can represent
	 * the given number of bits.
	 *
	 * @param __b The number of bits to hold values for.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the number of bits is zero,
	 (negative, or
	 * higher than 32.
	 * @since 2016/03/10
	 */
	public HuffmanTree ensureBits(int __b)
		throws IllegalArgumentException
	{
		// Check
		if (__b <= 0 || __b > 32)
			throw new IllegalArgumentException();
		
		// Lock
		synchronized (lock)
		{
			// Get the old one
			Object[] old = _values;
			
			// If null, just create an array
			
			
			if (true)
				throw new Error("TODO");
		}
		
		// Self
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/10
	 */
	@Override
	public Set<Map.Entry<Integer, T>> entrySet()
	{
		throw new Error("TODO");
	}
}

