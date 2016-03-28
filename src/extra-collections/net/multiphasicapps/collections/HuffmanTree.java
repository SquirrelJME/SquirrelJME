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
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 * This represents a mutable huffman tree.
 * 
 * This class is thread safe.
 *
 * Iteration of values goes through the internal value table in no particular
 * order. The iterator is fail-fast.
 *
 * @param <T> The type of values to store in the tree.
 * @since 2016/03/10
 */
public class HuffmanTree<T>
	implements Iterable<T>
{
	/** Lock. */
	protected final Object lock =
		new Object();	
	
	/** The huffman table. */
	private volatile int[] _table;
	
	/** Stored tree values. */
	private volatile Object[] _values;
	
	/** Modification count. */
	private volatile int _modcount;
	
	/**
	 * Initializes a basic blank huffman tree.
	 *
	 * @since 2016/03/10
	 */
	public HuffmanTree()
	{
		// Initially add table space so that it is always initially valid but
		// points to nothing.
		__addTableSpace();
	}
	
	/**
	 * Adds the specified object which is associated with the given symbol
	 * and mask.
	 *
	 * @param __v The value to add.
	 * @param __sym The bit representation of the symbol.
	 * @param __mask The mask of the symbol for its valid bits.
	 * @return The old value, or {@code null} if it is not set.
	 * @throws IllegalArgumentException If the specified symbol contains a bit
	 * which is outside of the mask or the mask does not start at shift zero
	 * or has zero gaps.
	 * @since 2016/03/28
	 */
	public T add(T __v, int __sym, int __mask)
		throws IllegalArgumentException
	{
		// Number of bits in the mask
		int ibm = Integer.bitCount(__mask);
		
		// Check mask and representation
		if ((__sym & (~__mask)) != 0)
			throw new IllegalArgumentException(String.format("XC01 %x %x",
				__sym, __mask));
		if (ibm != (32 - Integer.numberOfLeadingZeros(__mask)) ||
			(__mask & 1) == 0)
			throw new IllegalArgumentException(String.format("XC02 %x %x",
				__sym, __mask));
		
		// Lock
		synchronized (lock)
		{
			// Get the table
			int[] table = _table;
			int n = table.length;
			
			// Find the spot to add it based on the bit depth
			int at = 0;
			for (int b = 0; b < ibm; b++)
			{
				// Last bit set?
				boolean last = (b == (ibm - 1));
				
				// The array index to look at for the current position depends
				// on which bit is set
				int q = (__sym >>> b) & 1;
				
				// Get the jump value
				int jump = table[at + q];
				
				// If this points to a constant area but this is not the last
				// bit, then trash it.
				if (!last && jump < 0)
				{
					jump = Integer.MAX_VALUE;
					table[at + q] = jump;
				}
				
				// Jumps off the table end? Needs more values to be added for
				// the tree to be built
				if (jump == Integer.MAX_VALUE)
				{
					// If this is the last entry then a value index needs to
					// be created to store the value
					if (last)
					{
						// Add space for a new variable
						int vat = __addValueSpace();
						
						// Place value there
						_values[vat] = __v;
						
						// Set table index to point there
						table[at + 1] = -(vat + 1);
						
						// Modified
						_modcount++;
						
						// No old value exists
						return null;
					}
					
					// Otherwise, add some table space and jump to that
					// instead on the next run.
					else
					{
						// Add new location info
						int jat = __addTableSpace();
					
						// Correct vars
						table = _table;
						n = table.length;
					
						// Set jump to that position
						// Use that position instead on the next read
						table[at + 1] = at = jat;
					}
				}
				
				// Points to a constant area, return a value
				else if (jump < 0)
				{
					// Calculate actual placement
					int vat = (-jump) - 1;
					
					// Get old value
					Object[] vals = _values;
					Object old = vals[vat];
					
					// Set new value
					vals[vat] = __v;
					
					// Modified
					_modcount++;
					
					// Return the old value
					return __cast(old);
				}
				
				// Points to another location in the array
				else
					at = jump;
			}
			
			// Should not occur
			throw new RuntimeException("WTFX");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/10
	 */
	@Override
	public Iterator<T> iterator()
	{
		return new Iterator<T>()
			{
				/** The modification base. */
				protected final int basemod = 
					_modcount;
				
				/** The current index. */
				private volatile int _dx;
				
				/**
				 * {@inheritDoc}
				 * @since 2016/03/28
				 */
				@Override
				public boolean hasNext()
				{
					// Lock
					synchronized (lock)
					{
						// Modified too much?
						if (_modcount != basemod)
							throw new ConcurrentModificationException("XC04");
						
						// Before the end?
						Object[] vals = _values;
						return (vals == null ? false : _dx < vals.length);
					}
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2016/03/28
				 */
				@Override
				public T next()
				{
					// Lock
					synchronized (lock)
					{
						// Modified too much?
						if (_modcount != basemod)
							throw new ConcurrentModificationException("XC04");
						
						// The curent index
						int dx = _dx;
							
						// At the end?
						Object[] vals = _values;
						if (vals == null || dx >= vals.length)
							throw new NoSuchElementException("NSEE");
						
						// Get it
						Object rv = vals[dx];
						
						// Set next index
						_dx = dx + 1;
						
						// Return it
						return __cast(rv);
					}
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2016/03/28
				 */
				@Override
				public void remove()
				{
					throw new UnsupportedOperationException("RORO");
				}
			};
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/10
	 */
	@Override
	public String toString()
	{
		// Lock
		synchronized (lock)
		{
			// Setup
			StringBuilder sb = new StringBuilder("[");
			
			// Add elements in no particular order
			Object[] vals = _values;
			if (vals != null)
			{
				int n = vals.length;
				for (int i = 0; i < n; i++)
				{
					// Comma?
					if (i > 0)
						sb.append(", ");
					
					// Add the value
					sb.append(vals[i]);
				}
			}
			
			// Build it
			sb.append(']');
			return sb.toString();
		}
	}
	
	/**
	 * Returns a traverser over the given values in the tree.
	 *
	 * The traverser is fail fast.
	 *
	 * @return The traverser over the tree.
	 * @since 2016/03/28
	 */
	public Traverser<T> traverser()
	{
		return new Traverser<T>()
			{
				/** The modification base. */
				protected final int basemod = 
					_modcount;
				
				/** The current location index. */
				private volatile int _at;
				
				/**
				 * {@inheritDoc}
				 * @since 2016/03/28
				 */
				@Override
				public T getValue()
					throws NoSuchElementException
				{
					// Lock
					synchronized (lock)
					{
						// Modified too much?
						if (_modcount != basemod)
							throw new ConcurrentModificationException("XC04");
						
						// Get the jump table
						int[] table = _table;
						
						// Missing table?
						if (table == null)
							throw new NoSuchElementException("NSEE");
						
						// Read the jump index here
						int jump = table[_at];
						
						// Not a value associated jump?
						if (jump >= 0)
							throw new NoSuchElementException("NSEE");
						
						// Return value here
						return __cast(_values[(-jump) - 1]);
					}
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2016/03/28
				 */
				@Override
				public boolean hasValue()
				{
					// Try to get a value
					try
					{
						Object v = getValue();
						
						// If this point is reached then the value is valid
						return true;
					}
					
					// Indicative of no value.
					catch (NoSuchElementException e)
					{
						return false;
					}
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2016/03/28
				 */
				@Override
				public Traverser<T> traverse(int __side)
					throws IllegalArgumentException, NoSuchElementException
				{
					// Check
					if (__side < 0 || __side > 1)
						throw new IllegalArgumentException(String.format(
							"XC03 %d", __side));
					
					// Lock
					synchronized (lock)
					{
						// Modified too much?
						if (_modcount != basemod)
							throw new ConcurrentModificationException("XC04");
						
						// Get the jump table
						int[] table = _table;
						
						// Missing table? Fail
						if (table == null)
							throw new NoSuchElementException("NSEE");
						
						// Get the at index
						int at = _at;
						
						// Get the jump value
						int jump = table[at + __side];
						
						// A value or the end of the tree? Fail
						if (jump < 0 || jump == Integer.MAX_VALUE)
							throw new NoSuchElementException("NSEE");
						
						// Set the new position to this position
						_at = jump;
					}
					
					// Self
					return this;
				}
			};
	}
	
	/**
	 * Adds more table space for a branch.
	 *
	 * @return The base index of the newly added space.
	 * @since 2016/03/28
	 */
	private int __addTableSpace()
	{
		// Lock
		synchronized (lock)
		{
			// The returned value is the end of the table
			int[] table = _table;
			int rv = (table == null ? 0 : table.length);
			
			// Allocate some extra space
			int[] becomes = new int[rv + 2];
			
			// Copy the old array over
			for (int i = 0; i < rv; i++)
				becomes[i] = table[i];
			
			// The end bits become invalidated
			becomes[rv] = Integer.MAX_VALUE;
			becomes[rv + 1] = Integer.MAX_VALUE;
			
			// Set new table
			_table = becomes;
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Adds more value space to add a new value.
	 *
	 * @return The index where the value space was increased.
	 * @since 2016/03/28
	 */
	private int __addValueSpace()
	{
		// Lock
		synchronized (lock)
		{
			// The returned value is the end of the table
			Object[] values = _values;
			int rv = (values == null ? 0 : values.length);
			
			// Allocate some extra space
			Object[] becomes = new Object[rv + 1];
			
			// Copy the old array over
			for (int i = 0; i < rv; i++)
				becomes[i] = values[i];
			
			// Set new table
			_values = becomes;
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Fake casts the value to prevent warnings elsewhere.
	 *
	 * @param __v The value to cast.
	 * @return The value casted to a specific type.
	 * @since 2016/03/28
	 */
	@SuppressWarnings({"unchecked"})
	private T __cast(Object __v)
	{
		return (T)__v;
	}
	
	/**
	 * This is a traverser for the huffman table.
	 *
	 * @param <T> The type of value to return.
	 * @since 2016/03/28
	 */
	public static interface Traverser<T>
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
		public abstract Traverser<T> traverse(int __side)
			throws IllegalArgumentException, NoSuchElementException;
	}
}

