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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.JavaType;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodReference;

/**
 * Primitive types within the virtual machine.
 *
 * @since 2023/07/16
 */
public enum JvmPrimitiveType
{
	/** Boolean or byte. */
	BOOLEAN_OR_BYTE,
	
	/** Short. */
	SHORT,
	
	/** Character. */
	CHARACTER,
	
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
				
				// {@squirreljme.error NCa3 Has no valid descriptor.}
			default:
				throw new IllegalArgumentException("NCa3");
		}
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
				
				// {@squirreljme.error NCa2 Not valid for software math.}
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
			case LONG:
				name = "cc/squirreljme/jvm/SoftLong";
				break;
				
			case FLOAT:
				name = "cc/squirreljme/jvm/SoftFloat";
				break;
			
			case DOUBLE:
				name = "cc/squirreljme/jvm/SoftDouble";
				break;
				
				// {@squirreljme.error NCa1 Not valid for software math.}
			default:
				throw new IllegalArgumentException("NCa1");
		}
		
		return new ClassName(name);
	}
}
