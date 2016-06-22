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
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/22
	 */
	@Override
	public void activateClass(ClassNameSymbol __c)
	{
		throw new Error("TODO");
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
}

