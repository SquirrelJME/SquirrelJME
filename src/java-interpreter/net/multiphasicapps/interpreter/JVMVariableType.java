// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf;

/**
 * This represents the type of value which is stored in a variable that is
 * in a local variable is on the stack.
 *
 * @since 2016/03/21
 */
public enum JVMVariableType
{
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
}

