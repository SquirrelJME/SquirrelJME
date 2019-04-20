// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

import net.multiphasicapps.classfile.JavaType;

/**
 * This represents a Java type as it would appear on the stack.
 *
 * @since 2019/04/07
 */
public enum StackJavaType
{
	/** Integer. */
	INTEGER,
	
	/** Float. */
	FLOAT,
	
	/** Long. */
	LONG,
	
	/** Double. */
	DOUBLE,
	
	/** End. */
	;
	
	/**
	 * Returns this as a basic Java type.
	 *
	 * @return The Java type of this.
	 * @since 2019/04/16
	 */
	public final JavaType toJavaType()
	{
		switch (this)
		{
			case INTEGER:	return JavaType.INTEGER;
			case FLOAT:		return JavaType.FLOAT;
			case LONG:		return JavaType.LONG;
			case DOUBLE:	return JavaType.DOUBLE;
		}
		
		throw new todo.OOPS(this.name());
	}
	
	/**
	 * Returns the stack type for the given index.
	 *
	 * @param __i The index.
	 * @return The resulting stack type.
	 * @since 2019/04/08
	 */
	public static final StackJavaType of(int __i)
	{
		switch (__i)
		{
			case 0:		return INTEGER;
			case 1:		return LONG;
			case 2:		return FLOAT;
			case 3:		return DOUBLE;
		}
		
		// {@squirreljme.error JC3c Invalid stack Java type.}
		throw new IllegalArgumentException("JC3c");
	}
}

