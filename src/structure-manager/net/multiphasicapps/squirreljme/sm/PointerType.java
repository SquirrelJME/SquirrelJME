// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.sm;

/**
 * This is the Java type that is used to store pointer values.
 *
 * @since 2016/06/08
 */
public enum PointerType
{
	/** 16-bit pointers. */
	SHORT(short.class, 16),
	
	/** 32-bit pointers. */
	INTEGER(int.class, 32),
	
	/** 64-bit pointers. */
	LONG(long.class, 64),
	
	/** Long. */
	;
	
	/** The native type used. */
	protected final Class<?> type;
	
	/** The number of bits in the type. */
	protected final int bits;
	
	/**
	 * Initializes the pointer type information.
	 *
	 * @param __cl The class type of the primitive type.
	 * @param __b The number of used bits.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/07
	 */
	private PointerType(Class<?> __cl, int __b)
		throws NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.type = __cl;
		this.bits = __b;
	}
	
	/**
	 * Returns the number of bits that the type uses.
	 *
	 * @return The bit count.
	 * @since 2016/06/09
	 */
	public final int bits()
	{
		return this.bits;
	}
	
	/**
	 * Returns the number of bytes the pointer value consumes.
	 *
	 * @return The number of bytes the pointer uses.
	 * @since 2016/06/09
	 */
	public final int bytes()
	{
		return this.bits >>> 3;
	}
	
	/**
	 * Returns the mask which specifies which bits of a 64-bit value is valid
	 * for address usage.
	 *
	 * @return The mask for the lower bits which are valid for addresses.
	 * @since 2016/06/09
	 */
	public final long mask()
	{
		return (1L << (long)this.bits) - 1L;
	}
	
	/**
	 * Returns the native class type which is used for the given type.
	 *
	 * @return The native class type.
	 * @since 2016/06/09
	 */
	public final Class<?> type()
	{
		return this.type;
	}
}

