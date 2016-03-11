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
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
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
		new Traverse(null);
	
	/**
	 * Initializes a basic blank huffman tree.
	 *
	 * @since 2016/03/10
	 */
	public HuffmanTree()
	{
	}
	
	/**
	 * Returns the root node.
	 *
	 * @return The root node.
	 * @since 2016/03/10
	 */
	public Traverse root()
	{
		return root;
	}
	
	/**
	 * Adds a literal value representation to the tree.
	 *
	 * Traversal through the huffman tree is done from the higher shift values
	 * to the lower shift values.
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
	public HuffmanTree setLiteralRepresentation(int __rep, int __repmask,
		T __lit)
		throws IllegalArgumentException
	{
		// Number of bits in the mask
		int ibm = Integer.bitCount(__repmask);
		
		// Check mask and representation
		if ((__rep & (~__repmask)) != 0)
			throw new IllegalArgumentException();
		if (ibm != (32 - Integer.numberOfLeadingZeros(__repmask)) ||
			(__repmask & 1) == 0)
			throw new IllegalArgumentException();
		
		// Start from higher shifts to lower shifts
		Traverse rover = root;
		for (int i = ibm - 1; i >= 0; i--)
		{
			// Get zero or one
			int code = (__rep >>> i) & 1;
			
			// Last shift? Set the value
			if (i == 0)
				rover.setupLeaf(code).set(__lit);
			
			// Add traverser
			else
				rover = rover.setupTraverse(code);
		}
		
		// Self
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/10
	 */
	@Override
	public String toString()
	{
		return root.toString();
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
		
		/**
		 * Returns {@code true} if this is a leaf.
		 *
		 * @return {@code true} if this is a leaf.
		 * @since 2016/03/10
		 */
		public boolean isLeaf()
		{
			return Leaf.class.isInstance(this);
		}
		
		/**
		 * Returns {@code true} if this is a traverse.
		 *
		 * @return {@code true} if this is a traverse.
		 * @since 2016/03/10
		 */
		public boolean isTraverse()
		{
			return Traverse.class.isInstance(this);
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
		 * @since 2016/03/10
		 */
		protected Leaf(T __v)
		{
			set(__v);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/10
		 */
		@Override
		public boolean equals(Object __o)
		{
			// Not a leaf?
			if (!Leaf.class.isInstance(__o))
				return false;
			
			// Same value?
			return Objects.equals(_value, Leaf.class.cast(__o)._value);
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
		 * {@inheritDoc}
		 * @since 2016/03/10
		 */
		@Override
		public int hashCode()
		{
			return Objects.hashCode(_value);
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
		/** The parent of this traverse. */
		protected final Traverse parent;
		
		/** The zero side of the tree. */
		private volatile Node _zero;
		
		/** The one side of the tree. */
		private volatile Node _one;
		
		/**
		 * Initializes the traversal node.
		 *
		 * @param __p The parent node.
		 * @since 2016/03/10
		 */
		protected Traverse(Traverse __p)
		{
			// Set
			parent = __p;
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
		 * Returns the parent traverse node.
		 *
		 * @return The parent traverse node.
		 * @since 2016/03/10
		 */
		public Traverse getParent()
		{
			return parent;
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
		 * Sets the given side to a traverse, if there is a leaf here already
		 * then it is removed.
		 *
		 * @param __s The side to use as a traverse if it is not one.
		 * @return The traverse of the given side.
		 * @throws IllegalArgumentException If {@code __s} is not zero or
		 * one.
		 * @since 2016/03/10
		 */
		public Traverse setupTraverse(int __s)
			throws IllegalArgumentException
		{
			// Must be zero or one
			if (__s != 0 && __s != 1)
				throw new IllegalArgumentException();
			
			// Get node on this side
			Node n = get(__s);
			
			// If null or not a traverse, create new one
			if (n == null || !n.isTraverse())
			{
				// Create
				n = new Traverse(this);
				
				// Replace
				if (__s == 0)
					_zero = n;
				else
					_one = n;
			}
			
			// Return the traverse
			return (Traverse)n;
		}
		
		/**
		 * Sets the given side to a leaf and returns it, if a traverse on this
		 * side then it is removed.
		 *
		 * @param __s The side to use as aleaf.
		 * @return The leaf of the given side.
		 * @throws IllegalArgumentException If {@code __s} is not zero or
		 * one.
		 * @since 2016/03/10
		 */
		public Leaf setupLeaf(int __s)
			throws IllegalArgumentException
		{
			// Must be zero or one
			if (__s != 0 && __s != 1)
				throw new IllegalArgumentException();
			
			// Get node on this side
			Node n = get(__s);
			
			// If null or not a leaf, create new one
			if (n == null || !n.isLeaf())
			{
				// Create
				n = new Leaf();
				
				// Replace
				if (__s == 0)
					_zero = n;
				else
					_one = n;
			}
			
			// Return the leaf
			return (Leaf)n;
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

