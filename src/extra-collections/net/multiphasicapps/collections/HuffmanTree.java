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
import java.util.Objects;
import java.util.Set;

/**
 * This represents a mutable huffman tree.
 * 
 * This class is not thread safe.
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
	
	/** This is the root traversal node. */
	protected final Traverse root =
		new Traverse();
	
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
	 * The representation is traversed from the lower shifts to higher shifts.
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
		
		if (true)
			throw new Error("TODO");
		
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
	
	/**
	 * This represents a single huffman node.
	 *
	 * @since 2016/03/10
	 */
	public abstract class Node
	{
		/**
		 * Initializes the base node.
		 *
		 * @since 2016/03/10
		 */
		private Node()
		{
		}
		
		/**
		 * Returns this node as a leaf.
		 *
		 * @return {@code this}.
		 * @since 2016/03/10
		 */
		public final Leaf asLeaf()
		{
			return (Leaf)this;
		}
		
		/**
		 * Returns this node as a traverse.
		 *
		 * @return {@code this}.
		 * @since 2016/03/10
		 */
		public final Traverse asTraverse()
		{
			return (Traverse)this;
		}
	}
	
	/**
	 * This is a leaf of the tree which contains a value.
	 *
	 * @since 2016/03/10
	 */
	public class Leaf
		extends Node
	{
		/** The value of this leaf. */
		private volatile T _value;
		
		/**
		 * Initializes this leaf.
		 *
		 * @since 2016/03/10
		 */
		protected Leaf()
		{
		}
		
		/**
		 * Initializes this leaf with an initial value.
		 *
		 * @param __v The initial value of the leaf.
		 */
		protected Leaf(T __v)
		{
			set(__v);
		}
		
		/**
		 * Returns the value of this leaf.
		 *
		 * @return The leaf value.
		 * @since 2016/03/10
		 */
		public T get()
		{
			return _value;
		}
		
		/**
		 * Sets the value of this leaf.
		 *
		 * @param __v The value to set.
		 * @return The old value.
		 * @since 2016/03/10
		 */
		public T set(T __v)
		{
			T old = _value;
			_value = __v;
			return old;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/10
		 */
		@Override
		public String toString()
		{
			return Objects.toString(get());
		}
	}
	
	/**
	 * This is a traversal node.
	 *
	 * @since 2016/03/10
	 */
	public class Traverse
		extends Node
	{
		/** The zero side of the tree. */
		private volatile Node _zero;
		
		/** The one side of the tree. */
		private volatile Node _one;
		
		/**
		 * Initializes the traversal node.
		 *
		 * @since 2016/03/10
		 */
		protected Traverse()
		{
		}
		
		/**
		 * Gets the node which is either zero or one.
		 *
		 * @param __n {@code 0} or {@code 1}.
		 * @return That node or {@code null} if it is not set.
		 * @throws IllegalArgumentException If {@code null} is not zero or one.
		 * @since 2016/03/10
		 */
		public Node get(int __n)
			throws IllegalArgumentException
		{
			if (__n == 0)
				return _zero;
			else if (__n == 1)
				return _one;
			throw new IllegalArgumentException();
		}
		
		/**
		 * Returns the one node of the tree.
		 *
		 * @return The one node.
		 * @since 2016/03/10
		 */
		public Node getOne()
		{
			return _one;
		}
		
		/**
		 * Returns the zero node of the tree.
		 *
		 * @return The zero node.
		 * @since 2016/03/10
		 */ 
		public Node getZero()
		{
			return _zero;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/10
		 */
		@Override
		public String toString()
		{
			return "[0=" + Objects.toString(getZero()) + ", 1=" +
				Objects.toString(getOne()) + "]";
		}
	}
}

