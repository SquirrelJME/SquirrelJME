// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This indicates the type that a constant value is.
 *
 * @since 2018/05/16
 */
public enum ConstantValueType
{
	/** Integer. */
	INTEGER,
	
	/** Long. */
	LONG,
	
	/** Float. */
	FLOAT,
	
	/** Double. */
	DOUBLE,
	
	/** String. */
	STRING,
	
	/** Class. */
	CLASS,
	
	/** End. */
	;
	
	/**
	 * Checks if this value is compatible with the given field descriptor.
	 *
	 * @param __d The descriptor to check.
	 * @return If it is compatible.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/21
	 */
	public final boolean isCompatibleWith(FieldDescriptor __d)
		throws NullPointerException
	{
		if (__d == null)
			throw new NullPointerException("NARG");
		
		switch (this)
		{
			case INTEGER:
				if (!__d.isPrimitive())
					return false;
				switch (__d.primitiveType())
				{
					case BOOLEAN:
					case BYTE:
					case SHORT:
					case CHARACTER:
					case INTEGER:
						return true;
						
					default:
						return false;
				}
			
			case LONG:
				return __d.isPrimitive() &&
					__d.primitiveType().equals(PrimitiveType.LONG);
			
			case FLOAT:
				return __d.isPrimitive() &&
					__d.primitiveType().equals(PrimitiveType.FLOAT);
			
			case DOUBLE:
				return __d.isPrimitive() &&
					__d.primitiveType().equals(PrimitiveType.DOUBLE);
			
			case STRING:
				return __d.isObject() &&
					"Ljava/lang/String;".equals(__d.toString()); 
			
			case CLASS:
				return __d.isObject() &&
					"Ljava/lang/Class;".equals(__d.toString());
			
			default:
				throw new todo.OOPS();
		}
	}
	
	/**
	 * Is this a narrow constant value?
	 *
	 * @return If this is a narrow constant value.
	 * @since 2108/09/08
	 */
	public final boolean isNarrow()
	{
		switch (this)
		{
			case INTEGER:
			case FLOAT:
			case STRING:
			case CLASS:
				return true;
			
			default:
				return false;
		}
	}
	
	/**
	 * Is this a wide constant value?
	 *
	 * @return If this is a wide constant value.
	 * @since 2108/09/08
	 */
	public final boolean isWide()
	{
		switch (this)
		{
			case LONG:
			case DOUBLE:
				return true;
			
			default:
				return false;
		}
	}
	
	/**
	 * Returns the Java type for the constant value.
	 *
	 * @return The Java type that is used.
	 * @since 2019/03/17
	 */
	public final JavaType javaType()
	{
		switch (this)
		{
			case INTEGER:	return JavaType.INTEGER;
			case LONG:		return JavaType.LONG;
			case FLOAT:		return JavaType.FLOAT;
			case DOUBLE:	return JavaType.DOUBLE;
			case STRING:	return new JavaType(new FieldDescriptor(
				"Ljava/lang/String;"));
			case CLASS:		return new JavaType(new FieldDescriptor(
				"Ljava/lang/Class;"));
			
			default:
				throw new todo.OOPS();
		}
	}
}

