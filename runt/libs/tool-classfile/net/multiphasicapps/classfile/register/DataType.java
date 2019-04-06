// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import net.multiphasicapps.classfile.JavaType;
import net.multiphasicapps.classfile.PrimitiveType;

/**
 * Represents the data type to use for read/write.
 *
 * @since 2019/03/24
 */
public enum DataType
{
	/** Pointer. */
	POINTER,
	
	/** Byte. */
	BYTE,
	
	/** Short. */
	SHORT,
	
	/** Integer. */
	INTEGER,
	
	/** Long. */
	LONG,
	
	/** Float. */
	FLOAT,
	
	/** Double. */
	DOUBLE,
	
	/** End. */
	;
	
	/**
	 * Return the operation to use for array operations.
	 *
	 * @param __s Store into array?
	 * @return The operation to use for arrays.
	 * @since 2019/04/02
	 */
	public final int arrayOperation(boolean __s)
	{
		if (__s)
			switch (this)
			{
				case POINTER:
					return RegisterOperationType.ARRAY_STORE_OBJECT;
				
				case BYTE:
					return RegisterOperationType.ARRAY_STORE_X8;
					
				case SHORT:
					return RegisterOperationType.ARRAY_STORE_X16;
					
				case INTEGER:
				case FLOAT:
					return RegisterOperationType.ARRAY_STORE_X32;
				
				case LONG:
				case DOUBLE:
					return RegisterOperationType.ARRAY_STORE_X64;
			}
		else
			switch (this)
			{
				case POINTER:
					return RegisterOperationType.ARRAY_LOAD_OBJECT;
				
				case BYTE:
					return RegisterOperationType.ARRAY_LOAD_X8;
					
				case SHORT:
					return RegisterOperationType.ARRAY_LOAD_X16;
					
				case INTEGER:
				case FLOAT:
					return RegisterOperationType.ARRAY_LOAD_X32;
				
				case LONG:
				case DOUBLE:
					return RegisterOperationType.ARRAY_LOAD_X64;
			}
		
		throw new todo.OOPS();
	}
	
	/**
	 * Returns the operation used for copies.
	 *
	 * @param __nc Is counting needed?
	 * @return The operation to use.
	 * @since 2019/03/27
	 */
	public final int copyOperation(boolean __nc)
	{
		switch (this)
		{
			case POINTER:
				if (__nc)
					return RegisterOperationType.X32_COPY;
				return RegisterOperationType.OBJECT_COPY;
			
			case BYTE:
			case SHORT:
			case INTEGER:
			case FLOAT:
				return RegisterOperationType.X32_COPY;
			
			case LONG:
			case DOUBLE:
				return RegisterOperationType.X64_COPY;
		}
		
		// Should not be reached
		throw new todo.OOPS();
	}
	
	/**
	 * Returns the operation that should be used to operate on the field.
	 *
	 * @param __static Is the field static?
	 * @param __write Is the field written to?
	 * @return The operation to use when accessing a field of this type.
	 * @since 2019/03/27
	 */
	public final int fieldAccessOperation(boolean __static, boolean __write)
	{
		if (__static)
			if (__write)
				switch (this)
				{
					case POINTER:
						return RegisterOperationType.SFIELD_STORE_OBJECT;
					case BYTE:
						return RegisterOperationType.SFIELD_STORE_X8;
					case SHORT:
						return RegisterOperationType.SFIELD_STORE_X16;
					case INTEGER:
					case FLOAT:
						return RegisterOperationType.SFIELD_STORE_X32;
					case LONG:
					case DOUBLE:
						return RegisterOperationType.SFIELD_STORE_X64;
				}
			else
				switch (this)
				{
					case POINTER:
						return RegisterOperationType.SFIELD_LOAD_OBJECT;
					case BYTE:
						return RegisterOperationType.SFIELD_LOAD_X8;
					case SHORT:
						return RegisterOperationType.SFIELD_LOAD_X16;
					case INTEGER:
					case FLOAT:
						return RegisterOperationType.SFIELD_LOAD_X32;
					case LONG:
					case DOUBLE:
						return RegisterOperationType.SFIELD_LOAD_X64;
				}
		else
			if (__write)
				switch (this)
				{
					case POINTER:
						return RegisterOperationType.IFIELD_STORE_OBJECT;
					case BYTE:
						return RegisterOperationType.IFIELD_STORE_X8;
					case SHORT:
						return RegisterOperationType.IFIELD_STORE_X16;
					case INTEGER:
					case FLOAT:
						return RegisterOperationType.IFIELD_STORE_X32;
					case LONG:
					case DOUBLE:
						return RegisterOperationType.IFIELD_STORE_X64;
				}
			else
				switch (this)
				{
					case POINTER:
						return RegisterOperationType.IFIELD_LOAD_OBJECT;
					case BYTE:
						return RegisterOperationType.IFIELD_LOAD_X8;
					case SHORT:
						return RegisterOperationType.IFIELD_LOAD_X16;
					case INTEGER:
					case FLOAT:
						return RegisterOperationType.IFIELD_LOAD_X32;
					case LONG:
					case DOUBLE:
						return RegisterOperationType.IFIELD_LOAD_X64;
				}
		
		// Should not be reached
		throw new todo.OOPS();
	}
	
