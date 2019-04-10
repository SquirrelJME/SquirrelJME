// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

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

