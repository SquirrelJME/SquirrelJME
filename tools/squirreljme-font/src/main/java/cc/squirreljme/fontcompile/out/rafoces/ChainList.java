// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out.rafoces;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * This is a list of chain codes which can be used in a table to repeat
 * duplicated shapes in glyphs.
 *
 * @since 2024/05/27
 */
public final class ChainList
	implements Comparable<ChainList>, Iterable<ChainCode>
{
	/** Chain codes. */
	private final ChainCode[] _codes;
	
	/** The offset into the array. */
	private final int _off;
	
	/** The number of codes used. */
	private final int _len;
	
	/** The hashcode. */
	private volatile int _hash;
	
	/**
	 * Initializes the chain list.
	 *
	 * @param __codes The chain codes.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/27
	 */
	public ChainList(ChainCode... __codes)
		throws NullPointerException
	{
		if (__codes == null)
			throw new NullPointerException("NARG");
		
		// Defensive copy and check
		__codes = __codes.clone();
		for (ChainCode code : __codes)
			if (code == null)
				throw new NullPointerException("NARG");
		
		this._codes = __codes;
		this._off = 0;
		this._len = __codes.length;
	}
	
	/**
	 * Non-copying array range initialization.
	 *
	 * @param __codes The codes used.
	 * @param __off The offset into the array.
	 * @param __len The length of the array.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	private ChainList(ChainCode[] __codes, int __off, int __len)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__codes == null)
			throw new NullPointerException("NARG");
		
		if (__off < 0 || __len < 0 || (__off + __len) > __codes.length ||
			(__off + __len) < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		this._codes = __codes;
		this._off = __off;
		this._len = __len;
	}
	
	/**
	 * Initializes the chain list.
	 *
	 * @param __codes The chain codes.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/27
	 */
	public ChainList(List<ChainCode> __codes)
		throws NullPointerException
	{
		if (__codes == null)
			throw new NullPointerException("NARG");
		
		// Copy in
		int n = __codes.size();
		ChainCode[] codes = new ChainCode[n];
		for (int i = 0; i < n; i++)
		{
			ChainCode code = __codes.get(i);
			if (code == null)
				throw new NullPointerException("NARG");
			
			codes[i] = code;
		}
		
		// Set
		this._codes = codes;
		this._off = 0;
		this._len = codes.length;
	}
	
	/**
	 * The number of flat bits needed to represent this chain.
	 *
	 * @return Flattened bits needed to represent chain.
	 * @since 2024/06/03
	 */
	public int bitsFlat()
	{
		return this._len * 2;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/03
	 */
	@Override
	public int compareTo(ChainList __o)
	{
		int an = this.size();
		int bn = __o.size();
		
		// Compare individual entries
		int limit = (an < bn ? an : bn);
		for (int i = 0; i < limit; i++)
		{
			int cmp = this.get(i).compareTo(__o.get(i));
			if (cmp != 0)
				return cmp;
		}
		
		// Longer lengths first
		return bn - an;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/27
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		else if ((!(__o instanceof ChainList)))
			return false;
		else if (this.hashCode() != __o.hashCode())
			return false;
		
		// Different sizes will never be equal
		ChainList o = (ChainList)__o;
		if (this.size() != o.size())
			return false;
		
		// Compare each entry
		for (int i = 0, n = this.size(); i < n; i++)
			if (this.get(i) != o.get(i))
				return false;
		
		// No differences found
		return true;
	}
	
	/**
	 * Returns the code at the given index.
	 *
	 * @param __dx The index.
	 * @return The code at the given index.
	 * @throws IndexOutOfBoundsException If the index is out of range.
	 * @since 2024/06/03
	 */
	public ChainCode get(int __dx)
		throws IndexOutOfBoundsException
	{
		int len = this._len;
		if (__dx < 0 || __dx >= len)
			throw new IndexOutOfBoundsException("IOOB");
		
		return this._codes[this._off + __dx];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/27
	 */
	@Override
	public int hashCode()
	{
		int result = this._hash;
		if (result != 0)
			return result;
		
		// Calculate hash
		ChainCode[] codes = this._codes;
		for (int i = 0, o = this._off, n = this._len; i < n; i++, o++)
			result = (result * 37) + codes[o].hashCode();
		
		// Cache for later
		this._hash = result;
		return result;
	}
	
	/**
	 * Returns the first index where the given chain list appears in this
	 * one.
	 *
	 * @param __what What to look for.
	 * @return The given index or {@code -1} if it is not within.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	public int indexOf(ChainList __what)
		throws NullPointerException
	{
		if (__what == null)
			throw new NullPointerException("NARG");
		
		int len = this._len;
		int whatLen = __what._len;
		
		// Cannot possibly fit within?
		if (whatLen > len)
			return -1;
		
		// If these are the same size, then there is only a single possibility
		// that need actually be checked
		if (whatLen == len)
			return (this.equals(__what) ? 0 : -1);
		
		// Scan
__outer:
		for (int at = 0, end = len - whatLen; at < end; at++)
		{
			for (int sub = 0, fin = whatLen; sub < fin; sub++)
				if (this.get(at + sub) != __what.get(sub))
					continue __outer;
			
			// Found it here!
			return at;
		}
		
		// Nothing within
		return -1;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/27
	 */
	@Override
	public Iterator<ChainCode> iterator()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the number of codes in the list.
	 *
	 * @return The code list size.
	 * @since 2024/06/03
	 */
	public int size()
	{
		return this._len;
	}
	
	/**
	 * Returns a subsequence of this chain list.
	 *
	 * @param __dx The starting index.
	 * @param __len The number of items.
	 * @return The sub-sequence.
	 * @throws IndexOutOfBoundsException If the index and/or length are
	 * not valid.
	 * @since 2024/06/03
	 */
	public ChainList subSequence(int __dx, int __len)
		throws IndexOutOfBoundsException
	{
		int len = this._len;
		if (__dx < 0 || __len < 0 || (__dx + __len) < 0 ||
			(__dx + __len) > len)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Pointless subsequence?
		if (__dx == 0 && __len == len)
			return this;
		
		// Trim accordingly
		return new ChainList(this._codes, this._off + __dx, __len);
	}
	
	/**
	 * Returns the chain list as a compacted byte array.
	 *
	 * @return The chain list as a compacted byte array.
	 * @since 2024/06/07
	 */
	public byte[] toByteArray()
	{
		return HuffBits.toByteArray(this.toHuffBits());
	}
	
	/**
	 * Returns the chain list as a compacted list of huffman bits.
	 *
	 * @return The chain list as a compacted list of huffman bits.
	 * @since 2024/06/07
	 */
	public HuffBits[] toHuffBits()
	{
		int n = this.size();
		HuffBits[] result = new HuffBits[n];
		
		// Fill in
		for (int i = 0; i < n; i++)
			result[i] = HuffBits.of(this.get(i).ordinal(), 2);
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/03
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		ChainCode[] codes = this._codes;
		for (int i = 0, o = this._off, n = this._len; i < n; i++, o++)
			sb.append(codes[o].name().charAt(0));
		
		return sb.toString();
	}
	
	/**
	 * Trims the codes to the given length.
	 *
	 * @param __to The amount to trim to.
	 * @return The resultant trimmed chain list.
	 * @throws IndexOutOfBoundsException If the to count is negative, zero,
	 * or greater than this chain size.
	 * @since 2024/06/03
	 */
	public ChainList trim(int __to)
		throws IndexOutOfBoundsException
	{
		return this.subSequence(0, __to);
	}
}