	/**
	 * Returns the operation used for copies.
	 *
	 * @param __nc Is counting needed?
	 * @param __store Is this a store?
	 * @return The operation to use.
	 * @since 2019/03/27
	 */
	public final int fieldRegisterOperation(boolean __nc, boolean __store)
	{
		if (__store)
			switch (this)
			{
				case POINTER:
					if (__nc)
						return RegisterOperationType.OBJECT_FIELD_STORE;
					return RegisterOperationType.X32_FIELD_STORE;
				
				case BYTE:
				case SHORT:
				case INTEGER:
				case FLOAT:
					return RegisterOperationType.X32_FIELD_STORE;
				
				case LONG:
				case DOUBLE:
					return RegisterOperationType.X64_FIELD_STORE;
			}
		else
			switch (this)
			{
				case POINTER:
					if (__nc)
						return RegisterOperationType.OBJECT_FIELD_LOAD;
					return RegisterOperationType.X32_FIELD_LOAD;
				
				case BYTE:
				case SHORT:
				case INTEGER:
				case FLOAT:
					return RegisterOperationType.X32_FIELD_LOAD;
				
				case LONG:
				case DOUBLE:
					return RegisterOperationType.X64_FIELD_LOAD;
			}
		
		// Should not be reached
		throw new todo.OOPS();
	}
	
	/**
	 * Returns this as a basic Java type.
	 *
	 * @return The Java type of this.
	 * @since 2019/04/06
	 */
	public final JavaType toJavaType()
	{
		switch (this)
		{
			case POINTER:	return JavaType.OBJECT;
			case BYTE:
			case SHORT:
			case INTEGER:	return JavaType.INTEGER;
			case FLOAT:		return JavaType.FLOAT;
			case LONG:		return JavaType.LONG;
			case DOUBLE:	return JavaType.DOUBLE;
		}
		
		throw new todo.OOPS(this.name());
	}
	
	/**
	 * Returns the data type used for the primitive type.
	 *
	 * @param __t The type to use, {@code null} is treated as a pointer.
	 * @return The data type used.
	 * @since 2019/03/24
	 */
	public static final DataType of(PrimitiveType __t)
	{
		if (__t == null)
			return POINTER;
		
		switch (__t)
		{
			case BOOLEAN:
			case BYTE:		return BYTE;
			case CHARACTER:
			case SHORT:		return SHORT;
			case INTEGER:	return INTEGER;
			case LONG:		return LONG;
			case FLOAT:		return FLOAT;
			case DOUBLE:	return DOUBLE;
			
			default:
				throw new todo.OOPS(__t.toString());
		}
	}
	
	/**
	 * Returns the data type of the given Java type.
	 *
	 * @param __t The type to get.
	 * @return The data type used for this type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/27
	 */
	public static final DataType of(JavaType __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		return DataType.of(__t.type().primitiveType());
	}
}

