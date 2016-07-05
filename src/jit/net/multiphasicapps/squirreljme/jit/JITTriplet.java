// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

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
		
		// {@squirreljme.error ED03 Expected two periods in the triplet.
		// (The triplet)}
		int dota = __t.indexOf('.');
		if (dota < 0)
			throw new IllegalArgumentException(String.format("ED03 %s", __t));
		int dotb = __t.indexOf('.', dota + 1);
		if (dotb < 0)
			throw new IllegalArgumentException(String.format("ED03 %s", __t));
		
		// Split into three forms
		String fullarch = __t.substring(0, dota);
		
		// Store operating system and its variant
		this.os = __check(__t.substring(dota + 1, dotb));
		this.osvar = __check(__t.substring(dotb + 1));
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/05
	 */
	@Override
	public int compareTo(JITTriplet __b)
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
	 * {@inheritDoc}
	 * @since 2016/07/05
	 */
	@Override
	public boolean equals(Object __o)
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
	public int hashCode()
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
	 * @since 2016/07/05
	 */
	@Override
	public String toString()
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
		
		// {@squirreljme.error ED04 A fragment in a triplet cannot be empty.}
		int n = __s.length();
		if (n <= 0)
			throw new IllegalArgumentException("ED04");
		
		// Check all characters
		boolean upper = false;
		for (int i = 0; i < n; i++)
		{
			char c = __s.charAt(i);
			
			// {@squirreljme.error ED05 The fragment in the input triplet
			// contains an illegal character. (The fragment; The illegal
			// character)}
			boolean hasupper = (c >= 'A' && c <= 'Z');
			if (!((c >= 'a' && c <= 'z') || hasupper ||
				(c >= '0' && c <= '9')))
				throw new IllegalArgumentException(String.format("ED05 %s %c",
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

