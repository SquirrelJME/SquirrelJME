// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This interface is used by the class writer to write output logic to the
 * resulting native machine code generator.
 *
 * The JIT itself converts the stack based machine to a completely register
 * based machine. As such, stack variables are offset by the number of local
 * variables meaning that if any value is greater than {@code maxlocals} then
 * it is a temporary stack item.
 *
 * @since 2016/08/19
 */
public interface JITMethodWriter
	extends AutoCloseable
{
	/**
	 * This is called at the start before an instruction is operated on so
	 * that a JIT implementation may perform unspecified tasks if required.
	 *
	 * @param __code The Java byte code operation.
	 * @param __pos The position of the operation.
	 * @throws JITException If there was a problem using the instruction.
	 * @since 2016/09/04
	 */
	public abstract void atInstruction(int __code, int __pos)
		throws JITException;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/19
	 */
	@Override
	public abstract void close()
		throws JITException;
	
	/**
	 * This is called whenever a method invocation is performed so that the
	 * callee JIT can generate code for method linking and invocation.
	 *
	 * @param __type The type of invocation to perform.
	 * @param __pid The pool ID of the method reference.
	 * @param __ref The method to be invoked.
	 * @param __st The types associated with the invocation.
	 * @param __sp The input variable IDs, these may be local variables or
	 * stack variables.
	 * @param __rvt The type of return value the method uses, will be
	 * {@code null} if no value is returned
	 * @param __rv This is the register which is given the return value of a
	 * method if applicable, this will be a negative value if it is not used.
	 * Long and double return values additional place their value in the next
	 * variable entry if applicable.
	 * @throws JITException If the method could not be invoked.
	 * @throws NullPointerException On null arguments, except for
	 * {@code __rvt}.
	 * @since 2016/09/06
	 */
	public abstract void invoke(JITInvokeType __type, int __pid,
		JITMethodReference __ref, JITVariableType[] __st, int[] __sp,
		JITVariableType __rvt, int __rv)
		throws JITException, NullPointerException;
	
	/**
	 * Indicates the jump targets that are used in the method which may be used
	 * by an implementation of the JIT to only store state for these specific
	 * locations.
	 *
	 * For example, there could be a method that could have 65,000
	 * instructions in it. In this method there are no exception handlers and
	 * there are no {@code goto}s. Without the jump target table, the JIT
	 * would have to save a state so it may be restored for every instruction
	 * even when it would never be used. This method is used to prevent that
	 * waste from occuring.
	 *
	 * @param __jt The jump targets in the method.
	 * @throws JITException If the jump targets could not be set.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/03
	 */
	public abstract void jumpTargets(int[] __jt)
		throws JITException, NullPointerException;
	
	/**
	 * Primes the argument methods, essentially the input used for the method
	 * as it varies between methods. If the method is an instance method
	 * then the first argument will be {@link JITVariableType#OBJECT}.
	 *
	 * All of the input arguments map to virtual registers starting from
	 * {@code 0}.
	 *
	 * Any {@code null} elements in the type array specify that the area is
	 * not used and would be the top of a {@code float} or {@code double}.
	 *
	 * @param __t The variable types which are used in input.
	 * @throws JITException If the arguments could not be mapped.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/29
	 */
	public abstract void primeArguments(JITVariableType[] __t)
		throws JITException, NullPointerException;
	
	/**
	 * This indicates the number of stack entries and local variable entries
	 * that are used in the byte code that this method has.
	 *
	 * @param __stack The number of stack entries.
	 * @param __locals The number of local variables.
	 * @throws JITException If they could not be counted.
	 * @since 2016/09/03
	 */
	public abstract void variableCounts(int __stack, int __locals)
		throws JITException;
}

