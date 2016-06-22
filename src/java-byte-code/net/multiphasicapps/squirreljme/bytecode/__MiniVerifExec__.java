// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bytecode;

import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This is a miniature verification state calculator which can execute micro
 * operations so that the state of the stack and local variables can be
 * determined for an instruction.
 *
 * @since 2016/06/22
 */
class __MiniVerifExec__
	implements BCMicroExecution
{
	/** Local variables. */
	private final BCVariableType[] _locals;
	
	/** Stack variables. */
	private final BCVariableType[] _stack;
	
	/** The top of the stack. */
	private volatile int _top;
	
	/**
	 * Intializes the microoperation based next verification state.
	 *
	 * @param __base The base verification state.
	 * @param __op The operation to execute
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/22
	 */
	__MiniVerifExec__(BCStateVerification __base, BCOperation __op)
		throws NullPointerException
	{
		// Check
		if (__base == null || __op == null)
			throw new NullPointerException("NARG");
		
		// Copy locals
		BCStateVerification.Locals il = __base.locals();
		int iln = il.size();
		BCVariableType[] locals = new BCVariableType[iln];
		for (int i = 0; i < iln; i++)
			locals[i] = il.get(i);
		this._locals = locals;
		
		// Copy the stack
		BCStateVerification.Stack is = __base.stack();
		int isn = is.size();
		BCVariableType[] stack = new BCVariableType[isn];
		for (int i = 0; i < isn; i++)
			stack[i] = is.get(i);
		this._stack = stack;
		this._top = is.top();
		
		// Perform execution
		__op.microOpExecute(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/22
	 */
	@Override
	public void activateClass(ClassNameSymbol __c)
	{
		// The last active class need not be remembered
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/22
	 */
	@Override
	public void activateField(boolean __static, ClassNameSymbol __c,
		IdentifierSymbol __n, FieldSymbol __t)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/22
	 */
	@Override
	public void activateMethod(boolean __static, ClassNameSymbol __c,
		IdentifierSymbol __n, FieldSymbol __t)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/22
	 */
	@Override
	public void allocateClass(int __reg)
	{
		__write(BCVariableType.OBJECT, __reg);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/22
	 */
	@Override
	public void adjustStackTop(int __dif)
	{
		// Get
		BCVariableType[] stack = this._stack;
		int top = this._top;
		
		// Adjust
		int old = top;
		top += __dif;
		
		// {@squirreljme.error AX13 The new top of the stack exceeds the bounds
		// of the stack. (The adjusted size; The maximum stack size; The
		// previous unadjusted size)}
		if (top < 0 || top > stack.length)
			throw new BCException(String.format("AX13 %d %d %d", top,
				stack.length, old));
		
		// Set
		this._top = top;
		
		// Wipe variables on the stack
		for (int i = old; i < top; i++)
		{
			// {@squirreljme.error AX14 Top of a long/double was clipped during
			// a stack top adjustment.}
			if (i > 0)
			{
				BCVariableType was = stack[i - 1];
				if (was != null && was.isWide())
					throw new BCException("AX14");
			}
			
			// Clear it
			stack[i] = null;
		}
	}
	
	/**
	 * Writes the given type at the given location.
	 *
	 * @param __vt The type to write.
	 * @param __reg The location to write it.
	 * @throws BCException If it could not be written to.
	 * @since 2016/06/22
	 */
	private void __write(BCVariableType __vt, int __reg)
	{
		// On the stack?
		if (__reg < 0)
		{
			// Correct
			__reg = -(__reg + 1);
			
			// Get
			BCVariableType[] stack = this._stack;
			int top = this._top;
			
			// {@squirreljme.error AX15 Write to given variable is off the
			// current size of the stack. (The type to write;
			// The stack location; The current stack top)}
			if (__reg < 0 || __reg >= top)
				throw new BCException(String.format("AX15 %s %d", __vt,
					__reg, top));
		}
		
		// Local
		else
			throw new Error("TODO");
	}
}

