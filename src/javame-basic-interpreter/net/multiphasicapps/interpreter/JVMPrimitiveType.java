// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import net.multiphasicapps.descriptors.ClassNameSymbol;

/**
 * This represents a primitive type.
 *
 * @since 2016/03/31
 */
public enum JVMPrimitiveType
	implements JVMComponentType
{
	/** Boolean. */
	BOOLEAN('Z', ClassNameSymbol.BOOLEAN),
	
	/** Byte. */
	BYTE('B', ClassNameSymbol.BYTE),
	
	/** Short. */
	SHORT('S', ClassNameSymbol.SHORT),
	
	/** Character. */
	CHARACTER('C', ClassNameSymbol.CHARACTER),
	
	/** Integer. */
	INTEGER('I', ClassNameSymbol.INTEGER),
	
	/** Long. */
	LONG('J', ClassNameSymbol.LONG),
	
	/** Float. */
	FLOAT('F', ClassNameSymbol.FLOAT),
	
	/** Double. */
	DOUBLE('D', ClassNameSymbol.DOUBLE),
	
	/** End. */
	;
	
	/** The code used. */
	public final char code;
	
	/** The type which represents an array of this type. */
	public final String arrayform;
	
	/** The class name descriptor for this primitive. */
	public final ClassNameSymbol classname;
	
	/**
	 * Initializes the primitive type.
	 *
	 * @param __c The character which identifies the primitive.
	 * @param __cn The class name symbol which defines the this name for the
	 * primitive.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/31
	 */
	private JVMPrimitiveType(char __c, ClassNameSymbol __cn)
		throws NullPointerException
	{
		// Check
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		code = __c;
		arrayform = "[" + code;
		classname = __cn;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/31
	 */
	@Override
	public ClassNameSymbol thisName()
	{
		return classname;
	}
	
	/**
	 * Returns the primitive type which is associated with the given code.
	 *
	 * @param __code The code of the primitive type.
	 * @return The primitive type associated with this code.
	 * @since 2016/03/31
	 */
	public static JVMPrimitiveType byCode(char __c)
	{
		// Will never be found?
		if (__c < 'B' || __c > 'Z')
			return null;
		
		// Depends
		switch (__c)
		{
			case 'Z': return BOOLEAN;
			case 'B': return BYTE;
			case 'S': return SHORT;
			case 'C': return CHARACTER;
			case 'I': return INTEGER;
			case 'J': return LONG;
			case 'F': return FLOAT;
			case 'D': return DOUBLE;
			
			// Not valid
			default:
				return null;
		}
	}
}

