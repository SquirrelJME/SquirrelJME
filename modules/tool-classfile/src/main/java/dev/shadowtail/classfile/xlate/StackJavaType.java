// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.classfile.ClassName;
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
	
	/** Long. */
	LONG,
	
	/** Float. */
	FLOAT,
	
	/** Double. */
	DOUBLE,
	
	/* End. */
	;
	
	/**
	 * Returns the boxed type.
	 *
	 * @return The boxed type.
	 * @since 2019/05/27
	 */
	public final String boxedType()
	{
		switch (this)
		{
			case INTEGER:	return "Integer";
			case LONG:		return "Long";
			case FLOAT:		return "Float";
			case DOUBLE:	return "Double";
		}
		
		throw Debugging.oops();
	}
	
	/**
	 * Returns the signature of this type.
	 *
	 * @return The type signature.
	 * @since 2019/06/21
	 */
	public final String signature()
	{
		switch (this)
		{
			case INTEGER:	return "I";
			case LONG:		return "J";
			case FLOAT:		return "F";
			case DOUBLE:	return "D";
		}
		
		throw Debugging.oops();
	}
	
	/**
	 * Is this a wide type?
	 *
	 * @return If this is wide.
	 * @since 2019/04/20
	 */
	public final boolean isWide()
	{
		return (this == StackJavaType.LONG || this == StackJavaType.DOUBLE);
	}
	
	/**
	 * Returns the software math class.
	 *
	 * @return The software math class.
	 * @since 2019/05/27
	 */
	public final ClassName softwareMathClass()
	{
		String base;
		switch (this)
		{
			case INTEGER:	base = "SoftInteger"; break;
			case LONG:		base = "SoftLong"; break;
			case FLOAT:		base = "SoftFloat"; break;
			case DOUBLE:	base = "SoftDouble"; break;
			
			default:
				throw Debugging.todo();
		}
		
		return new ClassName("cc/squirreljme/jvm/" + base);
	}
	
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
		
		throw Debugging.oops(this.name());
	}
	
	/**
	 * Returns the stack type for the given index.
	 *
	 * @param __i The index.
	 * @return The resulting stack type.
	 * @since 2019/04/08
	 */
	public static StackJavaType of(int __i)
	{
		switch (__i)
		{
			case 0:		return StackJavaType.INTEGER;
			case 1:		return StackJavaType.LONG;
			case 2:		return StackJavaType.FLOAT;
			case 3:		return StackJavaType.DOUBLE;
		}
		
		// {@squirreljme.error JC1s Invalid stack Java type.}
		throw new IllegalArgumentException("JC1s");
	}
}

