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
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This represents a mutable huffman tree.
 * 
 * This class is not thread safe.
 *
 * Iteration of values goes through the internal value table in no particular
 * order. The iterator is fail-fast.
 *
 * {@squirreljme.error BD10 The huffman tree was modified in the middle of
 * iteration.}
 *
 * @param <int> The type of values to store in the tree.
 * @since 2016/03/10
 */
public class HuffmanTreeInt
{
	/** The huffman table. */
	private volatile int[] _table;
	
	/** Stored tree values. */
	private volatile int[] _values;
	
	/** Modification count. */
	private volatile int _modcount;
	
	/** Maximum used bits. */
	private volatile int _maxbits;
	
	/**
	 * Initializes a basic blank huffman tree.
	 *
	 * @since 2016/03/10
	 */
	public HuffmanTreeInt()
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
	public final int add(int __v, int __sym, int __mask)
		throws IllegalArgumentException
	{
		// Number of bits in the mask
		int ibm = Integer.bitCount(__mask);
		
		// Check mask and representation
		// {@squirreljme.error BD11 The symbol exceeds the range of the mask.
		// (The value; The mask)}
		if ((__sym & (~__mask)) != 0)
			throw new IllegalArgumentException(String.format("BD11 %x %x",
				__sym, __mask));
		// {@squirreljme.error BD12 The mask has a zero gap between bits or
		// at the least significant end. (The value; The mask)}
		if (ibm != (32 - Integer.numberOfLeadingZeros(__mask)) ||
			(__mask & 1) == 0)
			throw new IllegalArgumentException(String.format("BD12 %x %x",
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
					return 0;
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
				int[] vals = _values;
				int old = vals[vat];
				
				// Set new value
				vals[vat] = __v;
				
				// Modified
				_modcount++;
				
				// Return the old value
				return old;
			}
			
			// Points to another location in the array
			else
				at = jump;
		}
		
		// Should not occur
		throw new RuntimeException("OOPS");
	}
	
	/**
	 * Clears the huffman tree.
	 *
	 * @since 2017/02/25
	 */
	public void clear()
	{
		// Reset parameters
		this._table = null;
		this._values = null;
		this._modcount = 0;
		this._maxbits = 0;
		
		// Setup initial tree
		__addTableSpace();
	}
	
	/**
	 * Finds the bit sequence associated with the given value.
	 *
	 * @param __i The value to find the sequence for the given bit pattern.
	 * @return A {@code long} where the upper 32-bits is the bit mask while
	 * the lower 32-bits are the symbol.
	 * @throws NoSuchElementException If no sequence was found.
	 * @since 2016/08/24
	 */
	public final long findSequence(int __i)
		throws NoSuchElementException
	{
		// Get values
		int[] vals = this._values;
		
		// No values? nothing will ever be found
		if (vals == null)
			throw new NoSuchElementException("NSEE");
		
		// Look through all values
		int n = vals.length;
		for (int i = 0; i < n; i++)
			if (vals[i] == __i)
				return __recursiveMatch(0, 0, 0, -(i + 1));
		
		// Not found
		throw new NoSuchElementException("NSEE");
	}
	
	/**
	 * Returns the value obtained via the given bit source.
	 *
	 * @param __bs The source for bits.
	 * @return The value.
	 * @thorws IOException On read errors.
	 * @throws NoSuchElementException If no value was found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/16
	 */
	public final int getValue(BitSource __bs)
		throws IOException, NoSuchElementException, NullPointerException
	{
		// Check
		if (__bs == null)
			throw new NullPointerException("NARG");
		
		// Get the jump table
		int[] table = this._table;
		if (table == null)
			throw new NoSuchElementException("NSEE");
		
		// Try to find a value
		for (int at = 0;;)
		{
			// A value has been read?
			if (at < 0)
				return this._values[(-at) - 1];
			
			// {@squirreljme.error BD13 Key not found in tree.}
			else if (at == Integer.MAX_VALUE)
				throw new NoSuchElementException("BD13");
			
			// Set the new position to the table position
			at = table[at + (__bs.nextBit() ? 1 : 0)];
		}
	}
	
	/**
	 * Returns the maximum number of bits entries use.
	 *
	 * @return The maximum number of used bits.
	 * @since 2016/03/28
	 */
	public final int maximumBits()
	{
		return _maxbits;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/10
	 */
	@Override
	public final String toString()
	{
		// Setup
		StringBuilder sb = new StringBuilder("[");
		
		// Add elements in no particular order
		int[] vals = _values;
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
				int v = vals[i];
				int seq = -1;
				
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
		int[] values = _values;
		int rv = (values == null ? 0 : values.length);
		
		// Allocate some extra space
		int[] becomes = new int[rv + 1];
		
		// Copy the old array over
		for (int i = 0; i < rv; i++)
			becomes[i] = values[i];
		
		// Set new table
		_values = becomes;
		
		// Return it
		return rv;
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
}

