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

import java.util.NoSuchElementException;

/**
 * This is a traverser for the huffman table.
 *
 * @param <T> The type of value to return.
 * @since 2016/03/28
 */
public interface HuffmanTreeTraverser<T>
{
	/**
	 * Returns the value at this position.
	 *
	 * @return The value at this node.
	 * @throws NoSuchElementException If this is not a value node.
	 * @since 2016/03/28
	 */
	public abstract T getValue()
		throws NoSuchElementException;
	
	/**
	 * Returns {@code true} if a value is stored here.
	 *
	 * @return {@code true} if a value is at this location, this will
	 * return {@code false} if this is not a value node.
	 * @since 2016/03/28
	 */
	public abstract boolean hasValue();
	
	/**
	 * Traverses the given side, if the end of the value chain is reached
	 * and a value is valid then this throws an exception.
	 *
	 * @param __side The side to traverse, must be zero or one.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the side is not zero or one.
	 * @throws NoSuchElementException If an attempt was made to traverse
	 * over a value.
	 * @since 2016/03/28
	 */
	public abstract HuffmanTreeTraverser<T> traverse(int __side)
		throws IllegalArgumentException, NoSuchElementException;
}

