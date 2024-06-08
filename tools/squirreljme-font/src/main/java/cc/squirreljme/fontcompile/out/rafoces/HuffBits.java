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
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Huffman bit sequence.
 *
 * @since 2024/06/03
 */
public final class HuffBits
	implements Iterable<Boolean>
{
	/** The mask for the value. */
	private final long mask;
	
	/** The shift, that is number of bits used. */
	private final int shift;
	
	/** The value stored. */
	private final long value;
	
	/**
	 * Initializes the bit set.
	 *
	 * @param __val The value used.
	 * @param __shift The bits to select.
	 * @throws IndexOutOfBoundsException If the shift is not within the range
	 * of a {@code long} value or is zero.
	 * @since 2024/06/03
	 */
	public HuffBits(long __val, int __shift)
		throws IndexOutOfBoundsException
	{
		if (__shift <= 0 || __shift >= 63)
			throw new IndexOutOfBoundsException("IOOB");
		
		long mask = ((1L << __shift) - 1);
		this.value = __val & mask;
		this.mask = mask;
		this.shift = __shift;
	}
	
	/**
	 * Returns the given bit.
	 *
	 * @param __bit The bit to get.
	 * @return If it is set or not.
	 * @throws IndexOutOfBoundsException If the bit is not valid.
	 * @since 2024/06/03
	 */
	public boolean get(int __bit)
		throws IndexOutOfBoundsException
	{
		if (__bit < 0 || __bit >= this.shift)
			throw new IndexOutOfBoundsException("IOOB");
		
		return (this.value & (1L << __bit)) != 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/03
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		else if (!(__o instanceof HuffBits))
			return false;
		
		HuffBits o = (HuffBits)__o;
		return this.value == o.value &&
			this.mask == o.mask &&
			this.shift == o.shift;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/03
	 */
	@Override
	public int hashCode()
	{
		return this.shift ^
			(int)(((this.mask | this.value) >>> 32L) |
				(this.mask | this.value));
	}
	
	/**
	 * Increments the bits by 1.
	 *
	 * @return The bits incremented by one.
	 * @since 2024/06/03
	 */
	public HuffBits increment()
	{
		int shift = this.shift;
		long mask = this.mask;
		long value = this.value;
		
		// Determine next value, if it extends past the mask then shift up
		long next = value + 1;
		if ((next & (~mask)) != 0)
			return HuffBits.of(next, shift + 1);
		return HuffBits.of(next, shift);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/07
	 */
	@Override
	public Iterator<Boolean> iterator()
	{
		return new __HuffBitsIterator__(this);
	}
	
	/**
	 * Returns the number of bits in the sequence.
	 *
	 * @return The bits in the sequence.
	 * @since 2024/06/03
	 */
	public int length()
	{
		return this.shift;
	}
	
	/**
	 * Shifts in a single bit.
	 *
	 * @param __b The bit to shift in.
	 * @return The resultant shifted in bit.
	 * @since 2024/06/03
	 */
	public HuffBits shiftIn(boolean __b)
	{
		int shift = this.shift;
		long value = this.value;
		
		return HuffBits.of((value << 1L) | (__b ? 1L : 0L),
			shift + 1);
	}
	
	/**
	 * Returns the huffman bits as a byte array.
	 *
	 * @return The bits as a byte array.
	 * @since 2024/06/07
	 */
	public byte[] toByteArray()
	{
		return HuffBits.toByteArray(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/03
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		for (int i = this.length() - 1; i >= 0; i--)
			if (this.get(i))
				sb.append('1');
			else
				sb.append('0');
			
		return sb.toString();
	}
	
	/**
	 * Initializes the bit set.
	 *
	 * @param __val The value used.
	 * @param __shift The bits to select.
	 * @return The huffman bits.
	 * @throws IndexOutOfBoundsException If the shift is not within the range
	 * of a {@code long} value or is zero.
	 * @since 2024/06/03
	 */
	public static HuffBits of(long __val, int __shift)
		throws IndexOutOfBoundsException
	{
		return new HuffBits(__val, __shift);
	}
	
	/**
	 * Returns the number of total bits used.
	 *
	 * @param __in The input huffman bits.
	 * @return The bit count.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	public static int length(List<HuffBits> __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Count total
		int total = 0;
		for (int i = 0, n = __in.size(); i < n; i++)
			total += __in.get(i).length();
		
		return total;
	}
	
	/**
	 * Returns the number of total bits used.
	 *
	 * @param __in The input huffman bits.
	 * @return The bit count.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/03
	 */
	public static int length(HuffBits... __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		return HuffBits.length(Arrays.asList(__in));
	}
	
	/**
	 * Translates huffman bits to the given byte array, this outputs in a
	 * packed form.
	 *
	 * @param __in The input bits.
	 * @return The resultant byte array.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/07
	 */
	public static byte[] toByteArray(List<HuffBits> __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// The resultant byte array
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		
		// The current byte and bit to fill
		byte current = 0;
		int shift = 0;
		
		// Fill in bits
		for (HuffBits bits : __in)
			for (boolean bit : bits)
			{
				// Set bit?
				if (bit)
					current |= (byte)(1 << shift);
				
				// Move shift up
				if ((++shift) >= 8)
				{
					// Store into result
					result.write(current);
					
					// Move shift up and reset
					current = 0;
					shift = 0;
				}
			}
		
		// Left over?
		if (shift > 0)
			result.write(current);
		
		return result.toByteArray();
	}
	
	/**
	 * Translates huffman bits to the given byte array, this outputs in a
	 * packed form.
	 *
	 * @param __in The input bits.
	 * @return The resultant byte array.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/07
	 */
	public static byte[] toByteArray(HuffBits... __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		return HuffBits.toByteArray(Arrays.asList(__in));
	}
}
