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
 * This class is not thread safe.
 *
 * Iteration of values goes through the internal value table in no particular
 * order. The iterator is fail-fast.
 *
 * {@squirreljme.error AK04 The huffman tree was modified in the middle of
 * iteration.}
 *
 * @param <T> The type of values to store in the tree.
 * @since 2016/03/10
 */
public class HuffmanTree<T>
	implements Iterable<T>
{
	/** The huffman table. */
	private volatile int[] _table;
	
	/** Stored tree values. */
	private volatile Object[] _values;
	
	/** Modification count. */
	private volatile int _modcount;
	
	/** Maximum used bits. */
	private volatile int _maxbits;
	
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
		// {@squirreljme.error AK01 The symbol exceeds the range of the mask.
		// (The value; The mask)}
		if ((__sym & (~__mask)) != 0)
			throw new IllegalArgumentException(String.format("AK01 %x %x",
				__sym, __mask));
		// {@squirreljme.error AK02 The mask has a zero gap between bits or
		// at the least significant end. (The value; The mask)}
		if (ibm != (32 - Integer.numberOfLeadingZeros(__mask)) ||
			(__mask & 1) == 0)
			throw new IllegalArgumentException(String.format("AK02 %x %x",
				__sym, __mask));
		
		// Get the table
		int[] table = _table;
		int n = table.length;
		
		// Increase max bit count
		_maxbits = Math.max(_maxbits, ibm);
		
		// Find the spot to add it based on the bit depth
		int at = 0;
		for (int sh = (1 << (ibm - 1)); sh != 0; sh >>>= 1)
		{
			// Last bit set?
			boolean last = (sh == 1);
			
			// The array index to look at for the current position depends
			// on which bit is set
			int q = (((__sym & sh) != 0) ? 1 : 0);
			
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
					table[at + q] = -(vat + 1);
					
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
					table[at + q] = at = jat;
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
		throw new RuntimeException("OOPS");
	}
	
	/**
	 * Finds the bit representation and the mask which is associated with the
	 * given symbol.
	 *
	 * @param __v The value to find.
	 * @return {@code -1} if nothing was found, otherwise the upper 32-bits is
	 * the bit mask and the lower 32 is the symbol.
	 * @since 2016/03/28
	 */
	public long findSequence(Object __v)
	{
		// Get values
		Object[] vals = _values;
		
		// No values? nothing will ever be found
		if (vals == null)
			return -1L;
		
		// Look through all values
		int n = vals.length;
		for (int i = 0; i < n; i++)
			if (Objects.equals(vals[i], __v))
				return __recursiveMatch(0, 0, 0, -(i + 1));
		
		// Not found
		return -1L;
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
					// Modified too much?
					if (_modcount != basemod)
						throw new ConcurrentModificationException("AK04");
					
					// Before the end?
					Object[] vals = _values;
					return (vals == null ? false : _dx < vals.length);
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2016/03/28
				 */
				@Override
				public T next()
				{
					// Modified too much?
					if (_modcount != basemod)
						throw new ConcurrentModificationException("AK04");
					
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
	 * Returns the maximum number of bits entries use.
	 *
	 * @return The maximum number of used bits.
	 * @since 2016/03/28
	 */
	public int maximumBits()
	{
		return _maxbits;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/10
	 */
	@Override
	public String toString()
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
				
				// Begin sequence data
				sb.append('<');
				
				// Get the sequence of it
				Object v = vals[i];
				long seq = findSequence(v);
				
				// Not found?
				if (seq == -1L)
					sb.append('?');
				
				// Print bit pattern otherwise
				else
				{
					// Get mask and value
					int msk = (int)(seq >>> 32L);
					int val = (int)(seq);
					
					// Start from the highest bit first
					int hib = Integer.bitCount(msk);
					for (int b = hib - 1; b >= 0; b--)
						sb.append(((0 == (val & (1 << b))) ? '0' : '1'));
				}
				
				// End sequence data
				sb.append(">=");
				
				// Add the value
				sb.append(v);
			}
		}
		
		// Build it
		sb.append(']');
		return sb.toString();
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
					// Modified too much?
					if (_modcount != basemod)
						throw new ConcurrentModificationException("AK04");
					
					// Get the jump table
					Object[] vals = _values;
					
					// Missing table?
					if (vals == null)
						throw new NoSuchElementException("NSEE");
					
					// Not reading a value?
					int at = _at;
					if (at >= 0)
						throw new NoSuchElementException("NSEE");
					
					// Return value here
					return __cast(vals[(-at) - 1]);
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
							"AK03 %d", __side));
					
					// Modified too much?
					if (_modcount != basemod)
						throw new ConcurrentModificationException("AK04");
					
					// Get the jump table
					int[] table = _table;
					
					// Missing table? Fail
					if (table == null)
						throw new NoSuchElementException("NSEE");
					
					// Get the at index
					int at = _at;
					
					// A value or the end of the tree? Fail
					if (at < 0 || at == Integer.MAX_VALUE)
						throw new NoSuchElementException("NSEE");
					
					// Get the jump value
					int jump = table[at + __side];
					
					// Set the new position to this position
					_at = jump;
					
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
	
	/**
	 * Adds more value space to add a new value.
	 *
	 * @return The index where the value space was increased.
	 * @since 2016/03/28
	 */
	private int __addValueSpace()
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
	 * Searches the huffman tree for the given raw match value.
	 *
	 * @param __at The index to look at.
	 * @param __huf The huffman index.
	 * @param __mask The mask of the input value.
	 * @param __match The value to match.
	 * @return The bit mask and the value for the given entry or {@code -1L} if
	 * not found.
	 * @since 2016/03/28
	 */
	private long __recursiveMatch(int __at, int __huf, int __mask, int __match)
	{
		// Get tree
		int[] table = _table;
		
		// Get the left and right side jump values
		int jl = table[__at];
		int jr = table[__at + 1];
		
		// Matches left or right side?
		boolean left = (jl == __match);
		if (left || jr == __match)
			return (((long)((__mask << 1) | 1)) << 32L) |
				((long)((__huf << 1) | (left ? 0 : 1)));
		
		// Traverse left side
		long rv;
		if (jl >= 0 && jl != Integer.MAX_VALUE)
		{
			rv = __recursiveMatch(jl, __huf << 1, (__mask << 1) | 1,
				__match);
			if (rv != -1L)
				return rv;
		}
		
		// Traverse right side
		if (jr >= 0 && jr != Integer.MAX_VALUE)
		{
			rv = __recursiveMatch(jr, (__huf << 1) | 1, (__mask << 1) | 1,
				__match);
			if (rv != 1L)
				return rv;
		}
		
		// Not found
		return -1L;
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

