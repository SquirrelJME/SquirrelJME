// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This is the type of value that is stored within virtual registers.
 *
 * @since 2016/08/29
 */
public enum JITVariableType
{
	/** An {@code int}. */
	INTEGER(false),
	
	/** A {@code long}. */
	LONG(false),
	
	/** A {@code float}. */
	FLOAT(true),
	
	/** A {@code double}. */
	DOUBLE(true),
	
	/** An object (reference). */
	OBJECT(false),
	
	/** The top of a long or double. */
	TOP(false),
	
	/** End. */
	;
	
	/** Is this a floating point type? */
	protected final boolean isfloat;
	
	/**
	 * Initializes the JIT variable type.
	 *
	 * @param __if Is this a floating point type?
	 * @since 2016/09/03
	 */
	private JITVariableType(boolean __if)
	{
		this.isfloat = __if;
	}
	
	/**
	 * Is this a floating point type?
	 *
	 * @return {@code true} if this is a floating point type.
	 * @since 2016/09/03
	 */
	public final boolean isFloat()
	{
		return this.isfloat;
	}
}

