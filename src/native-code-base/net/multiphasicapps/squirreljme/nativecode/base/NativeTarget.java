// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This information is used to specify the native target that a code generator
 * will generate code for.
 *
 * This does not contain any ABI or Operating System specific information.
 *
 * @since 2016/09/22
 */
public final class NativeTarget
	implements Comparable<NativeTarget>
{
	/** The architecture. */
	protected final String architecture;
	
	/** The number of bits used. */
	protected final int bits;
	
	/** The variant of the CPU. */
	protected final String cpuvar;
	
	/** The endianess of the CPU. */
	protected final NativeEndianess endianess;
	
	/** The floating point type used. */
	protected final NativeFloatType floating;
	
	/** The string representation. */
	private volatile Reference<String> _string;
	
	/** The used hashcode. */
	private volatile int _hashcode;
	
	/**
	 * Initializes the native target from the given string.
	 *
	 * @param __s The target string to use.
	 * @throws IllegalArgumentException If the string is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/22
	 */
	public NativeTarget(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Find all symbol locations in the architecture
		int pdas = __s.indexOf('-'),
			pplu = __s.indexOf('+'),
			pcom = __s.indexOf(','),
			ptil = __s.indexOf('~');
		
		// {@squirreljme.error BX0b Expected the architecture part to be in
		// the form of {@code name-bits+variant,endianess~float}. (The input
		// target information)}
		if (pdas < 0 || pplu < 0 || pcom < 0 || pdas > pplu || pdas > pcom ||
			pplu > pcom || ptil < 0 || pdas > ptil || pplu > ptil ||
			pcom > ptil)
			throw new IllegalArgumentException(String.format("BX0b %s", __s));
		
		// Extract
		this.architecture = normalizeAndCheckFragmentString(
			__s.substring(0, pdas));
		this.cpuvar = normalizeAndCheckFragmentString(
			__s.substring(pplu + 1, pcom));
		this.endianess = NativeEndianess.of(normalizeAndCheckFragmentString(
			__s.substring(pcom + 1, ptil)));
		this.floating = NativeFloatType.of(normalizeAndCheckFragmentString(
			__s.substring(ptil + 1)));
		
		// Decode bits
		try
		{
			// {@squirreljme.error BX0c The specified word size in bits that
			// the CPU uses is zero or negative. (The target; The bit count)}
			int bits = Integer.decode(__s.substring(pdas + 1, pplu));
			if (bits <= 0)
				throw new IllegalArgumentException(String.format("BX0c %s %d",
					__s, bits));
			
			// Ok
			this.bits = bits;
		}
		
		// {@squirreljme.error BX0d The word size of the CPU in bits is not
		// a valid number. (The target)}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(String.format("BX0d %s", __s),
				e);
		}
	}
	
	/**
	 * Returns the associated architecture.
	 *
	 * @return The architecture to use.
	 * @since 2016/07/05
	 */
	public final String architecture()
	{
		return this.architecture;
	}
	
	/**
	 * Returns the variant of the architecture.
	 *
	 * @return The architecture variant.
	 * @since 2016/07/05
	 */
	public final String architectureVariant()
	{
		return this.cpuvar;
	}
	
	/**
	 * Returns the number of used CPU bits.
	 *
	 * @return The CPU bit count.
	 * @since 2016/07/05
	 */
	public final int bits()
	{
		return this.bits;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/22
	 */
	@Override
	public final int compareTo(NativeTarget __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// The architecture
		int rv = this.architecture.compareTo(__b.architecture);
		if (rv != 0)
			return rv;
		
		// Then the bits
		int ab = this.bits, bb = __b.bits;
		if (ab < bb)
			return -1;
		else if (ab > bb)
			return 1;
		
		// The CPU variant
		rv = this.cpuvar.compareTo(__b.cpuvar);
		if (rv != 0)
			return rv;
		
		// The endianess
		rv = this.endianess.compareTo(__b.endianess);
		if (rv != 0)
			return rv;
		
		// The same
		return 0;
	}
	
	/**
	 * Returns the endianess to target.
	 *
	 * @return The endianess to target.
	 * @since 2016/07/05
	 */
	public final NativeEndianess endianess()
	{
		return this.endianess;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/22
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Check
		if (__o instanceof NativeTarget)
		{
			NativeTarget o = (NativeTarget)__o;
			
			return this.architecture.equals(o.architecture) &&
				this.bits == o.bits &&
				this.cpuvar.equals(o.cpuvar) &&
				this.endianess.equals(o.endianess);
		}
		
		// Unknown
		else
			return false;
	}
	
	/**
	 * Returns the floating point type of the triplet.
	 *
	 * @return The floating point type used.
	 * @since 2016/08/29
	 */
	public final NativeFloatType floatingPoint()
	{
		return this.floating;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/22
	 */
	@Override
	public final int hashCode()
	{
		int rv = this._hashcode;
		
		// Calculate?
		if (rv == 0)
			this._hashcode = (rv = toString().hashCode());
		
		// Return it
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/22
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = _string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			_string = new WeakReference<>((rv = this.architecture + "-" +
				this.bits + "+" + this.cpuvar + "," +
				this.endianess + "~" + this.floating));
		
		// Return
		return rv;
	}
	
	/**
	 * Checks that architecture fragment strings are well formed.
	 *
	 * @param __s The string to check.
	 * @return {@code __s}, properly converted.
	 * @throws Illegal_ArgumentException If it is not well formed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/05
	 */
	public static String normalizeAndCheckFragmentString(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BX0e A fragment cannot be empty.}
		int n = __s.length();
		if (n <= 0)
			throw new IllegalArgumentException("BX0e");
		
		// Check all characters
		boolean upper = false;
		for (int i = 0; i < n; i++)
		{
			char c = __s.charAt(i);
			
			// {@squirreljme.error BX0f The fragment in the input
			// contains an illegal character. (The fragment; The illegal
			// character)}
			boolean hasupper = (c >= 'A' && c <= 'Z');
			if (!((c >= 'a' && c <= 'z') || hasupper ||
				(c >= '0' && c <= '9')))
				throw new IllegalArgumentException(String.format("BX0f %s %c",
					__s, c));
			
			// Uppercase?
			upper |= hasupper;
		}
		
		// No uppercase characters, keep
		if (!upper)
			return __s;
		
		// Make characters lowercase
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++)
		{
			char c = __s.charAt(i);
			
			if (c < 'A' || c > 'Z')
				sb.append(c);
			else
				sb.append('a' + (c - 'A'));
		}
		
		// Build it
		return sb.toString();
	}
}

