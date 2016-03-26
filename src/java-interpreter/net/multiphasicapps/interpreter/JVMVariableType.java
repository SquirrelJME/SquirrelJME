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

import net.multiphasicapps.descriptors.FieldSymbol;

/**
 * This represents the type of value which is stored in a variable that is
 * in a local variable is on the stack.
 *
 * @since 2016/03/21
 */
public enum JVMVariableType
{
	/** Nothing. */
	NOTHING(false),
	
	/** Integer. */
	INTEGER(false),
	
	/** Long. */
	LONG(true),
	
	/** Float. */
	FLOAT(false),
	
	/** Double. */
	DOUBLE(true),
	
	/** Objects. */
	OBJECT(false),
	
	/** The top of a long/double. */
	TOP(false),
	
	/** End. */
	;
	
	/** Is this type wide? */
	protected final boolean wide;
	
	/**
	 * Initializes the variable type.
	 *
	 * @param __w Is this a wide type?
	 * @since 2016/03/21
	 */
	private JVMVariableType(boolean __w)
	{
		wide = __w;
	}
	
	/**
	 * Is this type wide?
	 *
	 * @return {@code true} if it is wide.
	 * @since 2016/03/23
	 */
	public boolean isWide()
	{
		return wide;
	}
	
	/**
	 * Obtains the type of variable to use by its symbol.
	 *
	 * @param __sym The symbol to use for the variable.
	 * @return The variable which is associated with the given symbol.
	 * @throws IllegalArgumentException If the type is not known.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/23
	 */
	public static JVMVariableType bySymbol(FieldSymbol __sym)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__sym == null)
			throw new NullPointerException("NARG");
		
		// If an array then it is always an object
		if (__sym.isArray())
			return OBJECT;
		
		// Depends on the first character otherwise
		switch (__sym.charAt(0))
		{
			case 'L': return OBJECT;
			case 'D': return DOUBLE;
			case 'F': return FLOAT;
			case 'J': return LONG;
				
				// All of these map to integer (promotion)
			case 'B':
			case 'C':
			case 'I':
			case 'S':
			case 'Z':
				return INTEGER;
				
				// Unknown
			default:
				throw new IllegalArgumentException(String.format(
					"IN1k %s", __sym));
		}
	}
}

