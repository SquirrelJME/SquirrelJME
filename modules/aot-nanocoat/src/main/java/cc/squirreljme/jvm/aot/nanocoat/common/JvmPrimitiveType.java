// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.common;

import cc.squirreljme.c.CMathOperator;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.JavaType;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodReference;
import net.multiphasicapps.classfile.PrimitiveType;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Primitive types within the virtual machine.
 *
 * @since 2023/07/16
 */
public enum JvmPrimitiveType
{
	/** Integer. */
	INTEGER,
	
	/** Long. */
	LONG,
	
	/** Float. */
	FLOAT,
	
	/** Double. */
	DOUBLE,
	
	/** Object. */
	OBJECT,
	
	/** Boolean or byte. */
	BOOLEAN_OR_BYTE,
	
	/** Short. */
	SHORT,
	
	/** Character. */
	CHARACTER,
	
	/* End. */
	;
	
	/** The Java Types available. */
	public static final List<JvmPrimitiveType> JAVA_TYPES =
		UnmodifiableList.of(Arrays.asList(JvmPrimitiveType.INTEGER,
			JvmPrimitiveType.LONG,
			JvmPrimitiveType.FLOAT,
			JvmPrimitiveType.DOUBLE,
			JvmPrimitiveType.OBJECT));
	
	/** The maximum number of Java Types. */
	public static final int NUM_JAVA_TYPES =
		JvmPrimitiveType.JAVA_TYPES.size();
	
	/**
	 * Returns the descriptor to use for this type.
	 * 
	 * @return The descriptor to use for this type.
	 * @since 2023/07/16
	 */
	public FieldDescriptor descriptor()
	{
		switch (this)
		{
			case INTEGER:
				return JavaType.INTEGER.type();
				
			case LONG:
				return JavaType.LONG.type();
			
			case FLOAT:
				return JavaType.FLOAT.type();
			
			case DOUBLE:
				return JavaType.DOUBLE.type();
				
				/* {@squirreljme.error NCa3 Has no valid descriptor.} */
			default:
				throw new IllegalArgumentException("NCa3");
		}
	}
	
	/**
	 * Returns the Java primitive type. 
	 *
	 * @return The Java primitive type.
	 * @since 2023/08/09
	 */
	public JvmPrimitiveType javaType()
	{
		switch (this)
		{
			case BOOLEAN_OR_BYTE:
			case SHORT:
			case CHARACTER:
				return JvmPrimitiveType.INTEGER;
		}
		
		return this;
	}
	
