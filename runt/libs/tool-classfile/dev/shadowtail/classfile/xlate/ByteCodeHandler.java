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
	 * Performs a copy operation.
	 *
	 * @param __in The input.
	 * @param __out The output.
	 * @since 2019/04/07
	 */
	public abstract void doCopy(JavaStackResult.Input __in,
		JavaStackResult.Output __out);
	
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
	public abstract void doStateOperations(StateOperation... __ops);
	
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

