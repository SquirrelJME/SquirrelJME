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

/**
 * This represents the type of value which is stored in a variable that is
 * in a local variable is on the stack.
 *
 * @since 2016/03/21
 */
public enum JVMVariableType
{
	/** Nothing. */
	NULL,
	
	/** Integer. */
	INTEGER,
	
	/** Long. */
	LONG,
	
	/** Float. */
	FLOAT,
	
	/** Double. */
	DOUBLE,
	
	/** Objects. */
	OBJECT,
	
	/** The top of a long/double. */
	TOP,
	
	/** End. */
	;
	
	/**
	 * Initializes the variable type.
	 *
	 * @since 2016/03/21
	 */
	private JVMVariableType()
	{
	}
	
	/**
	 * Returns the type index 
	 *
	 * @param __i The index to get the type of.
	 * @return The type which belongs to this index.
	 * @throws IllegalArgumentException If the type is not known.
	 * @since 2016/03/23
	 */
	public static JVMVariableType byIndex(int __i)
		throws IllegalArgumentException
	{
		// Determine which one to use
		JVMVariableType rv;
		switch (__i)
		{
			case 0: rv = NULL; break;
			case 1: rv = INTEGER; break;
			case 2: rv = LONG; break;
			case 3: rv = FLOAT; break;
			case 4: rv = DOUBLE; break;
			case 5: rv = OBJECT; break;
			case 6: rv = TOP; break;
				
				// Unknown
			default:
				throw new IllegalArgumentException(String.format(
					"IN1k %d", __i));
		}
		
		// Sanity check
		if (rv.ordinal() != __i)
			throw new RuntimeException("WTFX");
		
		// All is fine
		return rv;
	}
}

