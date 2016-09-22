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
import net.multiphasicapps.squirreljme.nativecode.base.NativeEndianess;
import net.multiphasicapps.squirreljme.nativecode.base.NativeFloatType;
import net.multiphasicapps.squirreljme.nativecode.base.NativeTarget;

/**
 * This class provides a representation of triplets so that they may be
 * encoded and decoded.
 *
 * @since 2016/07/05
 */
public final class JITTriplet
	implements Comparable<JITTriplet>
{
	/** The native CPU target. */
	protected final NativeTarget nativetarget;
	
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
	
	/** The architecture property. */
	private volatile Reference<String> _archprop;
	
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
		
		// Decode architecture
		this.nativetarget = new NativeTarget(__t.substring(0, dota));
		
		// Store operating system and its variant
		this.os = __check(__t.substring(dota + 1, dotb));
		this.osvar = __check(__t.substring(dotb + 1));
	}
	
	/**
	 * Returns the string which represents the architecture for usage in the
	 * {@code os.arch} system property.
	 *
	 * @return The property value.
	 * @since 2016/07/24
	 */
	public final String architectureProperty()
	{
		Reference<String> ref = _archprop;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			NativeTarget nt = this.nativetarget;
			_archprop = new WeakReference<>((rv = nt.architecture() + "-" +
				nt.bits() + "+" + nt.architectureVariant() + "," +
				nt.endianess() + "~" + nt.floatingPoint()));
		}
		
		// Return
		return rv;
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
		
		// Compare architecture last
		return this.nativetarget.compareTo(__b.nativetarget);
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
			
			return this.nativetarget.equals(o.nativetarget) &&
				this.os.equals(o.os) &&
				this.osvar.equals(o.osvar);
		}
		
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
	 * Returns the native target of the triplet.
	 *
	 * @return The native target information.
	 * @since 2016/09/22
	 */
	public final NativeTarget nativeTarget()
	{
		return this.nativetarget;
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
		{
			NativeTarget nt = this.nativetarget;
			_package = new WeakReference<>((rv = nt.architecture() + "-" +
				nt.bits() + "," + nt.endianess() + "." +
				this.os + "." + this.osvar));
		}
		
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
			_string = new WeakReference<>((rv = this.nativetarget + "." +
				this.os + "." + this.osvar));
		
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
		return NativeTarget.normalizeAndCheckFragmentString(__s);
	}
}

