// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

/**
 * This interface is called during the parsing of the code attribute within
 * a method.
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
	 * @since 2016/09/09
	 */
	public abstract void atInstruction(int __code, int __pos);
	
	/**
	 * This specifies the length of the program.
	 *
	 * @param __n The number of bytes the byte code is.
	 * @since 2016/09/16
	 */
	public abstract void codeLength(int __n);
	
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
	 * Invokes the given method using the specified linkage and invocation
	 * type.
	 *
	 * @param __link The method linkage.
	 * @param __rv The variable to obtain the return value.
	 * @param __args The input arguments to the method.
	 * @throws NullPointerException On null arguments, except for {@code __rv}
	 * if there is no return value.
	 * @since 2016/09/16
	 */
	public abstract void invokeMethod(MethodLinkage __link, CodeVariable __rv,
		CodeVariable... __args)
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
	 * This is called when the number of variables that are stored on the
	 * stack and the local variables is known.
	 *
	 * @param __ms The number of stack variables.
	 * @param __ml The number of local variables.
	 * @since 2016/09/09
	 */
	public abstract void variableCounts(int __ms, int __ml);
}

