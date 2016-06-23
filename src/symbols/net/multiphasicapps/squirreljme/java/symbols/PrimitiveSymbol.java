// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.symbols;

/**
 * This represents the primitive type that a field symbol may be.
 *
 * @since 2016/03/19
 */
public enum PrimitiveSymbol
	implements FieldBaseTypeSymbol
{
	/** Byte. */
	BYTE('B', Byte.TYPE, Byte.class),
	
	/** Character. */
	CHARACTER('C', Character.TYPE, Character.class),
	
	/** Double. */
	DOUBLE('D', Double.TYPE, Double.class),
	
	/** Float. */
	FLOAT('F', Float.TYPE, Float.class),
	
	/** Integer. */
	INTEGER('I', Integer.TYPE, Integer.class),
	
	/** Long. */
	LONG('J', Long.TYPE, Long.class),
	
	/** Short. */
	SHORT('S', Short.TYPE, Short.class),
	
	/** Boolean. */
	BOOLEAN('Z', Boolean.TYPE, Boolean.class),
	
	/** End. */
	;
	
	/** The code for the primitive type. */
	public final char code;
	
	/** The native class type. */
	public final Class<?> nativeclass;
	
	/** The boxed class type. */
	public final Class<?> boxedclass;
	
	/**
	 * Initializes the primitive type.
	 *
	 * @param __c The primitive type code.
	 * @param __ncl The native class type.
	 * @param __bcl The boxed class type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/19
	 */
	private PrimitiveSymbol(char __c, Class<?> __ncl, Class<?> __bcl)
		throws NullPointerException
	{
		// Check
		if (__ncl == null || __bcl == null)
			throw new NullPointerException("NARG");
		
		// Set
		code = __c;
		nativeclass = __ncl;
		boxedclass = __bcl;
	}
	
	/**
	 * Finds the primitive type which matches this code.
	 *
	 * @param __c The code to find.
	 * @return The discovered code or {@code null} if not found.
	 * @since 2016/03/19
	 */
	public static PrimitiveSymbol byCode(char __c)
	{
		// Find matching code
		for (PrimitiveSymbol v : values())
			if (v.code == __c)
				return v;
		
		// Not found
		return null;
	}
}

