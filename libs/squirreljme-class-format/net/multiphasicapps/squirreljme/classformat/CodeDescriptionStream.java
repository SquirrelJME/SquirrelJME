// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

import net.multiphasicapps.squirreljme.linkage.ClassLinkage;
import net.multiphasicapps.squirreljme.linkage.MethodLinkage;

/**
 * This interface is called during the parsing of the code attribute within
 * a method. In most cases all methods should operate as if they do not require
 * their input state to be changed.
 *
 * However as an exception to this there is potential for states to be
 * transitioned where multiple methods are called per instruction. When this
 * occurs {@link #transitionState()} will be called.
 *
 * @since 2016/09/09
 */
public interface CodeDescriptionStream
{
	/**
	 * This is called when the instruction parse pointer is at the given
	 * position.
	 *
	 * @param __code The operation that is about to be decoded.
	 * @param __pos The position of the given operation.
	 * @param __eh The exceptions under effect in this instruction, will be
	 * {@code null} if there are none.
	 * @since 2016/09/09
	 */
	public abstract void atInstruction(int __code, int __pos,
		ExceptionHandler[] __eh);
	
	/**
	 * This specifies the length of the program.
	 *
	 * @param __n The number of bytes the byte code is.
	 * @since 2016/09/16
	 */
	public abstract void codeLength(int __n);
	
	/**
	 * Compares the two code variables and if the comparison type matches a
	 * branch will occur.
	 *
	 * @param __c The type of comparison to perform.
	 * @param __a The first variable.
	 * @param __b The second variable.
	 * @param __jt On success the branch will be taken to this instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/01/01
	 */
	public abstract void compareAndBranch(ObjectCompareType __c,
		CodeVariable __a, CodeVariable __b, int __jt);
		throws NullPointerException;
	
	/**
	 * Compares the two code variables and if the comparison type matches a
	 * branch will occur.
	 *
	 * @param __c The type of comparison to perform.
	 * @param __a The first variable.
	 * @param __b The second variable.
	 * @param __jt On success the branch will be taken to this instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/01/01
	 */
	public abstract void compareAndBranch(IntegerCompareType __c,
		CodeVariable __a, CodeVariable __b, int __jt)
		throws NullPointerException;
	
	/**
	 * Copies the value of one variable to another.
	 *
	 * @param __type The type of value to copy.
	 * @param __from The source variable.
	 * @param __to The destination variable.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/16
	 */
	public abstract void copy(StackMapType __type, CodeVariable __from,
		CodeVariable __to)
		throws NullPointerException;
	
	/**
	 * This is called when the instruction parse pointer has finished a given
	 * instruction and is ready to move to the next.
	 *
	 * @param __code The operation that is was decoded.
	 * @param __pos The position of the given operation.
	 * @param __next The position of the next instruction for natural program
	 * flow, if there is none then the value will be negative.
	 * @param __eh The exceptions under effect in this instruction, will be
	 * {@code null} if there are none.
	 * @since 2017/02/23
	 */
	public abstract void endInstruction(int __code, int __pos, int __next,
		ExceptionHandler[] __eh);
	
	/**
	 * This is called when the method has been fully parsed.
	 *
	 * @since 2017/03/23
	 */
	public abstract void endMethod();
	
	/**
	 * Specifies the initial argument types which are used on entry to the
	 * method.
	 *
	 * @param __cv The positions of the variables.
	 * @param __st Identity mapped to the {@__cv} argument, specifies the type
	 * of value that is stored at the given position.
	 * @param __sh The height of the stack.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/08
	 */
	public abstract void initialArguments(CodeVariable[] __cv,
		StackMapType[] __st, int __sh)
		throws NullPointerException;
	
	/**
	 * Invokes the given method using the specified linkage and invocation
	 * type.
	 *
	 * @param __link The method linkage.
	 * @param __d The depth of the Java stack when all arguments have been
	 * popped and no return value has been pushed, this can be used to not
	 * preserve certain values.
	 * @param __rv The variable to obtain the return value.
	 * @param __rvt The type of return value returned.
	 * @param __cargs The input arguments to the method, the code variables
	 * which are used.
	 * @param __targ The stack map types for the input arguments.
	 * @throws NullPointerException On null arguments, except for {@code __rv}
	 * if there is no return value.
	 * @since 2016/09/16
	 */
	public abstract void invokeMethod(MethodLinkage __link, int __d,
		CodeVariable __rv, StackMapType __rvt, CodeVariable[] __cargs,
		StackMapType[] __targ)
		throws NullPointerException;
	
	/**
	 * This is called when the jump targets in the code parser are known. This
	 * may be used by the code being given this information to determine if
	 * state may need to be stored for a given position.
	 *
	 * @param __t The instructional jump targets.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/09
	 */
	public abstract void jumpTargets(int[] __t)
		throws NullPointerException;
	
	/**
	 * This loads the given reference to the class object of the specified
	 * class into the specified variable.
	 *
	 * @param __cl The class constant to place.
	 * @param __cv The target slot where the constant is to be placed.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/24
	 */
	public abstract void loadConstant(ClassLinkage __cl, CodeVariable __cv)
		throws NullPointerException;
	
	/**
	 * Potentially returns the specified value.
	 *
	 * @param __t The type of value to return.
	 * @param __cv The variable containing the return value.
	 * @throws NullPointerException If one argument is {@code null} and the
	 * other is not.
	 * @since 2017/03/23
	 */
	public abstract void returnValue(StackMapType __t, CodeVariable __cv)
		throws NullPointerException;
	
	/**
	 * This is called when 
	 *
	 * @since 2017/03/30
	 */
	public abstract void transitionState();
	
	/**
	 * This is called when the number of variables that are stored on the
	 * stack and the local variables is known.
	 *
	 * @param __ms The number of stack variables.
	 * @param __ml The number of local variables.
	 * @param __mw The number of work variables.
	 * @since 2016/09/09
	 */
	public abstract void variableCounts(int __ms, int __ml, int __mw);
}

