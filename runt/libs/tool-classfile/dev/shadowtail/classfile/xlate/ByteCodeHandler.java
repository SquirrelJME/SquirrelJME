// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.InstructionJumpTarget;
import net.multiphasicapps.classfile.MethodReference;

/**
 * This class is used by the byte code processor to handle all of the various
 * instructions and such.
 *
 * @since 2019/04/06
 */
public interface ByteCodeHandler
{
	/**
	 * Read length of array.
	 *
	 * @param __in The instance.
	 * @param __len The output length.
	 * @since 2019/04/12
	 */
	public abstract void doArrayLength(JavaStackResult.Input __in,
		JavaStackResult.Output __len);
	
	/**
	 * Load from array.
	 *
	 * @param __dt The type of data to load.
	 * @param __in The instance.
	 * @param __dx The index.
	 * @param __v The value.
	 * @since 2019/04/12
	 */
	public abstract void doArrayLoad(DataType __dt,
		JavaStackResult.Input __in, JavaStackResult.Input __dx,
		JavaStackResult.Output __v);
	
	/**
	 * Store into array.
	 *
	 * @param __dt The type of data to store.
	 * @param __in The instance.
	 * @param __dx The index.
	 * @param __v The value.
	 * @since 2019/04/12
	 */
	public abstract void doArrayStore(DataType __dt,
		JavaStackResult.Input __in, JavaStackResult.Input __dx,
		JavaStackResult.Input __v);
	
	/**
	 * Check that an object is of a given type or thrown exception.
	 *
	 * @param __cl The class to check.
	 * @param __v The object to check.
	 * @since 2019/04/12
	 */
	public abstract void doCheckCast(ClassName __cl,
		JavaStackResult.Input __v);
	
	/**
	 * Performs convert of value.
	 *
	 * @param __as The source type.
	 * @param __a The input.
	 * @param __bs The output type.
	 * @param __b The output.
	 * @since 2019/04/16
	 */
	public abstract void doConvert(StackJavaType __as,
		JavaStackResult.Input __a, StackJavaType __bs,
		JavaStackResult.Output __b);
	
	/**
	 * Performs a copy operation.
	 *
	 * @param __in The input.
	 * @param __out The output.
	 * @since 2019/04/07
	 */
	public abstract void doCopy(JavaStackResult.Input __in,
		JavaStackResult.Output __out);
	
	/**
	 * Reads a field.
	 *
	 * @param __fr The field reference.
	 * @param __i The instance.
	 * @param __v The output value.
	 * @since 2019/04/12
	 */
	public abstract void doFieldGet(FieldReference __fr,
		JavaStackResult.Input __i, JavaStackResult.Output __v);
	
	/**
	 * Puts a field.
	 *
	 * @param __fr The field reference.
	 * @param __i The instance.
	 * @param __v The value.
	 * @since 2019/04/12
	 */
	public abstract void doFieldPut(FieldReference __fr,
		JavaStackResult.Input __i, JavaStackResult.Input __v);
	
	/**
	 * Compares two values and then possibly jumps.
	 *
	 * @param __a The first value.
	 * @param __b The second value.
	 * @param __ijt The jump target.
	 * @since 2019/04/12
	 */
	public abstract void doIfICmp(CompareType __ct, JavaStackResult.Input __a,
		JavaStackResult.Input __b, InstructionJumpTarget __ijt);
	
	/**
	 * Invocation of a method.
	 *
	 * @param __t The type of invoke to be performed.
	 * @param __r The reference to the method.
	 * @param __out The output.
	 * @param __in The arguments to the method.
	 * @since 2019/04/10
	 */
	public abstract void doInvoke(InvokeType __t, MethodReference __r,
		JavaStackResult.Output __out, JavaStackResult.Input... __in);
	
	/**
	 * Performs math operation.
	 *
	 * @param __dt The type to operate on.
	 * @param __mt The math operation to perform.
	 * @param __a Argument A.
	 * @param __b Argument B.
	 * @param __c Output.
	 * @since 2019/04/07
	 */
	public abstract void doMath(StackJavaType __dt, MathType __mt,
		JavaStackResult.Input __a, JavaStackResult.Input __b,
		JavaStackResult.Output __c);
	
	/**
	 * Performs math operation.
	 *
	 * @param __dt The type to operate on.
	 * @param __mt The math operation to perform.
	 * @param __a Argument A.
	 * @param __b Constant B.
	 * @param __c Output.
	 * @since 2019/04/07
	 */
	public abstract void doMath(StackJavaType __dt, MathType __mt,
		JavaStackResult.Input __a, Number __b, JavaStackResult.Output __c);
	
	/**
	 * Allocates new class.
	 *
	 * @param __cn The class to allocate.
	 * @param __out The output.
	 * @since 2019/04/11
	 */
	public abstract void doNew(ClassName __cn, JavaStackResult.Output __out);
	
	/**
	 * Allocates a new array.
	 *
	 * @param __at The array type with all the needed dimensions.
	 * @param __len The length of the array.
	 * @param __out Where the array is to be stored.
	 * @since 2019/04/12
	 */
	public abstract void doNewArray(ClassName __at,
		JavaStackResult.Input __len, JavaStackResult.Output __out);
	
	/**
	 * Loads pool value.
	 *
	 * @param __v The value to load.
	 * @param __out The output.
	 * @since 2019/04/12
	 */
	public abstract void doPoolLoad(Object __v, JavaStackResult.Output __out);
	
	/**
	 * Return of value.
	 *
	 * @param __rv The return value, {@code null} means no value was returned.
	 * @since 2019/04/11
	 */
	public abstract void doReturn(JavaStackResult.Input __in);
	
	/**
	 * Generates code for state operations.
	 *
	 * @param __ops The operations to perform.
	 * @since 2019/04/11
	 */
	public abstract void doStateOperations(StateOperations __ops);
	
	/**
	 * Reads a static field.
	 *
	 * @param __fr The field to read from.
	 * @param __v The output register.
	 * @since 2019/04/12
	 */
	public abstract void doStaticGet(FieldReference __fr,
		JavaStackResult.Output __v);
	
	/**
	 * Writes a static field.
	 *
	 * @param __fr The field to write to.
	 * @param __v The input register.
	 * @since 2019/04/13
	 */
	public abstract void doStaticPut(FieldReference __fr,
		JavaStackResult.Input __v);
	
	/**
	 * Throws the given object.
	 *
	 * @param __in The object to throw.
	 * @since 2019/04/12
	 */
	public abstract void doThrow(JavaStackResult.Input __in);
	
	/**
	 * Finishes handling the instruction operation.
	 *
	 * @since 2019/04/07
	 */
	public abstract void instructionFinish();
	
	/**
	 * Sets up before processing the instruction.
	 *
	 * @since 2019/04/07
	 */
	public abstract void instructionSetup();
	
	/**
	 * Returns the state of the byte code, this must always return the
	 * same object.
	 *
	 * @return The byte code state.
	 * @since 2019/04/06
	 */
	public abstract ByteCodeState state();
}

