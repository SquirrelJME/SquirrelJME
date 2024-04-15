// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This enumeration represents a primitive type.
 *
 * @since 2017/10/16
 */
public enum PrimitiveType
{
	/** Byte. */
	BYTE,
	
	/** Character. */
	CHARACTER,
	
	/** Double. */
	DOUBLE,
	
	/** Float. */
	FLOAT,
	
	/** Integer. */
	INTEGER,
	
	/** Long. */
	LONG,
	
	/** Short. */
	SHORT,
	
	/** Boolean. */
	BOOLEAN,
	
	/* End. */
	;
	
	/**
	 * The number of bytes needed to store this type.
	 *
	 * @return The number of bytes required to store data for this type.
	 * @since 2019/03/11
	 */
	public final int bytes()
	{
		switch (this)
		{
			case BOOLEAN:
			case BYTE:		return 1;
			case SHORT:
			case CHARACTER:	return 2;
			case INTEGER:
			case FLOAT:		return 4;
			case LONG:
			case DOUBLE:	return 8;
			default:
				throw Debugging.oops();
		}
	}
	
	/**
	 * Returns the field descriptor of the given type.
	 * 
	 * @return The field descriptor of the primitive type.
	 * @since 2023/07/03
	 */
	public FieldDescriptor field()
	{
		return this.toClassName().field();
	}
	
	/**
	 * Is this a wide type?
	 *
	 * @return If this is a wide type.
	 * @since 2017/10/16
	 */
	public final boolean isWide()
	{
		return this == PrimitiveType.LONG || this == PrimitiveType.DOUBLE;
	}
	
	/**
	 * Does this type promote to int?
	 *
	 * @return If this type promotes to int.
	 * @since 2017/10/16
	 */
	public final boolean promotesToInt()
	{
		switch (this)
		{
			case BYTE:
			case CHARACTER:
			case SHORT:
			case BOOLEAN:
				return true;	
			
			default:
				return false;
		}
	}
	
	/**
	 * Returns the Java type this uses on the stack.
	 *
	 * @return The Java type this uses on the stack.
	 * @since 2019/04/02
	 */
	public final JavaType stackJavaType()
	{
		switch (this)
		{
			case BOOLEAN:
			case BYTE:
			case SHORT:
			case CHARACTER:
			case INTEGER:	return JavaType.INTEGER;
			case FLOAT:		return JavaType.FLOAT;
			case LONG:		return JavaType.LONG;
			case DOUBLE:	return JavaType.DOUBLE;
			default:
				throw Debugging.oops();
		}
	}
	
	/**
	 * Returns the class name for the given type.
	 *
	 * @return The type's class name.
	 * @since 2019/03/24
	 */
	public final ClassName toClassName()
	{
		switch (this)
		{
			case BOOLEAN:	return new ClassName("boolean");
			case BYTE:		return new ClassName("byte");
			case SHORT:		return new ClassName("short");
			case CHARACTER:	return new ClassName("char");
			case INTEGER:	return new ClassName("int");
			case LONG:		return new ClassName("long");
			case FLOAT:		return new ClassName("float");
			case DOUBLE:	return new ClassName("double");
			
			default:
				throw Debugging.oops(this.toString());
		}
	}
}