	/**
	 * Returns the reference to a soft comparison.
	 *
	 * @param __op The compare operation.
	 * @return The reference to the resultant method.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	public MethodReference softCompare(JvmCompareOp __op)
		throws NullPointerException
	{
		if (__op == null)
			throw new NullPointerException("NARG");
		
		// Which class do we use?
		ClassName useClass = this.softMathClass();
		
		// Name of the operation?
		String methodName;
		switch (__op)
		{
			case CMP:
				methodName = "cmp";
				break;
				
			case CMPG:
				methodName = "cmpg";
				break;
				
			case CMPL:
				methodName = "cmpl";
				break;
				
				/* {@squirreljme.error NCa7 Not valid for software math.} */
			default:
				throw new IllegalArgumentException("NCa7");
		}
		
		// Build
		FieldDescriptor self = this.descriptor();
		return new MethodReference(useClass,
			new MethodName(methodName),
			MethodDescriptor.ofArguments(self, self, FieldDescriptor.INTEGER),
			false);
	}
	
	/**
	 * Returns the method to use for software comparison.
	 *
	 * @param __to The target type.
	 * @return The reference to convert the type.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	public MethodReference softConvert(JvmPrimitiveType __to)
		throws NullPointerException
	{
		if (__to == null)
			throw new NullPointerException("NARG");
		
		// Which class do we use?
		ClassName useClass = this.softMathClass();
		
		// Which method to call?
		String methodName;
		switch (__to)
		{
			case INTEGER:
				methodName = "toInteger";
				break;
				
			case LONG:
				methodName = "toLong";
				break;
			
			case FLOAT:
				methodName = "toFloat";
				break;
			
			case DOUBLE:
				methodName = "toDouble";
				break;
				
				/* {@squirreljme.error NCad Not valid for software compare.} */
			default:
				throw new IllegalArgumentException("NCad");
		}
		
		// Build
		return new MethodReference(useClass,
			new MethodName(methodName),
			MethodDescriptor.ofArguments(__to.descriptor(), this.descriptor()),
			false);
	}
	
	/**
	 * Determines the method to use for software math operations.
	 * 
	 * @param __mathOp The math operator.
	 * @return The reference to the method that performs software math for
	 * this operation and type.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	public MethodReference softMath(CMathOperator __mathOp)
		throws NullPointerException
	{
		if (__mathOp == null)
			throw new NullPointerException("NARG");
		
		// Which class do we use?
		ClassName useClass = this.softMathClass();
		
		// Name of the operation?
		String methodName;
		switch (__mathOp)
		{
			case ADD:
				methodName = "add";
				break;
				
			case SUBTRACT:
				methodName = "sub";
				break;
				
			case MULTIPLY:
				methodName = "mul";
				break;
				
			case DIVIDE:
				methodName = "div";
				break;
				
			case REMAINDER:
				methodName = "rem";
				break;
				
			case AND:
				methodName = "and";
				break;
				
			case OR:
				methodName = "or";
				break;
				
			case XOR:
				methodName = "xor";
				break;
				
			case SHIFT_LEFT:
				methodName = "shl";
				break;
				
			case SHIFT_RIGHT:
				methodName = "shr";
				break;
				
				/* {@squirreljme.error NCa2 Not valid for software math.} */
			default:
				throw new IllegalArgumentException("NCa2");
		}
		
		// Determine the type of method used
		FieldDescriptor self = this.descriptor();
		MethodDescriptor type;
		switch (methodName)
		{
			case "shl":
			case "shr":
			case "ushr":
				type = MethodDescriptor.ofArguments(
					self,
					self, FieldDescriptor.INTEGER);
				break;
				
			default:
				type = MethodDescriptor.ofArguments(
					FieldDescriptor.INTEGER,
					self, self);
				break;
		}
		
		// Build
		return new MethodReference(useClass,
			new MethodName(methodName),
			type,
			false);
	}
	
	/**
	 * Returns the class to use for software math.
	 * 
	 * @return The class name to use for the software math.
	 * @since 2023/07/16
	 */
	public ClassName softMathClass()
	{
		String name;
		switch (this)
		{
			case INTEGER:
				name = "cc/squirreljme/jvm/SoftInteger";
				break;
				
			case LONG:
				name = "cc/squirreljme/jvm/SoftLong";
				break;
				
			case FLOAT:
				name = "cc/squirreljme/jvm/SoftFloat";
				break;
			
			case DOUBLE:
				name = "cc/squirreljme/jvm/SoftDouble";
				break;
				
				/* {@squirreljme.error NCa1 Not valid for software math.} */
			default:
				throw new IllegalArgumentException("NCa1");
		}
		
		return new ClassName(name);
	}
	
	/**
	 * Returns the method used for software negative.
	 *
	 * @return The method used for software negative.
	 * @since 2023/07/16
	 */
	public MethodReference softNegative()
	{
		// Build
		FieldDescriptor self = this.descriptor();
		return new MethodReference(this.softMathClass(),
			new MethodName("neg"),
			MethodDescriptor.ofArguments(self, self),
			false);
	}
	
	/**
	 * Determines the method to use for soft shifting. 
	 *
	 * @param __op The operation to use.
	 * @return The reference to the shifted method.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	public MethodReference softShift(JvmShiftOp __op)
		throws NullPointerException
	{
		if (__op == null)
			throw new NullPointerException("NARG");
		
		// Which class do we use?
		ClassName useClass = this.softMathClass();
		
		// Name of the operation?
		String methodName;
		switch (__op)
		{
			case SIGNED_SHIFT_LEFT:
				methodName = "shl";
				break;
				
			case SIGNED_SHIFT_RIGHT:
				methodName = "shr";
				break;
				
			case UNSIGNED_SHIFT_RIGHT:
				methodName = "ushr";
				break;
				
				/* {@squirreljme.error NCa2 Not valid for software math.} */
			default:
				throw new IllegalArgumentException("NCa2");
		}
		
		// Build
		FieldDescriptor self = this.descriptor();
		return new MethodReference(useClass,
			new MethodName(methodName),
			MethodDescriptor.ofArguments(self,
				self, FieldDescriptor.INTEGER),
			false);
	}
	
	/**
	 * Returns the primitive type associated with the given field.
	 *
	 * @param __fieldType The field type.
	 * @return The primitive type that is used.
	 * @throws IllegalArgumentException If the type is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/09
	 */
	public static JvmPrimitiveType of(FieldDescriptor __fieldType)
		throws IllegalArgumentException, NullPointerException
	{
		if (__fieldType == null)
			throw new NullPointerException("NARG");
		
		if (__fieldType.isPrimitive())
			return JvmPrimitiveType.of(__fieldType.primitiveType());
		return JvmPrimitiveType.OBJECT;
	}
	
	/**
	 * Returns the primitive type associated with the primitive type.
	 *
	 * @param __primitiveType The primitive type.
	 * @return The primitive type that is used.
	 * @throws IllegalArgumentException If the type is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/09
	 */
	private static JvmPrimitiveType of(PrimitiveType __primitiveType)
		throws IllegalArgumentException, NullPointerException
	{
		if (__primitiveType == null)
			throw new NullPointerException("NARG");
		
		switch (__primitiveType)
		{
			case BYTE:
			case BOOLEAN:
				return JvmPrimitiveType.BOOLEAN_OR_BYTE;
				
			case SHORT:
				return JvmPrimitiveType.SHORT;
				
			case CHARACTER:
				return JvmPrimitiveType.CHARACTER;
				
			case INTEGER:
				return JvmPrimitiveType.INTEGER;
				
			case LONG:
				return JvmPrimitiveType.LONG;
				
			case FLOAT:
				return JvmPrimitiveType.FLOAT;
				
			case DOUBLE:
				return JvmPrimitiveType.DOUBLE;
		}
		
		throw new IllegalArgumentException("IARG " + __primitiveType);
	}
	
	/**
	 * Maps the Class file Java Type to the primitive type used by NanoCoat
	 *
	 * @param __type The type to map.
	 * @return The resultant primitive type.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/10
	 */
	public static JvmPrimitiveType of(JavaType __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error NC02 Cannot map nothing or top type.}. */ 
		if (__type.isNothing() || __type.isTop())
			throw new IllegalArgumentException("NC02");
		
		// Basic types
		if (JavaType.INTEGER.equals(__type))
			return JvmPrimitiveType.INTEGER;
		else if (JavaType.LONG.equals(__type))
			return JvmPrimitiveType.LONG;
		else if (JavaType.FLOAT.equals(__type))
			return JvmPrimitiveType.FLOAT;
		else if (JavaType.DOUBLE.equals(__type))
			return JvmPrimitiveType.DOUBLE;
		
		// Assume object otherwise
		return JvmPrimitiveType.OBJECT;
	}
}
