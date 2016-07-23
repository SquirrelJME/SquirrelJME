// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This class provides a representation of triplets so that they may be
 * encoded and decoded.
 *
 * @since 2016/07/05
 */
public final class JITTriplet
	implements Comparable<JITTriplet>
{
	/** The architecture. */
	protected final String architecture;
	
	/** The number of bits used. */
	protected final int bits;
	
	/** The variant of the CPU. */
	protected final String cpuvar;
	
	/** The endianess of the CPU. */
	protected final JITCPUEndian endianess;
	
	/** The operating system. */
	protected final String os;
	
	/** The variant of the operating system. */
	protected final String osvar;
	
	/** The string representation. */
	private volatile Reference<String> _string;
	
	/** The used hashcode. */
	private volatile int _hashcode;
	
	/** The package target. */
	private volatile Reference<String> _package;
	
	/**
	 * This decodes the given input string as a triplet.
	 *
	 * @param __t The triplet to decode.
	 * @throws IllegalArgumentException If the triplet is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/05
	 */
	public JITTriplet(String __t)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BQ0a Expected two periods in the triplet.
		// (The triplet)}
		int dota = __t.indexOf('.');
		if (dota < 0)
			throw new IllegalArgumentException(String.format("BQ0a %s", __t));
		int dotb = __t.indexOf('.', dota + 1);
		if (dotb < 0)
			throw new IllegalArgumentException(String.format("BQ0a %s", __t));
		
		// Split into three forms
		String fullarch = __t.substring(0, dota);
		
		// Store operating system and its variant
		this.os = __check(__t.substring(dota + 1, dotb));
		this.osvar = __check(__t.substring(dotb + 1));
		
		// Find all symbol locations in the architecture
		int pdas = fullarch.indexOf('-'),
			pplu = fullarch.indexOf('+'),
			pcom = fullarch.indexOf(',');
		
		// {@squirreljme.error BQ0b Expected the architecture part to be in
		// the form of {@code name-bits+variant,endianess}. (The input
		// triplet)}
		if (pdas < 0 || pplu < 0 || pcom < 0 || pdas > pplu || pdas > pcom ||
			pplu > pcom)
			throw new IllegalArgumentException(String.format("BQ0b %s", __t));
		
		// Extract
		this.architecture = __check(fullarch.substring(0, pdas));
		this.cpuvar = __check(fullarch.substring(pplu + 1, pcom));
		this.endianess = JITCPUEndian.of(__check(
			fullarch.substring(pcom + 1)));
		
		// Decode bits
		try
		{
			// {@squirreljme.error BQ0c The specified word size in bits that
			// the CPU uses is zero or negative. (The triplet; The bit count)}
			int bits = Integer.decode(fullarch.substring(pdas + 1, pplu));
			if (bits <= 0)
				throw new IllegalArgumentException(String.format("BQ0c %s %d",
					__t, bits));
			
			// Ok
			this.bits = bits;
		}
		
		// {@squirreljme.error BQ0d The word size of the CPU in bits is not
		// a valid number. (The triplet)}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(String.format("BQ0d %s", __t),
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
	 * @since 2016/07/05
	 */
	@Override
	public final int compareTo(JITTriplet __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// The operating system name
		int rv = this.os.compareTo(__b.os);
		if (rv != 0)
			return rv;
		
		// The variant of the operating system
		rv = this.osvar.compareTo(__b.osvar);
		if (rv != 0)
			return rv;
		
		// The architecture
		rv = this.architecture.compareTo(__b.architecture);
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
	public final JITCPUEndian endianess()
	{
		return this.endianess;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/05
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Check
		if (__o instanceof JITTriplet)
		{
			JITTriplet o = (JITTriplet)__o;
			
			return this.architecture.equals(o.architecture) &&
				this.bits == o.bits &&
				this.cpuvar.equals(o.cpuvar) &&
				this.endianess.equals(o.endianess) &&
				this.os.equals(o.os) &&
				this.osvar.equals(o.osvar);
		}
		
		// String
		else if (__o instanceof String)
			return toString().equals(((String)__o));
		
		// Unknown
		else
			return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/05
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
	 * Returns the operating system.
	 *
	 * @return The operating system.
	 * @since 2016/07/05
	 */
	public final String operatingSystem()
	{
		return this.os;
	}
	
	/**
	 * Returns the operating system variant.
	 *
	 * @return The operating system variant.
	 * @since 2016/07/05
	 */
	public final String operatingSystemVariant()
	{
		return this.osvar;
	}
	
	/**
	 * Returns the package based target name for a given operating system
	 * which lacks the variant due to potentially variability. Thus the
	 * resulting form is that of {@code arch-bits,endian.os.variant}.
	 *
	 * @return The package variant of the target.
	 * @since 2016/07/05
	 */
	public final String toPackageTarget()
	{
		Reference<String> ref = _package;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			_package = new WeakReference<>((rv = this.architecture + "-" +
				this.bits + "," + this.endianess.endianName() + "." +
				this.os + "." + this.osvar));
		
		// Return
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/05
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
				this.endianess.endianName() + "." + this.os + "." +
				this.osvar));
		
		// Return
		return rv;
	}
	
	/**
	 * Checks that triplet fragments are well formed.
	 *
	 * @param __s The string to check.
	 * @return {@code __s}, properly converted.
	 * @throws IllegalArgumentException If it is not well formed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/05
	 */
	private static final String __check(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BQ0e A fragment in a triplet cannot be empty.}
		int n = __s.length();
		if (n <= 0)
			throw new IllegalArgumentException("BQ0e");
		
		// Check all characters
		boolean upper = false;
		for (int i = 0; i < n; i++)
		{
			char c = __s.charAt(i);
			
			// {@squirreljme.error BQ0f The fragment in the input triplet
			// contains an illegal character. (The fragment; The illegal
			// character)}
			boolean hasupper = (c >= 'A' && c <= 'Z');
			if (!((c >= 'a' && c <= 'z') || hasupper ||
				(c >= '0' && c <= '9')))
				throw new IllegalArgumentException(String.format("BQ0f %s %c",
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

